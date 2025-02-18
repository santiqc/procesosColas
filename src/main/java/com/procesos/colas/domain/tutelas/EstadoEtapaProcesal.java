package com.procesos.colas.domain.tutelas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "estado_etapa_procesal")
public class EstadoEtapaProcesal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "etapa_procesal_id", nullable = false)
    private EtapaProcesal etapaProcesal;
}
