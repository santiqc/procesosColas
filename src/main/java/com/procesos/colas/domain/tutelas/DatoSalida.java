package com.procesos.colas.domain.tutelas;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tutela_datos_salida")
public class DatoSalida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "forma_envio")
    private String formaEnvio;
    private String anexos;
    private String asunto;
    private String observacion;
}