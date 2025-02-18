package com.procesos.colas.application.services.tutelaAlfresco;


import com.procesos.colas.application.Dto.AlfrescoNode;
import com.procesos.colas.application.Dto.tutelaAlfresco.TutelaAlfrescoDTO;
import com.procesos.colas.application.Dto.tutelasDTO.ArchivosRadicacionDto;
import com.procesos.colas.application.services.alfresco.AlfrescoManagementNodePort;
import com.procesos.colas.domain.TutelaArchivo;
import com.procesos.colas.domain.alfesco.CreateNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class TutelaAlfrescoServiceImpl implements TutelaAlfrescoService {

    @Autowired
    private AlfrescoManagementNodePort alfrescoClient;

    @Value("${alfresco.almacentutelas}")
    private String almacenTutelas;

    @Value("${alfresco.prefijo}")
    private String prefijo;


    @Override
    public ArchivosRadicacionDto crearTutela(TutelaAlfrescoDTO data, MultipartFile radicado, List<MultipartFile> archivos) {

        AlfrescoNode responseFolder;


        CreateNode createNode = CreateNode.builder()
                .name(data.getRadicado())
                .nodeType("cm:folder")
                .build();

        AlfrescoNode responseTutela;
        List<TutelaArchivo> archivosTutela = new ArrayList<>();
        try {

            responseFolder = alfrescoClient.createNode(almacenTutelas, convertDTOToMap(createNode));

            MultiValueMap<String, Object> dataNodeType = new LinkedMultiValueMap<>();
            dataNodeType.add("name", data.getArchivoTutelaNombre());
            dataNodeType.add("nodeType", "cm:content");
            dataNodeType.add("filedata", new HttpEntity<>(radicado.getResource()));
            responseTutela = alfrescoClient.createNode(responseFolder.getEntry().getId(), dataNodeType);

            archivosTutela.add(TutelaArchivo.builder()
                            .nombre(responseTutela.getEntry().getName())
                            .tipo("De entrada")
                            .fechaCreacion(new Date())
                            .nodeId(responseTutela.getEntry().getId())
                    .build());
            // Agrega documento Radicado  //

            CreateNode createNodeTut = CreateNode.builder()
                    .name(responseTutela.getEntry().getName())
                    .nodeType(responseTutela.getEntry().getNodeType())
                    .build();

            /**Map<String, Object> properties = new HashMap<>();
            properties.put(prefijo+":tramite_tutela", data.getTramite());
            properties.put(prefijo+":codigo_proceso_tutela", data.getCodigoProceso());
            //Se adicionan 5 horas debido al cambio horario con alfresco
            properties.put(prefijo+":fecha_ingreso_tutela", data.getFechaIngresoCorreo().plusHours(5).toString());
            properties.put(prefijo+":fecha_radicado_tutela", data.getFechaRadicacion().plusHours(5).toString());
            properties.put(prefijo+":descripcion_tutela", data.getDescripcion());
            properties.put(prefijo+":telefono_tutela", data.getTelefono());
            properties.put(prefijo+":celular_tutela", data.getCelular());
            properties.put(prefijo+":correo_remitente_tutela", data.getCorreoRemitente());
            properties.put(prefijo+":nombre_juzgado", data.getNombreJuzgado());
            properties.put(prefijo+":direccion_juzgado", data.getDireccionJuzgado());
            properties.put(prefijo+":pais", data.getPais());
            properties.put(prefijo+":departamento", data.getDepartamento());
            properties.put(prefijo+":ciudad", data.getMunicipio());

            createNodeTut.setProperties(properties);**/

            alfrescoClient.actualizarNodo(responseTutela.getEntry().getId(), convertDTOToMap(createNodeTut));

            // Agrega documentos Radicado  //

            for (MultipartFile file : archivos) {

                CreateNode createNode2 = CreateNode.builder()
                        .name(file.getOriginalFilename())
                        .nodeType("cm:content")
                        .build();

                MultiValueMap<String, Object> dataMultiMap = new LinkedMultiValueMap<>();
                dataMultiMap.add("filedata", new HttpEntity<>(file.getResource()));
                dataMultiMap.addAll(convertDTOToMultiValueMap(createNode2));

                AlfrescoNode alfrescoNode = alfrescoClient.createNode(responseFolder.getEntry().getId(), dataMultiMap);
                archivosTutela.add(TutelaArchivo.builder()
                                .nombre(alfrescoNode.getEntry().getName())
                                .nodeId(alfrescoNode.getEntry().getId())
                                .fechaCreacion(new Date())
                                .tipo("De entrada")
                        .build());
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ArchivosRadicacionDto.builder()
                .nodeIdPrincipal(responseFolder.getEntry().getId())
                .archivosTutelas(archivosTutela)
                .build();
    }


}
