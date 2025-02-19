package com.procesos.colas.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oficina")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OficinaModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_dependencia")
    private SeccionSubSeccion dependencia;
    @Column(nullable = false)
    private Boolean estado = true; // Valor por defecto
}
