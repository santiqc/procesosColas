package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Rol {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;

  @Column(name = "id_bonita")
  private int idBonita;

  @Column(name = "id_grupo_bonita")
  private int idGrupoBonita;

  @Column(name = "id_keycloak")
  private String idKeycloak;

  @ManyToMany(mappedBy = "roles")
  @JsonIgnore
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<FlujoSgda> flujos;
}
