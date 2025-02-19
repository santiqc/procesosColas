package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "firma")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Firma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_data")
    private String fileData;  // Archivo en Base64

    @Column(name = "file_type")
    private String fileType;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_firma_usuario_id", nullable = false)
    private TipoFirmaUsuario tipoFirmaUsuario;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuarios usuario;
}
