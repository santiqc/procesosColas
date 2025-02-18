package com.procesos.colas.application.Dto.alfrescoDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "nodeType",
        "properties"
})
@Generated("jsonschema2pojo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlfrescoRequestCreateExpedient {

    @JsonProperty("name")
    public String name;
    @JsonProperty("nodeType")
    public String nodeType;
    @JsonProperty("properties")
    public Properties properties;

}