package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_relacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRelacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Usuarios usuario;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "seccionsubseccion_id")
  private SeccionSubSeccion seccionSubSeccion;

  // Opcionalmente, null si no está asociada a una OficinaModel específica
  @ManyToOne(optional = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "oficina_id", nullable = true)
  private OficinaModel oficina;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "punto_radicacion_id")
  private PuntoRadicacion puntoRadicacion;

  @ManyToOne
  @JoinColumn(name = "rol_id")
  private Rol rol;
}