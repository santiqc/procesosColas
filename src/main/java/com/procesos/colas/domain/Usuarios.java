package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.procesos.colas.application.Utils.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "cargo", referencedColumnName = "id")
    private Cargo cargo;

    @Column(name = "bonita_id")
    private String bonitaId;

    @Column(name = "email")
    private String email;

    @Column(name = "email_personal")
    private String emailPersonal;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_firma_predeterminada")
    @JsonIgnore
    private Firma firmaPredeterminada;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulos modulo;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private Set<UsuarioRelacion> usuarioRelaciones;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipousuario", nullable = false)
    private TipoUsuario tipoUsuario = TipoUsuario.Interno; // Default value is 'Interno'

    @ManyToOne
    @JoinColumn(name = "tipo_documento")
    private TipoDocumentoUsuario tipoDocumento;

    @Column(name = "causa_ultima_inactivacion")
    private String causaUltimaInactivacion;

    @Column(name = "fecha_ultima_inactivacion")
    private LocalDateTime fechaUltimaInactivacion;

    @Column(name = "ultima_fecha_acceso")
    private LocalDateTime ultimaFechaAcceso;

    @Column(name = "correo_certificado_gestionador")
    private String correoCertificadoGestionador; // Nuevo campo

    @Column(name = "consolida")
    private Boolean consolida = false; // Nuevo campo con valor predeterminado


    @Column(name = "accion_modificacion")
    private String accionModificacion; // Nuevo campo para registrar el tipo de modificación

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "razonSocial")
    private String razonSocial;

    @Column(name = "isConsolidador")
    private Boolean isConsolidador = false;

    @Column(name = "celular")
    private Long celular;

    @Column(name = "isCertificado", nullable = false)
    private Boolean isCertificado = true;

    @Column(name = "timeout_min")
    private int timeout;

    @ManyToOne
    @JoinColumn(name = "usuario_modificacion_id")
    private Usuarios usuarioModificacion; // Relación a la misma entidad


    @PrePersist
    protected void onCreate() {
        this.accionModificacion = "Creacion de usuario";
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {

        if (this.getEnabled() != null && this.getEnabled()) {
            this.accionModificacion = "Activo";
        } else if (this.getEnabled() != null && !this.getEnabled()) {
            this.accionModificacion = "Inactivo";
        } else {
            this.accionModificacion = "Actualizacion de datos";
        }

        fechaModificacion = LocalDateTime.now();
    }

    @Transient
    public String getFullname() {
        return firstname + " " + lastname;
    }

    @Transient
    public Long getFirmaPredeterminadaId() {
        return (firmaPredeterminada != null) ? firmaPredeterminada.getId() : null;
    }

    public void setTimeoutMin(Integer timeoutMin) {
    }
}
