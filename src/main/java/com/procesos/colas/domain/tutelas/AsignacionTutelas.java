package com.procesos.colas.domain.tutelas;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
@Builder
@Table(name = "asignacion_tutelas")
public class AsignacionTutelas {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asignacion_id_asignacion_seq")
    @SequenceGenerator(name = "asignacion_id_asignacion_seq", sequenceName = "asignacion_id_asignacion_seq", allocationSize = 1)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;
    @Column
    private Boolean asignado;
    @Column(name = "usuario_asignado")
    private String usuarioAsignado;
    @Column(name = "motivo_asignacion")
    private String motivoAsignacion;
    @Column(name = "motivo_reasignacion")
    private String motivoReAsignacion;
    @Column
    private String etapa;
    @Column(name = "fecha_asignacion")
    private Date fechaAsignacion;
    @JsonBackReference
    @ManyToMany(mappedBy = "asignaciones")
    private Set<Tutela> tutelas;

    @PrePersist
    protected void onCreate() {
        fechaAsignacion = new Date();
    }

    @Column(name = "notificada")
    private Boolean notificada;
}
