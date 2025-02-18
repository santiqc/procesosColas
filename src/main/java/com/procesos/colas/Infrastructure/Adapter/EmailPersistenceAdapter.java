package com.procesos.colas.Infrastructure.Adapter;


importcom.procesos.colas.centronotificaciones.Infrastructure.repository.AddresseeRepository;
import com.procesos.colas.Infrastructure.repository.ApplicationRepository;
import com.procesos.colas.Infrastructure.repository.FilesRepository;
import com.procesos.colas.Infrastructure.repository.JpaEmailRepository;
import com.procesos.colas.domain.Addressee;
import com.procesos.colas.domain.Application;
import com.procesos.colas.domain.Email;
import com.procesos.colas.domain.Files;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@Component
public class EmailPersistenceAdapter {
    private final JpaEmailRepository emailRepository;
    private final AddresseeRepository addresseeRepository;
    private final FilesRepository filesRepository;
    private final ApplicationRepository applicationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EmailPersistenceAdapter(JpaEmailRepository emailRepository, AddresseeRepository addresseeRepository, FilesRepository filesRepository, ApplicationRepository applicationRepository) {
        this.emailRepository = emailRepository;
        this.addresseeRepository = addresseeRepository;
        this.filesRepository = filesRepository;
        this.applicationRepository = applicationRepository;
    }

    public Email saveEmail(Email email) {
        return emailRepository.save(email);
    }

    public List<Email> saveAllEmail(List<Email> emails) {
        return emailRepository.saveAll(emails);
    }

    public Optional<Email> findEmailByTrackingId(String trackingId) {
        return emailRepository.findByTrackingId(trackingId);
    }

    public Optional<Addressee> findAddresseeByNroDocument(String nroDocument) {
        return addresseeRepository.findByDocumentNumber(nroDocument);
    }
    @Transactional
    public Optional<Addressee> findAddresseeByEmail(String nroDocument) {
        return addresseeRepository.findByEmail(nroDocument);
    }

    public List<Files> findFilesByIdHistoryOrTrackingId(String trackingId, Long idHistory) {
        return filesRepository.findFilesByIdHistoryOrTrackingId(idHistory, trackingId);
    }

    @Transactional
    public Page<Email> filterEmailsByStatusCcAndProcess(String status, String process, String documentNumber,
                                                        LocalDateTime startDate, LocalDateTime endDate, String of,
                                                        String to, String subject, String containsWords, Boolean containsAttachments,
                                                        Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Email> query = cb.createQuery(Email.class);
        Root<Email> email = query.from(Email.class);

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Email> countRoot = countQuery.from(Email.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        if (status != null) {
            predicates.add(cb.equal(email.get("status"), status));
            countPredicates.add(cb.equal(countRoot.get("status"), status));
        }
        if (process != null) {
            predicates.add(cb.equal(email.get("process"), process));
            countPredicates.add(cb.equal(countRoot.get("process"), process));
        }

        if (documentNumber != null) {
            Join<Email, Addressee> addressee = email.join("addressees", JoinType.LEFT);
            predicates.add(cb.equal(addressee.get("documentNumber"), documentNumber));

            Join<Email, Addressee> countAddressee = countRoot.join("addressees", JoinType.LEFT);
            countPredicates.add(cb.equal(countAddressee.get("documentNumber"), documentNumber));
        }

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(email.get("eventdate"), startDate));
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("eventdate"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(email.get("eventdate"), endDate));
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("eventdate"), endDate));
        }
        if (of != null) {
            predicates.add(cb.equal(email.get("since"), of));
            countPredicates.add(cb.equal(countRoot.get("since"), of));
        }

        if (to != null) {
            predicates.add(cb.equal(email.get("forTo"), to));
            countPredicates.add(cb.equal(countRoot.get("forTo"), to));
        }
        if (subject != null && !subject.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(email.get("subject")), "%" + subject.toLowerCase().trim() + "%"));
            countPredicates.add(cb.like(cb.lower(countRoot.get("subject")), "%" + subject.toLowerCase().trim() + "%"));
        }

        if (containsWords != null && !containsWords.trim().isEmpty()) {
            String[] words = containsWords.toLowerCase().trim().split("\\s+");
            List<Predicate> wordPredicates = new ArrayList<>();

            for (String word : words) {
                String cleanWord = word.trim();
                if (!cleanWord.isEmpty()) {
                    Predicate subjectPredicate = cb.like(cb.lower(email.get("subject")), "%" + cleanWord + "%");

                    // Forzar conversión a texto concatenando string vacío
                    Expression<String> bodyText = cb.concat(email.get("body"), "");
                    Predicate bodyPredicate = cb.like(cb.lower(bodyText), "%" + cleanWord + "%");

                    wordPredicates.add(cb.or(subjectPredicate, bodyPredicate));
                }
            }

            if (!wordPredicates.isEmpty()) {
                predicates.add(cb.and(wordPredicates.toArray(new Predicate[0])));
            }
        }


        if (containsAttachments != null && containsAttachments) {

            Subquery<Long> fileSubquery = query.subquery(Long.class);
            Root<Files> fileRoot = fileSubquery.from(Files.class);

            fileSubquery.select(cb.count(fileRoot))
                    .where(
                            cb.and(
                                    cb.equal(fileRoot.get("trackingId"), email.get("trackingId")),
                                    cb.equal(fileRoot.get("witness"), false)
                            )
                    );
            predicates.add(cb.greaterThan(fileSubquery, 0L));


            Subquery<Long> countFileSubquery = countQuery.subquery(Long.class);
            Root<Files> countFileRoot = countFileSubquery.from(Files.class);

            countFileSubquery.select(cb.count(countFileRoot))
                    .where(
                            cb.and(
                                    cb.equal(countFileRoot.get("trackingId"), countRoot.get("trackingId")),
                                    cb.equal(countFileRoot.get("witness"), false)
                            )
                    );
            countPredicates.add(cb.greaterThan(countFileSubquery, 0L));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(email.get("sentAt")));
        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        List<Email> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Addressee saveAddressee(Addressee addressee) {
        return addresseeRepository.save(addressee);
    }

    public List<Addressee> saveAllAddressee(List<Addressee> addressee) {
        return addresseeRepository.saveAll(addressee);
    }

    public Object saveFiles(List<Files> files) {
        return filesRepository.saveAll(files);
    }


    public Optional<Email> findByIdHistoryOrTrackingId(Long idHistory, String trackingId) {
        return emailRepository.findByIdHistoryOrTrackingId(idHistory, trackingId);
    }

    public List<Email> findByHasWitnessFalse(){
        return emailRepository.findByHasWitnessFalse();
    }
}
