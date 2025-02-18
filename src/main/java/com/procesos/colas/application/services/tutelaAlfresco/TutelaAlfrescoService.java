package com.procesos.colas.application.services.tutelaAlfresco;

import com.procesos.colas.application.Dto.tutelaAlfresco.TutelaAlfrescoDTO;
import com.procesos.colas.application.Dto.tutelasDTO.ArchivosRadicacionDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TutelaAlfrescoService {
    ArchivosRadicacionDto crearTutela(TutelaAlfrescoDTO data, MultipartFile radicado, List<MultipartFile> archivos);

}
