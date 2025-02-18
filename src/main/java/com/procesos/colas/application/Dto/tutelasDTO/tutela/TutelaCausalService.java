package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.tutela.dto.TutelaCausalDto;

import java.util.List;

public interface TutelaCausalService {

    List<TutelaCausalDto> obtenerCausales();
}
