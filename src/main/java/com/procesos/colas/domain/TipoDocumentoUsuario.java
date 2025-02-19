package com.procesos.colas.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tipo_documento_usuario")
public class TipoDocumentoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;
}