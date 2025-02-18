package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaDocumentoSalidaService;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.TutelaDocumentoSalidaDto;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.TutelaDocumentoSalidaDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaDocumentoSalidaEntity;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.tutela.TutelasRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Service
public class TutelaDocumentoSalidaServiceImpl implements TutelaDocumentoSalidaService {

    private final TutelaDocumentoSalidaDao dao;
    private final TutelaDao tutelaDao;
    private final TutelasRepository tutelasRepository;

    public TutelaDocumentoSalidaServiceImpl(TutelaDocumentoSalidaDao dao, TutelaDao tutelaDao, TutelasRepository tutelasRepository) {
        this.dao = dao;
        this.tutelaDao = tutelaDao;
        this.tutelasRepository = tutelasRepository;
    }

    @Override
    public TutelaDocumentoSalidaEntity crearDocumentoSalida(TutelaDocumentoSalidaDto documentoSalidaDto) {
        if (validacionDocumentoSalida(documentoSalidaDto)) {
            throw new IllegalArgumentException("La información enviada no es valida.");
        }

        if (!tutelaDao.existeTutelaPorId(documentoSalidaDto.getTutelaId())) {
            throw new NullPointerException("No existe la tutela.");
        }

        TutelaDocumentoSalidaEntity documentoSalida = TutelaDocumentoSalidaEntity.builder()
                .contenido(documentoSalidaDto.getContenido())
                .usuario(documentoSalidaDto.getUsuario())
                .tutelaId(documentoSalidaDto.getTutelaId())
                .fechaCreacion(new Date())
                .build();
        documentoSalida = dao.crearDocumentoSalida(documentoSalida);

        Tutela tutela = tutelasRepository.findTutelaById(documentoSalidaDto.getTutelaId()).orElseThrow(() -> new IllegalArgumentException("No se encontro tutela"));
        tutela.setEstado("En edición");
        tutelaDao.guardarTutela(tutela);
        return documentoSalida;
    }

    @Override
    public TutelaDocumentoSalidaEntity actualizarDocumentoSalida(TutelaDocumentoSalidaEntity tutelaDocumentoSalidaEntity) {
        tutelaDocumentoSalidaEntity.setFechaActualizacion(LocalDateTime.now());
        return dao.actualizarDocumentoSalida(tutelaDocumentoSalidaEntity);
    }

    @Override
    public void eliminarDocumentoSalida(Long id) {
        dao.eliminarDocumentoSalida(id);
    }

    @Override
    public TutelaDocumentoSalidaEntity obtenerDocumentoSalidaPorId(Long id) {
        return dao.obtenerDocumentoSalidaPorId(id);
    }

    @Override
    public TutelaDocumentoSalidaEntity obtenerDocumentoSalidPorTutelaId(Long tutelaId) {
        TutelaDocumentoSalidaEntity tutelaDocumentoSalida = dao.obtenerDocumentoSalidPorTutelaId(tutelaId);
        if( tutelaDocumentoSalida != null) {
            tutelaDocumentoSalida.setContenido(contentEnabled(tutelaDocumentoSalida.getContenido()));
        }
        return tutelaDocumentoSalida;
    }

    private boolean validacionDocumentoSalida(TutelaDocumentoSalidaDto dto) {
        return Objects.isNull(dto.getTutelaId()) && Objects.isNull(dto.getUsuario() == null);
    }

    private String contentEnabled(String content) {
        return content.replaceAll("contenteditable=\"false\"", "contenteditable=\"true\"");
    }

}
