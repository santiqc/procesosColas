package com.procesos.colas.application;


import com.procesos.colas.application.Dto.ArchivoDto;

import java.util.List;

public interface ProcesamientoService {
    List<ArchivoDto> buscarArchivosPorIdHistory(Long idHistory);
}
