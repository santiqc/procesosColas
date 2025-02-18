package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaArchivo;

import java.util.List;

public interface TutelaArchivoService {

    TutelaArchivo obtenerArchivoPorId(Long id);
    TutelaArchivo guardarArchivo(TutelaArchivo tutelaArchivo);
    List<TutelaArchivo> guardarArchivos(List<TutelaArchivo> archivos);
    void eliminarTutela(Long id);
    List<TutelaArchivo> encontrarArchivoPorTutela(Long id);
    List<TutelaArchivo> encontrarArchivosPorTutelaYTipo(Long id, String tipo);

}
