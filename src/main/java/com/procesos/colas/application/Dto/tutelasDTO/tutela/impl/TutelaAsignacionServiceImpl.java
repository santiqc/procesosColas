package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaAsignacionService;
import com.prolinktic.sgdea.domain.model.tutelas.AsignacionTutelas;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.AsignacionTutelasRepository;
import org.springframework.stereotype.Service;

@Service
public class TutelaAsignacionServiceImpl implements TutelaAsignacionService {

    private final AsignacionTutelasRepository asignacionTutelasRepository;

    public TutelaAsignacionServiceImpl(AsignacionTutelasRepository asignacionTutelasRepository) {
        this.asignacionTutelasRepository = asignacionTutelasRepository;
    }

    @Override
    public AsignacionTutelas guardarAsignacion(AsignacionTutelas asignacion) {
        return asignacionTutelasRepository.save(asignacion);
    }
}
