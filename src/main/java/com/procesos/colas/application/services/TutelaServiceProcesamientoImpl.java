package com.procesos.colas.application.services;



import com.procesos.colas.Infrastructure.repository.TutelasRepository;
import com.procesos.colas.application.Dto.TutelaMessage;
import com.procesos.colas.application.Dto.tutelaAlfresco.TutelaAlfrescoDTO;
import com.procesos.colas.application.Dto.tutelasDTO.ArchivosRadicacionDto;
import com.procesos.colas.application.Dto.tutelasDTO.TutelaResponseDto;
import com.procesos.colas.application.Dto.tutelasDTO.tutela.TutelaArchivoService;
import com.procesos.colas.application.Utils.MultipartFileConverter;
import com.procesos.colas.application.services.tutelaAlfresco.TutelaAlfrescoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Slf4j
@Service
public class TutelaServiceProcesamientoImpl {

    @Autowired
    private TutelaAlfrescoService tutelaAlfrescoService;
    @Autowired
    public TutelasRepository tutelasRepository;


    private final TutelaArchivoService tutelaArchivoService;

    public TutelaServiceProcesamientoImpl(TutelaArchivoService tutelaArchivoService) {
        this.tutelaArchivoService = tutelaArchivoService;
    }


    public TutelaResponseDto procesarTutelaQueue(TutelaMessage message)
            throws Exception {
        try{

            MultipartFile radicadoMultipart = MultipartFileConverter.convertToMultipartFile(
                    message.getRadicadoFile(), message.getRadicadoNombre()
            );

            List<MultipartFile> archivosMultipart = MultipartFileConverter.convertToMultipartFiles(
                    message.getArchivos(), message.getArchivosNombres()
            );

            TutelaAlfrescoDTO data = TutelaAlfrescoDTO.builder()
                    .archivoTutelaNombre(message.getArchivoTutelaNombre())
                    .radicado(message.getRadicado()).build();

            ArchivosRadicacionDto response = tutelaAlfrescoService.crearTutela(
                    data, radicadoMultipart, archivosMultipart);


            for(int i=0;i<response.getArchivosTutelas().size();i++){
                response.getArchivosTutelas().get(i).setTutelaId(message.getIdTutela());
                response.getArchivosTutelas().get(i).setUsuario(message.getUsuario());
            }
            tutelaArchivoService.guardarArchivos(response.getArchivosTutelas());

            int filasActualizadas = tutelasRepository.actualizarNodeId(message.getIdTutela(), response.getNodeIdPrincipal());

            if (filasActualizadas > 0) {
                log.info("NodeId actualizado correctamente para la tutela con ID: {}", message.getIdTutela());
            } else {
                log.warn("No se encontró la tutela con ID: {}", message.getIdTutela());
            }

            return new TutelaResponseDto();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new RuntimeException("Ha ocurrido un error en el sistema::"+e);
        }
    }


}
