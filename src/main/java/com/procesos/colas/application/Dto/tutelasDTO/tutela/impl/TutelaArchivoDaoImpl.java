package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.procesos.colas.Infrastructure.repository.TutelaArchivoRepository;
import com.procesos.colas.application.Dto.tutelasDTO.tutela.TutelaArchivoDao;
import com.procesos.colas.domain.tutelas.TutelaArchivo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class TutelaArchivoDaoImpl implements TutelaArchivoDao {

    private final TutelaArchivoRepository repository;

    public TutelaArchivoDaoImpl(TutelaArchivoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TutelaArchivo> guardarArchivos(List<TutelaArchivo> archivos) {
        return repository.saveAll(archivos);
    }


}
