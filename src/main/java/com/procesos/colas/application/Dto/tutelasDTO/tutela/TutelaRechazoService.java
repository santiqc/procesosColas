package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.RechazoTutelaDto;

public interface TutelaRechazoService {

    Tutela rechazoTutela(Long tutelaId, RechazoTutelaDto rechazoTutelaDto);

}
