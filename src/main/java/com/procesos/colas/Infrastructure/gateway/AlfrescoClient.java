package com.procesos.colas.Infrastructure.gateway;



import com.procesos.colas.application.Dto.AlfrescoResponseDTO;
import com.procesos.colas.application.Dto.alfrescoDTO.AlfrescoRequestCreateExpedient;
import com.procesos.colas.application.Dto.alfrescoDTO.AlfrescoResponse;
import com.procesos.colas.application.Dto.alfrescoDTO.EntradaAlfresco;
import com.procesos.colas.domain.alfesco.CreateNode;
import com.procesos.colas.domain.alfesco.NodesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "alfrescohost", url = "${alfresco.host}", configuration = FeignClientConfiguration.class)
public interface AlfrescoClient {
    @GetMapping(path = "/alfresco/versions/1/nodes/{id}")
    public Object obtenerContenidoDeNodo(@PathVariable(name = "id") String id);

    @PostMapping(path = "/alfresco/versions/1/nodes/{node_id_subserie}/children")
    public AlfrescoResponse crearExpediente(@PathVariable(name = "node_id_subserie") String id_node, @RequestBody AlfrescoRequestCreateExpedient request);

    @PostMapping(path = "/alfresco/versions/1/nodes/{id}/children", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NodesResponse crearNodo(@PathVariable(name = "id") String id, @RequestBody String body, @RequestPart(name = "filedata") MultipartFile file);

    @PostMapping(path = "/alfresco/versions/1/nodes/{id}/children")
    public NodesResponse crearNodo(@PathVariable(name = "id") String id, @RequestBody CreateNode body);
    @PostMapping(path = "/alfresco/versions/1/nodes/{id}/children")
    public Object crearNodoA(@PathVariable(name = "id") String id, @RequestBody CreateNode body);

    //inicio de consumos de juan diego mora
    @PostMapping(path = "/search/versions/1/search")
    public Object buscarDocumento(@RequestBody Object peticion);

    @GetMapping(path = "/alfresco/versions/1/nodes/{id}/content?attachment=true")
    public String consultarDocumento(@PathVariable(name = "id") String id);
    //este endpoint de alfresco devuelve base 64 si se agrega al final /content?attachment=true

    //fin de consumos de juan diego mora

    // consumo Mauro
    @PostMapping(value = "/alfresco/versions/1/nodes/{id}/children",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NodesResponse crearPlantilla(@PathVariable (name = "id")String id, @RequestPart String json, @RequestPart(name = "filedata") MultipartFile file );

    @PostMapping(value = "/alfresco/versions/1/nodes/{id}/children",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object crearPlantillaB(@PathVariable (name = "id")String id, @RequestPart String json, @RequestPart(name = "filedata") MultipartFile file );

    @DeleteMapping(path = "/alfresco/versions/1/nodes/{id}")
    public Boolean eliminarPlantilla(@PathVariable(name = "id")String id);

    @GetMapping("/alfresco/versions/1/nodes/{id}/children?include=properties")
    public AlfrescoResponseDTO getAllPlantilla(@PathVariable(name = "id")String id);
    // fin consumo Mauro

    // Consumos John Rueda
    @PostMapping(value = "/alfresco/versions/1/nodes/{id}/children", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NodesResponse crearNodoNodeType(
            @PathVariable (name = "id")String id,
            @RequestPart("filedata") MultipartFile filedata,
            @RequestPart("nodeType") String nodeType,
            @RequestPart("name") String name
    );

    @PutMapping(path = "/alfresco/versions/1/nodes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NodesResponse actualizarNodo(@PathVariable(name = "id") String id, @RequestBody CreateNode body);

    // Fin Consumos John Rueda
    @PostMapping(path = "/alfresco/versions/1/nodes/{id}/children",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object radicadoEntrada(@RequestBody EntradaAlfresco entradaAlfresco, @RequestPart(name = "filedata") MultipartFile file, @PathVariable(name = "id") String id);

    @GetMapping(path = "/alfresco/versions/1/nodes/{id}/content?attachment=true")
    @ResponseBody
    public byte[] consultarDocumentoByte(@PathVariable(name = "id") String id);

    @GetMapping(path = "/alfresco/versions/1/nodes/{id}")
    @ResponseBody
    public Object obtenerContentType(@PathVariable(name = "id") String id);

    @PostMapping(path = "/search/versions/1/search")
    public Object buscarNodeIdPorNombreCarpeta(@RequestBody Object peticion);


    @GetMapping(path = "/alfresco/versions/1/nodes/{id}/content?attachment=true", produces = "application/octet-stream")
    public byte[] consultarDocumentos(@PathVariable(name = "id") String id);

    @GetMapping("/alfresco/versions/1/nodes/{parentId}/children")
    public AlfrescoResponseDTO obtenerNodoPorNombre(@PathVariable(name = "parentId") String parentId,
                                                    @RequestParam(name = "name") String nombre);

}
