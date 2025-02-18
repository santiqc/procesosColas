package com.procesos.colas.Infrastructure.repository;

import com.procesos.colas.domain.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaEmailRepository extends JpaRepository<Email, Long> {
    @Transactional
    @Query("SELECT e FROM Email e")
    Page<Email> filterEmails(Pageable pageable);

    @Transactional
    @Query("SELECT e FROM Email e WHERE e.trackingId = :trackingId")
    Optional<Email> findByTrackingId(@Param("trackingId") String trackingId);

    @Transactional
    @Query("SELECT e FROM Email e WHERE e.idHistory = :idHistory OR e.trackingId = :trackingId")
    Optional<Email> findByIdHistoryOrTrackingId(@Param("idHistory") Long idHistory,
                                                @Param("trackingId") String trackingId);

    @Transactional
    @Query("SELECT e FROM Email e WHERE e.hasWitness = false")
    List<Email> findByHasWitnessFalse();
}
