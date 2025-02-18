package com.procesos.colas.application.Dto.tutelaAlfresco;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutelaAlfrescoDTO {
    private String radicado;
    private String tramite;
    private String pais;
    private String departamento;
    private String municipio;
    private String nombreJuzgado;
    private String direccionJuzgado;
    private String telefono;
    private String celular;
    private String correoRemitente;
    private String codigoProceso;
    private LocalDateTime fechaIngresoCorreo;
    private LocalDateTime fechaRadicacion;
    private String archivoTutelaNombre;
    private String descripcion;

}
