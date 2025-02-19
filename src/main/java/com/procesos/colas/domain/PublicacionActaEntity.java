package com.procesos.colas.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "publicacion_acta")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicacionActaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "node_id")
    private String nodeId;
    @Column(name = "numero_acta")
    private String numeroActa;
    @Column(name = "fecha_acta")
    private LocalDateTime fechaActa;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "proceso_id")
    private Long procesoId;

}
