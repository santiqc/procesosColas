package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.domain.exception.CustomHttpException;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.TutelaAprobacionDto;

public interface TutelaAprobacionService {
    Tutela tutelaAprobacion(Long id, TutelaAprobacionDto tutelaAprobacionDto) throws CustomHttpException;
}
