package com.procesos.colas.domain.tutelas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "escalamiento_tutela")
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EscalamientoTutelas {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "asunto", length = 255)
  private String asunto;
  @Column(name = "fecha_notificado")
  private Date fechaNotificacion;
  @Column(name = "detalle_pretencion", length = 255)
  private String detallePretencion;
  @Column(name = "motivo_escalamiento", length = 255)
  private String motivoEscalamiento;
  @Column(name = "usuario_escalador")
  private String usuarioEscalador;
  @Column(name = "fecha_cracion")
  private Date fechaDt;
  @Column(name = "fecha_actualizacion")
  private Date fechaUpdateDt;

}
