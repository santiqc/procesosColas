package com.procesos.colas.application.Dto.tutelaAlfresco;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutelaAlfrescoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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
