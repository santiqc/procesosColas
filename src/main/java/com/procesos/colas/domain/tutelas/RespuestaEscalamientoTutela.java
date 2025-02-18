package com.procesos.colas.domain.tutelas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "respuesta_escalamiento_tutela")
public class RespuestaEscalamientoTutela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aprobacion_escalamiento_tutela_id", nullable = false)
    private AprobacionEscalamientoTutela aprobacionEscalamientoTutela;


    @Column(name = "respuestaDetalle", length = 200)
    private String respuestaDetalle;

}
