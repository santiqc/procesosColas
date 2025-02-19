package com.procesos.colas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departamento")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddepartamento")
    private Long idDepartamento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "idpais")
    private Long idPais;

    @Column(name = "codigodivipola")
    private String codigoDivipola;
}
