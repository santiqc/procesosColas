package com.procesos.colas.domain.tutelas;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.procesos.colas.domain.OficinaModel;
import com.procesos.colas.domain.PublicacionActaEntity;
import com.procesos.colas.domain.Usuarios;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tutela")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Tutela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtutela")
    Long idTutela;
    @NotEmpty(message = "El campo canal de recepción es obligatorio")
    private String canal;
    @NotEmpty(message = "El campo código del proceso es obligatorio")
    private String codigoProceso;
    @NotEmpty(message = "El campo fecha de ingreso de correo es obligatorio")
    private String fecha;
    @NotEmpty(message = "El campo descripción es obligatorio")
    @Size(max = 1500, min = 10, message = "El campo descripción debe contener entre 10 y 1500 caracteres")
    private String descripcion;
    @Column
    private String idRadicado;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechaRadicacion")
    private Date fechaRadicacion;
    @NotEmpty(message = "El campo estado tutela es obligatorio")
    @Column(name = "estado")
    private String estado;
    @Column(name = "node_id")
    private String nodeId;
    @Column(name = "codprocesovalidado")
    private boolean codprocesovalidado;
    @Column
    private String pais;
    @Column
    private String departamento;
    @Column
    private String municipio;

    @ManyToOne
    @JoinColumn(name = "id_datos_salida", referencedColumnName = "id")
    private DatoSalida radicadoSalida;

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tutela_asignacion_relacion", joinColumns = {
            @JoinColumn(name = "tutela_id", referencedColumnName = "idTutela", table = "tutela")
    }, inverseJoinColumns = {
            @JoinColumn(name = "asignacion_id", referencedColumnName = "id_asignacion", table = "asignacion_tutelas")
    })
    private List<AsignacionTutelas> asignaciones;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "juzgado_id", referencedColumnName = "id_juzgado")
    private Juzgado juzgado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "informacion_proceso_id", referencedColumnName = "id")
    private TutelaDatosProceso informacionProceso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "etapa_id")
    private EtapaProcesal etapaProcesal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_etapa_id")
    private EstadoEtapaProcesal estadoEtapaProcesal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escalamiento_id")
    private EscalamientoTutelas escalamientoTutelas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private Usuarios userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_antiguo_id", referencedColumnName = "id")
    private Usuarios usuarioAntiguo;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutela_id")
    private List<TutelaUsuarioEscalado> usuarioEscalados;
    @Column(name = "tutela_escalada")
    private boolean tutelEscalada;
    @Column
    private String observacion;
    @Column(name = "radicado_salida")
    private String refRadicadoSalida;
    // @Column
    // private String aprobador;
    @Column(name = "fecha_aprobacion")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime fechaAprobacion;
    @Column(name = "firma_aprobador")
    private String firmaAprobador;
    @Column(name = "correo_positiva_envia")
    private String correoPositivaEnvia;
    @Column(name = "fecha_envio")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime fechaEnvio;
    @Column(name = "tracker_mail")
    private String trackEmail;
    @Column(name = "es_devolucion")
    private Boolean es_devolucion;
    @Column(name = "id_usuario_publicador")
    private Long idUsuarioPublicador;
    @Column(name = "motivo_rechazo_aprobacion_publicacion")
    private String motivoRechazoAprobacionPublicacion;
    @Column(name = "motivo_reinicio")
    private String motivoReinicio;
    @Column(name = "motivo_anulacion")
    private String motivoAnulacion;
    @Column(name = "fecha_anulacion")
    private LocalDateTime fechaAnulacion;
    @Column(name = "anulacion_reinicio")
    private boolean anulacionConReinicio = false;
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    @Column(name = "publicacion_cerrada")
    private Boolean publicacionCerrada;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_acta_id", referencedColumnName = "id")
    private PublicacionActaEntity publicacionActa;

    @Column(name = "motivo_devolucion", nullable = true, length = 1000)
    private String motivoDevolucion;

    @PrePersist
    protected void onCreate() {
        fechaRadicacion = new Date();
        reclasificada = Boolean.FALSE;
    }

    @PreUpdate
    public void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public List<AsignacionTutelas> asigarNuevoUsuario(AsignacionTutelas asignacionTutelas) {
        this.getAsignaciones().add(asignacionTutelas);
        return this.getAsignaciones();
    }

    public List<AsignacionTutelas> asigarNuevoUsuario(List<AsignacionTutelas> asignacionTutelas) {
        this.getAsignaciones().addAll(asignacionTutelas);
        return this.getAsignaciones();
    }

    @Column(name = "is_cierre_devolucion")
    private Boolean isCierreDevolucion;

    @Column(name = "numero_devoluciones")
    private Integer numeroDevoluciones = 0; // Default value is 0

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    public void incrementarNumeroDevoluciones() {
        if (this.numeroDevoluciones == null) {
            this.numeroDevoluciones = 1;
        } else {
            this.numeroDevoluciones += 1;
        }
    }

    @ManyToOne
    @JoinColumn(name = "user_asignador_lider_id")
    private Usuarios userAsignadorLiderId;

    @ManyToOne
    @JoinColumn(name = "user_asignador_responsable_id")
    private Usuarios userAsignadorResponsableId;

    @ManyToOne
    @JoinColumn(name = "user_aprobador_id")
    private Usuarios userAprobadorId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "oficina_id", nullable = false)
    private OficinaModel oficina;

    @Column(name = "fecha_rechazo_aprobador")
    private LocalDateTime fechaRechazoAprobador;

    @Column(name = "reclasificada")
    private Boolean reclasificada;

    @Column(name = "usuario_creador")
    private String usuarioCreador;

    @Column(name = "es_radicacion_automatica")
    private Boolean radicacionAutomatica;

    @Transient
    public String getFechaRadicacionFormatted() {
        if (this.fechaRadicacion == null)
            return null;

        // Convertir Date a LocalDateTime
        LocalDateTime localDateTime = this.fechaRadicacion.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"));
    }

    public static Tutela crearTutela(String idRadicado, String nodeId,
            Date fechaRadicacion, String estado, Boolean reclasificada, OficinaModel oficina) {
        Tutela tutela = new Tutela();
        tutela.setIdRadicado(idRadicado);
        tutela.setNodeId(nodeId);
        tutela.setFechaRadicacion(fechaRadicacion);
        tutela.setEstado(estado);
        tutela.setReclasificada(reclasificada);
        tutela.setOficina(oficina);
        return tutela;
    }

}
