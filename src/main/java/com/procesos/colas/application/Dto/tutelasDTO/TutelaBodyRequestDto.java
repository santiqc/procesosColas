package com.procesos.colas.application.Dto.tutelasDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TutelaBodyRequestDto {

    @NotEmpty(message = "El campo canal de recepción es obligatorio")
    private String canal;


    @NotEmpty(message = "El campo código del proceso es obligatorio")
    private String codigoProceso;

    @NotEmpty(message = "El campo fecha de ingreso de correo es obligatorio")
    private String fecha;

    private String hora;

    @NotEmpty(message = "El campo descripción es obligatorio")
    private String descripcion;

    private String idRadicado;

    private String nodeId;

    private String pais;

    private String departamento;

    private String municipio;
    private LocalDateTime fechaRadicacion;
    private String usuario;
    @Nullable()
    private Long tutelaId;
    private String estado;

    private Boolean radicacionAutomatica = false;
}
