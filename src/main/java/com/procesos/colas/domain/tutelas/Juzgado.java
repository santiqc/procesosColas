package com.procesos.colas.domain.tutelas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "juzgados")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Juzgado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_juzgado")
    private Long idJuzgado;

    @Column(unique = true)
    private String nombre;

    private String direccion;

    private String pais;

    private String departamento;

    private String municipio;

    @Column(unique = true)
    private String codigo;

    private String telefono;

    private String celular;

    private String correo;

    private Boolean estado;

}
