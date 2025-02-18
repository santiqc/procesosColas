package com.procesos.colas.Infrastructure.repository;

import com.procesos.colas.domain.Addressee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AddresseeRepository extends JpaRepository<Addressee, Long> {

    @Transactional
    Optional<Addressee> findByDocumentNumber(String documentNumber);

    @Transactional
    Optional<Addressee> findByEmail(String email);

}
