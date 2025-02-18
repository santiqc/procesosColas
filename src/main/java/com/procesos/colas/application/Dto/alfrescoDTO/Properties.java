package com.procesos.colas.application.Dto.alfrescoDTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class Properties {


    //inicio de campos nuevo consumo
    @JsonProperty("proltk:tipoRadicado")
    private int tipoRadicado;

    @JsonProperty("proltk:radicadoOrigen")
    private String radicadoOrigen;

    @JsonProperty("proltk:fechaDocumento")
    private LocalDate fechaDocumento;

    @JsonProperty("proltk:medioRecepcion")
    private int medioRecepcion;

    @JsonProperty("proltk:asuntoRadicacion")
    private String asuntoRadicacion;

    @JsonProperty("proltk:identificadorSistema")
    private int identificadorSistema;

    @JsonProperty("proltk:idTramite")
    private String idTramite;

    @JsonProperty("destinatarioDependencia")
    private int destinatarioDependencia;




    @JsonProperty("proltk:destinatarioUbicacionSede")
    private String destinatarioUbicacionSede;

    @JsonProperty("proltk:destinatarioNombresApellidos")
    private String destinatarioNombresApellidos;

    @JsonProperty("proltk:tipoDocumentoRemitente")
    private int tipoDocumentoRemitente;

    @JsonProperty("proltk:numeroDocumentoRemitente")
    private String numeroDocumentoRemitente;

    @JsonProperty("proltk:remitenteTipoPersona")
    private int remitenteTipoPersona;

    @JsonProperty("proltk:nombreRemitente")
    private String nombreRemitente;

    @JsonProperty("proltk:remitenteDireccion")
    private String remitenteDireccion;

    @JsonProperty("proltk:remitentePais")
    private int remitentePais;

    @JsonProperty("proltk:remitenteDepartamento")
    private int remitenteDepartamento;

    @JsonProperty("proltk:remitenteMunicipio")
    private int remitenteMunicipio;

    @JsonProperty("proltk:remitenteCorreoElectronico")
    private String remitenteCorreoElectronico;

    @JsonProperty("proltk:remitenteTelefono")
    private String remitenteTelefono;

    @JsonProperty("proltk:fondoRadicacion")
    private int fondoRadicacion;

    @JsonProperty("proltk:oficinaProductora")
    private int oficinaProductora;

    @JsonProperty("proltk:serieRadicacion")
    private String serieRadicacion;

    @JsonProperty("proltk:subserieRadicacion")
    private String subserieRadicacion;

    @JsonProperty("proltk:tipoDocumentalRadicacion")
    private int tipoDocumentalRadicacion;

    @JsonProperty("proltk:observacionPpal")
    private String observacionPpal;

    @JsonProperty("proltk:descripcionAnexo")
    private String descripcionAnexo; //este campo tipo texto va a ser un array json con los siguientes campos en string:
    //name, observacion, tipoDocumental


    /*
    @JsonProperty("proltk:detinatario_dependencia")
    private String destinatarioDependencia;

    @JsonProperty("proltk:destinatario_ubicacion_sede")
    private String destinatarioUbicacionSede;

    @JsonProperty("proltk:destinatario_nombres_apellidos")
    private String destinatarioNombresApellidos;

    @JsonProperty("proltk:destinatario_copia")
    private String destinatarioCopia;

    @JsonProperty("proltk:tipo_tramite")
    private String tipoTramite;

    @JsonProperty("proltk:tipo_comunicacion")
    private String tipoComunicacion;

    @JsonProperty("proltk:numero_folios")
    private float numeroFolios;

    @JsonProperty("proltk:anexos")
    private boolean anexos;

    @JsonProperty("proltk:descripcion_anexo")
    private String descripcionAnexo;

    @JsonProperty("proltk:numero_folios_anexos")
    private float numeroFoliosAnexos;

    @JsonProperty("proltk:canal_recepcion")
    private String canalRecepcion;

    @JsonProperty("proltk:asunto_radicado")
    private String asuntoRadicado;

    @JsonProperty("proltk:radicado_entidad_remitente")
    private String radicadoEntidadRemitente;

    @JsonProperty("proltk:referenciar_radicado")
    private String referenciarRadicado;

    @JsonProperty("proltk:fecha_original")
    private LocalDate fechaOriginal;

    @JsonProperty("proltk:fecha_recepcion")
    private LocalDate fechaRecepcion;

    @JsonProperty("proltk:hora_recepcion")
    private String horaRecepcion;

    @JsonProperty("proltk:num_radicado")
    private String numRadicado;

    @JsonProperty("proltk:tipo_documento")
    private String tipoDocumento;

    @JsonProperty("proltk:numero_documento")
    private String numeroDocumento;

    @JsonProperty("proltk:nombres_remitente")
    private String nombresRemitente;

    @JsonProperty("proltk:Apellidos_remitente")
    private String ApellidosRemitente;

    @JsonProperty("proltk:pais")
    private String pais;

    @JsonProperty("proltk:departamento")
    private String departamento;

    @JsonProperty("proltk:ciudad")
    private String ciudad;

    @JsonProperty("proltk:direccion_remitente")
    private String direccionRemitente;

    @JsonProperty("proltk:telefono_remitente")
    private String telefonoRemitente;

    @JsonProperty("proltk:movil_remitente")
    private String movilRemitente;

    @JsonProperty("proltk:correo_electronico_remitente")
    private String correoElectronicoRemitente;

    @JsonProperty("proltk:fondo")
    private String fondo;

    @JsonProperty("proltk:seccion")
    private String seccion;

    @JsonProperty("proltk:subseccion")
    private String subseccion;

    @JsonProperty("proltk:serie")
    private String serie;

    @JsonProperty("proltk:subserie")
    private String subserie;

    @JsonProperty("proltk:tipodocumental")
    private String tipoDocumental;

    */
}