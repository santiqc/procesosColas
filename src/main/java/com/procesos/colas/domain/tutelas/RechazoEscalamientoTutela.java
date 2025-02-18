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
@Table(name = "tutela_escalamiento_rechazo")
public class RechazoEscalamientoTutela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "observaciones", length = 200)
    private String motivoRechazo;
    @Column(name = "escalamiento_id")
    private Long escalamientoId;
    @Column(name = "usuario")
    private String usuario;

}
