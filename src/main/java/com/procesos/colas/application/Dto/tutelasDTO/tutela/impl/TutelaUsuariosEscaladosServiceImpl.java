package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaUsuariosEscaladosService;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.TutelasUsuariosEscaladosDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaUsuarioEscalado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class TutelaUsuariosEscaladosServiceImpl implements TutelaUsuariosEscaladosService {

    private final TutelasUsuariosEscaladosDao usuariosEscaladosDao;

    public TutelaUsuariosEscaladosServiceImpl(TutelasUsuariosEscaladosDao usuariosEscaladosDao) {
        this.usuariosEscaladosDao = usuariosEscaladosDao;
    }

    @Override
    public List<TutelaUsuarioEscalado> obtenerEscalamientosPorUsuario(String usuario) {
        return usuariosEscaladosDao.obtenerEscalamientosPorUsuario(usuario);
    }

    @Override
    public List<TutelaUsuarioEscalado> guardarListadoDeUsuariosEscalados(List<TutelaUsuarioEscalado> usuarioEscalados) {
        return usuariosEscaladosDao.guardarListadoDeUsuariosEscalados(usuarioEscalados);
    }

    @Override
    public TutelaUsuarioEscalado guardarUsuarioEscalado(TutelaUsuarioEscalado tutelaUsuarioEscalado) {
        return usuariosEscaladosDao.guardarUsuarioEscalado(tutelaUsuarioEscalado);
    }

    @Override
    public TutelaUsuarioEscalado obtenerTutelaUsuarioEscaladoPorId(Long id) {
        return usuariosEscaladosDao.obtenerTutelaUsuarioEscaladoPorId(id);
    }

    @Override
    public List<TutelaUsuarioEscalado> obtenerUsuariosEscaladorPorIdTutela(Long id) {
        return usuariosEscaladosDao.obtenerUsuariosEscaladorPorIdTutela(id);
    }

}
