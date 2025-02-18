package com.procesos.colas.domain.tutelas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "etapa_procesal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EtapaProcesal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

}
