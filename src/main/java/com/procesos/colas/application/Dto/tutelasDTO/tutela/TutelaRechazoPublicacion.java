package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.PeticionPublicacionDto;

public interface TutelaRechazoPublicacion {

    boolean rechazoPublicacion(Long tutelaId, PeticionPublicacionDto data);
    boolean publicacion(Long tutelaId, PeticionPublicacionDto data);
    boolean cerrar(Long tutelaId, PeticionPublicacionDto data);

}
