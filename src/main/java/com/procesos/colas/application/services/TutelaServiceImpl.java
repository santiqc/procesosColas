package com.procesos.colas.application.services;


import com.procesos.colas.Infrastructure.gateway.AlfrescoClient;
import com.procesos.colas.application.Dto.tutelaAlfresco.TutelaAlfrescoDTO;
import com.procesos.colas.application.Dto.tutelasDTO.ArchivosRadicacionDto;
import com.procesos.colas.application.Dto.tutelasDTO.TutelaRequestDto;
import com.procesos.colas.application.Dto.tutelasDTO.TutelaResponseDto;
import com.procesos.colas.application.services.tutelaAlfresco.TutelaAlfrescoService;
import com.procesos.colas.domain.tutelas.Tutela;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TutelaServiceImpl {

    @Autowired
    private TutelaAlfrescoService tutelaAlfrescoService;

    @Autowired
    AlfrescoClient alfrescoCliente;


    public TutelaResponseDto procesarTutelaQueue(TutelaRequestDto tutelaRequestDto)
            throws Exception {
        try{

            TutelaAlfrescoDTO tutelaAlfrescoDTO = new TutelaAlfrescoDTO(
                    requestTutela.getIdRadicado(), TIPO_TRAMITE, juzgado.getPais(), juzgado.getDepartamento(),
                    juzgado.getMunicipio(), juzgado.getNombre(), juzgado.getDireccion(),
                    juzgado.getTelefono(), juzgado.getCelular(), juzgado.getCorreo(),
                    requestTutela.getCodigoProceso(), fechaCorreo, fechaRadicacion, arcSopo.getOriginalFilename(),
                    requestTutela.getDescripcion());

            List<MultipartFile> archivos = new ArrayList<>();
            if (Objects.nonNull(tutelaRequestDto.getArchivos())) {
                archivos = tutelaRequestDto.getArchivos().stream()
                        .filter(a -> !a.isEmpty())
                        .collect(Collectors.toList());
            }

            ArchivosRadicacionDto response = tutelaAlfrescoService.crearTutela(
                    tutelaAlfrescoDTO, arcSopo, archivos);
            requestTutela.setNodeId(response.getNodeIdPrincipal());


            // se registra en base de datos del ms_sgdea
            Tutela tutela = tutelasServices.CrearTutela(tutelaRequestDto);
            for(int i=0;i<response.getArchivosTutelas().size();i++){
                response.getArchivosTutelas().get(i).setTutelaId(tutela.getIdTutela());
                response.getArchivosTutelas().get(i).setUsuario(requestTutela.getUsuario());
            }
            tutelaArchivoService.guardarArchivos(response.getArchivosTutelas());

            return new TutelaResponseDto(requestTutela.getIdRadicado());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new RuntimeException("Ha ocurrido un error en el sistema::"+e);
        }
    }


}
