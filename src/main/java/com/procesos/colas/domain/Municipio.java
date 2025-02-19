package com.procesos.colas.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "municipio")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmunicipio")
    private Long idMunicipio;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "iddepartamento")
    private Long idDepartamento;

    @Column(name = "codigodivipola")
    private String codigoDivipola;

}
