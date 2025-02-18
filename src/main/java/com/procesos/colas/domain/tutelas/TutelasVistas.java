package com.procesos.colas.domain.tutelas;

import com.prolinktic.sgdea.domain.model.autentificacion.Usuarios;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "TutelasVistas")
public class TutelasVistas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "tutela_id", nullable = false)
    private Tutela tutela;

    @Column(name = "fecha_vista", nullable = false)
    private LocalDateTime fechaVista;

    @PrePersist
    protected void onCreate() {
        fechaVista = LocalDateTime.now();
    }
}
