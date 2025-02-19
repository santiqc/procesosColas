package com.procesos.colas.Infrastructure.repository;


import com.procesos.colas.domain.tutelas.TutelaArchivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutelaArchivoRepository extends JpaRepository<TutelaArchivo, Long> {

    List<TutelaArchivo> findByTutelaId(Long id);
    List<TutelaArchivo> findByTutelaIdAndTipo(Long id, String tipo);

}
