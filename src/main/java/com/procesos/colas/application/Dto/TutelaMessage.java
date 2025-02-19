package com.procesos.colas.application.Dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonTypeName("tutela")
public class TutelaMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("idTutela")
    private Long idTutela;

    @JsonProperty("usuario")
    private String usuario;

    @JsonProperty("radicado")
    private String radicado;

    @JsonProperty("radicadoFile")
    private byte[] radicadoFile;

    @JsonProperty("radicadoNombre")
    private String radicadoNombre;

    @JsonProperty("archivos")
    private List<byte[]> archivos;

    @JsonProperty("archivosNombres")
    private List<String> archivosNombres;

    @JsonProperty("archivoTutelaNombre")
    private String archivoTutelaNombre;
}