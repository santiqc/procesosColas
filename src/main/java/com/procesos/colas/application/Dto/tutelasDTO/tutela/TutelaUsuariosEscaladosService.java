package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaUsuarioEscalado;

import java.util.List;

public interface TutelaUsuariosEscaladosService {
    List<TutelaUsuarioEscalado> obtenerEscalamientosPorUsuario(String usuario);
    List<TutelaUsuarioEscalado> guardarListadoDeUsuariosEscalados(List<TutelaUsuarioEscalado> usuarioEscalados);
    TutelaUsuarioEscalado guardarUsuarioEscalado(TutelaUsuarioEscalado tutelaUsuarioEscalado);
    TutelaUsuarioEscalado obtenerTutelaUsuarioEscaladoPorId(Long id);
    List<TutelaUsuarioEscalado> obtenerUsuariosEscaladorPorIdTutela(Long id);
}
