package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.TutelaDocumentoSalidaDto;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaDocumentoSalidaEntity;

public interface TutelaDocumentoSalidaService {

    TutelaDocumentoSalidaEntity crearDocumentoSalida(TutelaDocumentoSalidaDto documentoSalidaDto);
    TutelaDocumentoSalidaEntity actualizarDocumentoSalida(TutelaDocumentoSalidaEntity tutelaDocumentoSalidaEntity);
    void eliminarDocumentoSalida(Long id);
    TutelaDocumentoSalidaEntity obtenerDocumentoSalidaPorId(Long id);
    TutelaDocumentoSalidaEntity obtenerDocumentoSalidPorTutelaId(Long tutelaId);


}
