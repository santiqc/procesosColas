package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.domain.exception.CustomHttpException;
import com.prolinktic.sgdea.infrastructure.in.dto.correspondencia.CorrespondenciaGuardarDocumentoSalidaDTO;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaArchivo;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaDocumentoSalidaEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TutelaArchivosService {

    List<TutelaArchivo> subirArchivo(Long id, Long usuarioId, MultipartFile[] files, boolean es_devolucion);
    Map<String, List<?>> subirArchivoConTipo(Long id, Long usuarioId, String tipo, MultipartFile[] files, boolean es_devolucion);
    TutelaArchivo actualizarArchivo(TutelaArchivo tutelaArchivo);
    TutelaDocumentoSalidaEntity tutelaGuardarDocumentoSalida(CorrespondenciaGuardarDocumentoSalidaDTO documentoSalidaDTO) throws CustomHttpException;
    TutelaDocumentoSalidaEntity tutelaActualizarDocumentoSalida(CorrespondenciaGuardarDocumentoSalidaDTO documentoSalidaDTO) throws CustomHttpException;
    List<TutelaDocumentoSalidaEntity> tutelaListaDocumentosSalida(Long tutelaId) throws CustomHttpException;
    TutelaDocumentoSalidaEntity tutelaBuscarDocumentosSalida(int idDocumentoSalida) throws CustomHttpException;
    TutelaDocumentoSalidaEntity documentoSalidaFinalStatus(int idDocumentoSalida, boolean es_final) throws CustomHttpException;
    TutelaDocumentoSalidaEntity documentoSalidaActual(Long tutelaId);
}
