package com.procesos.colas.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;


@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tutelas_archivos")
public class TutelaArchivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(name = "arch_extension")
    private String extension;
    private String tipo;
    private String usuario;
    private String username;
    @Column(name = "tutel_id")
    private Long tutelaId;
    @Column(name = "node_id")
    private String nodeId;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "es_devolucion")
    private Boolean es_devolucion;

}
