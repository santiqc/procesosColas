package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "versiontrd")
public class VersionesTRD {

    @Id
    @Column(name="idversiontrd")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 10)
    private String codigo;

    @Column(name = "nombreversion",  length = 50)
    private String nombreVersion;

    @Column(name = "fechainicio")
    private Date fechaInicio;

    @Column(name = "fechafin")
    private Date fechaFin;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "fechaversion")
    private Date fechaVersion;

    @Column(name = "fondoid")
    private Integer id_fondo;


}
