package com.procesos.colas.domain.tutelas;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prolinktic.sgdea.domain.model.SeccionSubSeccion.SeccionSubSeccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "causal_dependencia")
public class CausalDependencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "dependencia_id", referencedColumnName = "idseccionsubseccion")
    private SeccionSubSeccion dependencia;
}
