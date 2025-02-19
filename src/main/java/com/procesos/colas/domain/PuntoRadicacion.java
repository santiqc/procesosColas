package com.procesos.colas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Table(name = "punto_radicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntoRadicacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "oficina_id")
  private OficinaModel oficinaId;

  @Column(name = "nombre_punto", length = 100, nullable = false)
  private String nombrePunto;

  @Column(name = "pais", length = 50, nullable = true, columnDefinition = "VARCHAR(50) DEFAULT 'COLOMBIA'")
  private String pais;

  private String departamento;

  private String municipio;

  private boolean activo;

  @Column(name = "cod_centro_costo", nullable = false, length = 10)
  private BigInteger codCentroCosto;

  @Column(name = "punto_radicacion", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean puntoRadicacion;

  @ManyToOne
  @JoinColumn(name = "id_dependencia")
  private SeccionSubSeccion id_dependencia;
}
