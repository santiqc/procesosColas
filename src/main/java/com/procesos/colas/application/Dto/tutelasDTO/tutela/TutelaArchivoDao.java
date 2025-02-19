package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.procesos.colas.domain.tutelas.TutelaArchivo;


import java.util.List;

public interface TutelaArchivoDao {
    List<TutelaArchivo> guardarArchivos(List<TutelaArchivo> archivos);
}
