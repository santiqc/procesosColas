package com.procesos.colas.application.services.alfresco;

import com.procesos.colas.application.Dto.AlfrescoNode;
import com.procesos.colas.application.Dto.AlfrescoResponseDTO;
import com.procesos.colas.application.Dto.EntryDTO;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface AlfrescoManagementNodePort {

    //inicio observaciones createNode
    //existe una sobre carga, usar este para enviar archivos
    public AlfrescoNode createNode(String nodeId, MultiValueMap<String, Object> data);

    AlfrescoNode createNode(String nodeId, Map<String, Object> data);
    //usar este para crear carpetas o nodos
    //fin observaciones createNode


    public void actualizarNodo(String nodeId, Map<String, Object> data);

    public AlfrescoNode getNode(String nodeId);

    AlfrescoResponseDTO getListNodeChildren(String nodeId);


    AlfrescoResponseDTO getAllPlantilla(String id);

    byte[] consultarDocumentoAlf(String nodeId);

    EntryDTO consultarDatosDocumentoAlf(String nodeId);


}
