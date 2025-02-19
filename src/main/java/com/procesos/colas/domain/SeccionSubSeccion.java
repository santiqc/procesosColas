package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "seccionsubseccion")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeccionSubSeccion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idseccionsubseccion")
    private int idSeccionSubSeccion;

    @ManyToOne
    @JoinColumn(name = "idversiontrd")
    private VersionesTRD idversiontrd;

    @Column(name = "nombre")
    private String nombre;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idpadre")
    private SeccionSubSeccion padre;

    // @ManyToOne
    // @JoinColumn(name = "idfondo")
    // private Fondo fondo;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "observaciones")
    private String observacion;

    @Column(name = "idnodealfresco")
    private String idNodeAlfresco;

    @Column(name = "idalfresco")
    private String idAlfresco;

    @ManyToOne
    @JoinColumn(name = "consolidadorAsignado")
    private ConsolidadorBase consolidadorAsignado;

}
