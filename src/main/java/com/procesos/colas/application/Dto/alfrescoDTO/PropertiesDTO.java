package com.procesos.colas.application.Dto.alfrescoDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Generated("jsonschema2pojo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesDTO {
    @JsonProperty("Doc:Nombre_documento")
    private String nombreDocumento;

    @JsonProperty("proltk:asunto")
    private String asunto;

    @JsonProperty("proltk:tipodocumental")
    private String tipologiaDocumental;

    @JsonProperty("proltk:fecha_original")
    private String fechaCreacion;

    @JsonProperty("proltk:tipoRadicado")
    private String tipoDocumento;
}
