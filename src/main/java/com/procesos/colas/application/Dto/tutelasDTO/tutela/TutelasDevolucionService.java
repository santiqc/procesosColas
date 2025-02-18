package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.ActualizarMetodoEnvioDevolucionDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TutelasDevolucionService {
    boolean actualizarMetodoEnvioDevolucion(Long idTutela,ActualizarMetodoEnvioDevolucionDto actualizarMetodoEnvioDevolucionDto);
    boolean envioDeCorreoDevolucion(Long idtutela, List<MultipartFile> archivos);
}
