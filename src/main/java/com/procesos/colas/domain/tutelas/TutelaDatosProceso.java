package com.procesos.colas.domain.tutelas;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tutela_datos_proceso")
public class TutelaDatosProceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String numerosiniestro;
    @Column
    private String dependencias;
    @Column
    private String causales;

    @Column(name = "observaciones", length = 5000)
    private String observaciones;

    @Column(name = "tipodocumentoaccionante")
    private String tipoDocumentoAccionante;

    @Column(name = "numeroDocumentoAccionante", length = 20, nullable = false)
    private String numeroDocumentoAccionante;

    @Column(name = "primerNombreAccionante", length = 100, nullable = false)
    private String primerNombreAccionante;

    @Column(name = "segundoNombreAccionante", length = 100)
    private String segundoNombreAccionante;

    @Column(name = "primerApellidoAccionante", length = 100, nullable = false)
    private String primerApellidoAccionante;

    @Column(name = "segundoApellidoAccionante", length = 100)
    private String segundoApellidoAccionante;

    @Column(name = "tipodocumentoafectado")
    private String tipoDocumentoAfectado;

    @Column(name = "numeroDocumentoAfetcado", length = 20, nullable = false)
    private String numeroDocumentoAfectado;

    @Column(name = "primerNombreAfetcado", length = 100, nullable = false)
    private String primerNombreAfectado;

    @Column(name = "segundoNombreAfetcado", length = 100)
    private String segundoNombreAfectado;

    @Column(name = "primerApellidoAfetcado", length = 100, nullable = false)
    private String primerApellidoAfectado;

    @Column(name = "segundoApellidoAfetcado", length = 100)
    private String segundoApellidoAfectado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechaRespuesta")
    private Date fechaRespuesta;

    @Formula("concat_ws(' ', primerNombreAccionante, segundoNombreAccionante, primerApellidoAccionante, segundoApellidoAccionante)")
    private String nombreCompletoAccionante;

    @Formula("concat_ws(' ', primerNombreAfetcado, segundoNombreAfetcado, primerApellidoAfetcado, segundoApellidoAfetcado)")
    private String nombreCompletoAfectado;

    @PrePersist
    public void prePersist() {
        if (segundoNombreAfectado == null) {
            segundoNombreAfectado = "";
        }
        if (segundoApellidoAfectado == null) {
            segundoApellidoAfectado = "";
        }
        if (segundoNombreAccionante == null) {
            segundoNombreAccionante = "";
        }
        if (segundoApellidoAccionante == null) {
            segundoApellidoAccionante = "";
        }
    }

}
