package com.procesos.colas.application.services.alfresco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procesos.colas.Infrastructure.gateway.AlfrescoClient;
import com.procesos.colas.application.Dto.Entry;
import com.procesos.colas.domain.alfesco.CreateNode;
import com.procesos.colas.domain.alfesco.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.prolinktic.sgdea.common.util.UtilsHelper.convertDTOToMultiValueMap;

@Service
@RequiredArgsConstructor
public class AlfrescoServices implements AlfrescoUseCase {

    private final AlfrescoManagementNodePort managementNodePort;
    private final AlfrescoClient alfrescoClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<UploadFile> uploadFiles(String nodeId, MultipartFile[] files) {
        List<UploadFile> ans = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                CreateNode createNode = CreateNode.builder()
                        .name(file.getOriginalFilename())
                        .nodeType("cm:content")
                        .build();

                MultiValueMap<String, Object> dataMultiMap = new LinkedMultiValueMap<>();
                dataMultiMap.add("filedata", new HttpEntity<>(file.getResource()));
                dataMultiMap.addAll(convertDTOToMultiValueMap(createNode));
                Entry node = managementNodePort.createNode(nodeId, dataMultiMap).getEntry();
                ans.add(UploadFile.builder()
                        .id(node.getId())
                        .createdAt(node.getCreatedAt())
                        .parentId(node.getParentId())
                        .name(node.getName())
                        .status("CREATED").build());
            }catch (RuntimeException e){
                ans.add(UploadFile.builder()
                        .status("FAIL")
                        .name(file.getOriginalFilename())
                        .message(e.getMessage()).build());
            }

        }
        return ans;
    }

    @Override
    public Boolean deleteNode(String nodeId) {
        Boolean ans = Boolean.TRUE;
        try {
            alfrescoClient.eliminarPlantilla(nodeId);
        }catch (Exception e){
            ans = Boolean.FALSE;
        }

        return ans;
    }

    @Override
    public Boolean deleteAlfrescoNode(String nodeId) throws CustomHttpException {
        Boolean ans = Boolean.TRUE;
        try {
            alfrescoClient.eliminarPlantilla(nodeId);
        }catch (Exception e){
            throw new CustomHttpException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    @Override
    public String createNode(String sec, String folderId) throws MedicinaLaboralException {
        CreateNode node = CreateNode.builder()
            .name(sec)
            .nodeType("cm:folder")
            .build();

        Object respFolder = alfrescoClient.crearNodoA(folderId, node);
        NodesResponse2 nodesResponse = objectMapper.convertValue(((LinkedHashMap) respFolder).get("entry"), NodesResponse2.class);

        if (nodesResponse == null || nodesResponse.getId() == null) {
            throw new MedicinaLaboralException("No se pudo crear la carpeta base en Alfresco", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return nodesResponse.getId();
    }

    @Override
    public MultipartFile getDocument(String nodeId) {
        var info = managementNodePort.consultarDatosDocumentoAlf(nodeId);
        var doc = managementNodePort.consultarDocumentoAlf(nodeId);

        return new CustomMultipartFile(info.getEntry().getName(),info.getEntry().getName(),info.getEntry().getContent().getMimeType(),doc);
    }

    @Override
    public NodesResponse changeNameDocument(String nodeid, String newName) {

        return alfrescoClient.actualizarNodo(nodeid, CreateNode.builder()
                .name(newName).build());
    }
}
