package com.procesos.colas.Infrastructure.repository;

import com.procesos.colas.domain.tutelas.Tutela;

import jdk.jfr.Registered;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


@Registered
public interface TutelasRepository extends JpaRepository<Tutela, Long>, JpaSpecificationExecutor<Tutela> {

    @Modifying
    @Transactional
    @Query("UPDATE Tutela t SET t.nodeId = :nodeId WHERE t.idTutela = :idTutela")
    int actualizarNodeId(@Param("idTutela") Long idTutela, @Param("nodeId") String nodeId);

}
