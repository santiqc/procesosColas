package com.procesos.colas.application.services.alfresco;

import com.procesos.colas.application.Dto.AlfrescoNode;
import com.procesos.colas.application.Dto.AlfrescoResponseDTO;
import com.procesos.colas.application.Dto.EntryDTO;
import com.procesos.colas.application.services.alfresco.AlfrescoManagementNodePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.procesos.colas.application.Utils.UtilsHelper.convertDTOToMap;
import static com.procesos.colas.application.Utils.UtilsHelper.convertDTOToMultiValueMap;

@Service
public class AlfrescoRestTemplate implements AlfrescoManagementNodePort {

    @Value("${alfresco.url}")
    private String url;

    @Autowired
    @Qualifier("alfresco")
    private RestTemplate restTemplateCustom;

    //usar este para enviar archivo(s)
    @Override
    public AlfrescoNode createNode(String nodeId, MultiValueMap<String, Object> data) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Basic xY12ymQ7jnLrENWvBmc=");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data);
        ResponseEntity<AlfrescoNode> response = restTemplateCustom.exchange("/nodes/"+nodeId+"/children", HttpMethod.POST, requestEntity, AlfrescoNode.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return  response.getBody();
        } else {
            throw new RuntimeException("Error al cargar el documento en Alfresco. Código de respuesta: " + response.getBody());
        }
    }


    //usar este para crear carpetas o nodos
    @Override
    public AlfrescoNode createNode(String nodeId, Map<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data,headers);
        ResponseEntity<AlfrescoNode> response = restTemplateCustom.exchange("/nodes/"+nodeId+"/children", HttpMethod.POST, requestEntity, AlfrescoNode.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return  response.getBody();
        } else {
            throw new RuntimeException("Error al cargar el documento en Alfresco. Código de respuesta: " + response.getBody());
        }
    }

    @Override
    public void actualizarNodo(String nodeId, Map<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        restTemplateCustom.exchange("/nodes/" + nodeId,
                HttpMethod.PUT,
                request,
                String.class
        );
        String resultadoVer = "";
    }

    @Override
    public AlfrescoNode getNode(String nodeId) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
        var response = restTemplateCustom.exchange("/nodes/"+nodeId, HttpMethod.GET, null, AlfrescoNode.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }

    @Override
    public AlfrescoResponseDTO getListNodeChildren(String nodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        var response = restTemplateCustom.exchange("/nodes/"+nodeId+"/children", HttpMethod.GET, entity, AlfrescoResponseDTO.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }



    @Override
    public AlfrescoResponseDTO getAllPlantilla(String nodeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        var response = restTemplateCustom.exchange("/nodes/"+nodeId+"/children", HttpMethod.GET, entity, AlfrescoResponseDTO.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return null;
    }
    @Override
    public byte[] consultarDocumentoAlf(String nodeId){
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
        var response = restTemplateCustom.exchange("/nodes/"+nodeId+"/content?attachment=true", HttpMethod.GET, null,byte[].class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        }
        return null;
    }

    @Override
    public EntryDTO consultarDatosDocumentoAlf(String nodeId) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
        var response = restTemplateCustom.exchange("/nodes/"+nodeId, HttpMethod.GET, null,EntryDTO.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        }
        return null;
    }




    /*
    @Override
  //  public NodesResponse crearNodo(String id, CreateNode body) {
    public NodesResponse crearNodo(String id, MultiValueMap<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);




    }*/

    //desde
    /*@Override

    public NodesResponse crearNodo(String nodeId, MultiValueMap<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Basic YWRtaW46YWRtaW4=");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
        ResponseEntity<AlfrescoNode> response = restTemplateCustom.exchange("/nodes/"+nodeId+"/children", HttpMethod.POST, requestEntity, AlfrescoNode.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return  response.getBody();
        } else {
            throw new RuntimeException("Error al cargar el documento en Alfresco. Código de respuesta: " + response.getBody());
        }
    }
    */

    //hasta
}
