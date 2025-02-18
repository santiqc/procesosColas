package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.application.services.tutela.dto.TutelaUsuarioDto;

import java.util.List;

public interface TutelasUsuariosService {

    List<TutelaUsuarioDto> obtenerUsuariosAsignadores();

}
