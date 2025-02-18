package com.procesos.colas.domain.tutelas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tutela_siniestro")
public class TutelaSiniestro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;
    @Column(name = "nroSiniestro", length = 500, nullable = false)
    private String nroSiniestro;

}
