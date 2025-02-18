package com.procesos.colas.domain.tutelas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "aprobacion_escalamiento_tutela")
public class AprobacionEscalamientoTutela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutela_id", nullable = false)
    private Tutela tutela;

}
