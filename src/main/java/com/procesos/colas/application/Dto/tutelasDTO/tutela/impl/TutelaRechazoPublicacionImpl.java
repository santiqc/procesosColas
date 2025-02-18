package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaRechazoPublicacion;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.PeticionPublicacionDto;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TutelaRechazoPublicacionImpl implements TutelaRechazoPublicacion {

    private final TutelaDao tutelaDao;

    public TutelaRechazoPublicacionImpl(TutelaDao tutelaDao) {
        this.tutelaDao = tutelaDao;
    }

    @Override
    public boolean rechazoPublicacion(Long tutelaId, PeticionPublicacionDto data) {
        try {
            Tutela tutela = tutelaDao.obtenerTutelaPorId(tutelaId);
            tutela.setMotivoRechazoAprobacionPublicacion(data.getMotivo());
            tutela.setIdUsuarioPublicador(null);
            tutela.setEstado("Publicacion rechazada");
            tutelaDao.guardarTutela(tutela);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean publicacion(Long tutelaId, PeticionPublicacionDto data) {
        try {
            Tutela tutela = tutelaDao.obtenerTutelaPorId(tutelaId);
            tutela.setMotivoRechazoAprobacionPublicacion(data.getMotivo());
            tutela.setIdUsuarioPublicador(null);
            tutela.setEstado("Publicado");
            tutela.setFechaPublicacion(LocalDateTime.now());
            tutelaDao.guardarTutela(tutela);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean cerrar(Long tutelaId, PeticionPublicacionDto data) {
        try {
            Tutela tutela = tutelaDao.obtenerTutelaPorId(tutelaId);
            tutela.setMotivoRechazoAprobacionPublicacion(data.getMotivo());
            tutela.setIdUsuarioPublicador(null);
            tutela.setEstado("Publicado cerrada");
            tutelaDao.guardarTutela(tutela);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
