package com.procesos.colas.Infrastructure.repository;


import com.procesos.colas.domain.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FilesRepository extends JpaRepository<Files, Long> {


    @Query("SELECT f FROM Files f WHERE f.trackingId = :trackingId")
    List<Files> findFilesByTrackingId(@Param("trackingId") String trackingId);

    @Query("SELECT f FROM Files f WHERE f.idHistory = :idHistory OR f.trackingId = :trackingId")
    List<Files> findFilesByIdHistoryOrTrackingId(@Param("idHistory") Long idHistory,
                                                 @Param("trackingId") String trackingId);
}