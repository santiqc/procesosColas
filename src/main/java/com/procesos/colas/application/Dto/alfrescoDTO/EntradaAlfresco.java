package com.procesos.colas.application.Dto.alfrescoDTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class EntradaAlfresco {
    private String name;
    private String nodeType;
    Properties Properties;
}
