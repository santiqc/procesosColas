package com.procesos.colas.domain.tutelas;

import com.procesos.colas.domain.Usuarios;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tutelas_usuarios_escalados")
public class TutelaUsuarioEscalado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;
    private String usuario;
    private String estado;
    @ManyToOne
    @Nullable
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuarios usuarioInfo;
    @Column(name = "tutela_id")
    private Long tutelaId;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @Column(name = "fecha_actualizacion")
    private Date fechaActualizacion;
    @Column(name = "motivo", length = 9000)
    private String motivo;

}
