package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaArchivosService;
import com.prolinktic.sgdea.domain.exception.CustomHttpException;
import com.prolinktic.sgdea.domain.model.alfesco.UploadFile;
import com.prolinktic.sgdea.domain.model.autentificacion.Usuarios;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.domain.service.alfresco.AlfrescoServices;
import com.prolinktic.sgdea.infrastructure.in.dto.correspondencia.CorrespondenciaGuardarDocumentoSalidaDTO;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.TutelaArchivoDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaArchivo;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.tutelas.TutelaDocumentoSalidaEntity;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.UsuarioJpaRepository;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.tutela.TutelaDocumentoSalidaRepository;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.tutela.TutelasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TutelaArchivoServiceImpl implements TutelaArchivosService {

    private final TutelaDao tutelaDao;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final AlfrescoServices alfrescoServices;
    private final TutelaArchivoDao tutelaArchivoDao;
    private final TutelasRepository tutelasRepository;
    private final TutelaDocumentoSalidaRepository tutelaDocumentoSalidaRepository;

    public TutelaArchivoServiceImpl(TutelaDao tutelaDao, UsuarioJpaRepository usuarioJpaRepository, AlfrescoServices alfrescoServices, TutelaArchivoDao tutelaArchivoDao, TutelasRepository tutelasRepository, TutelaDocumentoSalidaRepository tutelaDocumentoSalidaRepository) {
        this.tutelaDao = tutelaDao;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.alfrescoServices = alfrescoServices;
        this.tutelaArchivoDao = tutelaArchivoDao;
        this.tutelasRepository = tutelasRepository;
        this.tutelaDocumentoSalidaRepository = tutelaDocumentoSalidaRepository;
    }

    @Override
    public List<TutelaArchivo> subirArchivo(Long id, Long usuarioId, MultipartFile[] files, boolean es_devolucion) {
        Tutela tutela = tutelasRepository.findTutelaById(id).orElseThrow(()-> new IllegalArgumentException("No se encontro tutela"));
        Usuarios usuario = usuarioJpaRepository.obtenerUsuarioSinRelacion(usuarioId).orElseThrow(()-> new NullPointerException("Usuario no encontrado"));
        List<UploadFile> alfrescoArchivos = alfrescoServices.uploadFiles(tutela.getNodeId(),files);
        List<TutelaArchivo> archivos = alfrescoArchivos.stream()
                .map(item-> {
                    if("FAIL".equals(item.getStatus())){
                        return null;
                    }
                    return TutelaArchivo.builder()
                            .nodeId(item.getId())
                            .nombre(item.getName())
                            .tipo("De Salida")
                            .tutelaId(tutela.getIdTutela())
                            .usuario(usuario.getFirstname()+" "+usuario.getLastname())
                            .fechaCreacion(new Date())
                            .es_devolucion(es_devolucion)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
        if(!archivos.isEmpty()){
            tutelaArchivoDao.guardarArchivos(archivos);
        }
        return archivos;
    }

    @Override
    public Map<String, List<?>> subirArchivoConTipo(Long id, Long usuarioId, String tipo, MultipartFile[] files, boolean es_devolucion) {
        Tutela tutela = tutelasRepository.findTutelaById(id).orElseThrow(()-> new IllegalArgumentException("No se encontro tutela"));
        Usuarios usuario = usuarioJpaRepository.obtenerUsuarioSinRelacion(usuarioId)
                .orElseThrow(() -> new NullPointerException("Usuario no encontrado"));

        // Upload the files to Alfresco
        List<UploadFile> alfrescoArchivos = alfrescoServices.uploadFiles(tutela.getNodeId(), files);

        // Prepare lists for successful and failed uploads
        List<TutelaArchivo> successfulUploads = new ArrayList<>();
        List<UploadFile> failedUploads = new ArrayList<>();

        for (UploadFile item : alfrescoArchivos) {
            if ("FAIL".equals(item.getStatus())) {
                failedUploads.add(item);
            } else {
                TutelaArchivo archivo = TutelaArchivo.builder()
                        .nodeId(item.getId())
                        .nombre(item.getName())
                        .tipo(tipo)
                        .tutelaId(tutela.getIdTutela())
                        .usuario(usuario.getFirstname() + " " + usuario.getLastname())
                        .username(usuario.getUserName())
                        .fechaCreacion(new Date())
                        .es_devolucion(es_devolucion)
                        .build();
                successfulUploads.add(archivo);
            }
        }

        // Save the successful uploads to the database
        if (!successfulUploads.isEmpty()) {
            tutelaArchivoDao.guardarArchivos(successfulUploads);
        }

        // Create a map to return the results
        Map<String, List<?>> resultMap = new HashMap<>();
        resultMap.put("successfulUploads", successfulUploads);
        resultMap.put("failedUploads", failedUploads);

        return resultMap;
    }


    @Override
    public TutelaArchivo actualizarArchivo(TutelaArchivo tutelaArchivo) {
        return tutelaArchivoDao.guardarArchivo(tutelaArchivo);
    }

    @Override
    public TutelaDocumentoSalidaEntity tutelaGuardarDocumentoSalida(CorrespondenciaGuardarDocumentoSalidaDTO documentoSalidaDTO) throws CustomHttpException {
        Tutela tutela = tutelasRepository.findTutelaById(documentoSalidaDTO.getIdCorrespondencia()).orElseThrow(() -> new CustomHttpException("La correspondencia con el id " + documentoSalidaDTO.getIdCorrespondencia() + " No fue encontrado", HttpStatus.NOT_FOUND));
        Usuarios usuario = usuarioJpaRepository.findByIdWithRoles(documentoSalidaDTO.getUsuarioCreadorId()).orElseThrow(() -> new CustomHttpException("El usuario con el id " + documentoSalidaDTO.getUsuarioCreadorId() + " No fue encontrado", HttpStatus.NOT_FOUND));

        TutelaDocumentoSalidaEntity documentoSalida = TutelaDocumentoSalidaEntity.builder().tutelaId(tutela.getIdTutela()).contenido(documentoSalidaDTO.getContenido_html()).usuario(usuario.getNumeroDocumento()).build();

        return tutelaDocumentoSalidaRepository.save(documentoSalida);
    }

    @Override
    public TutelaDocumentoSalidaEntity tutelaActualizarDocumentoSalida(CorrespondenciaGuardarDocumentoSalidaDTO documentoSalidaDTO) throws CustomHttpException {
        TutelaDocumentoSalidaEntity documentoSalida = tutelaDocumentoSalidaRepository.findById((long)documentoSalidaDTO.getId_documentoSalida()).orElseThrow(() -> new CustomHttpException("El documento salida con el id " + documentoSalidaDTO.getId_documentoSalida() + " No fue encontrado", HttpStatus.NOT_FOUND));

        documentoSalida.setContenido(documentoSalidaDTO.getContenido_html());
        documentoSalida.setFechaActualizacion(LocalDateTime.now());
        documentoSalida.setEs_nuevo_devolucion(Boolean.TRUE);

        tutelaDocumentoSalidaRepository.save(documentoSalida);

        return documentoSalida;
    }
    @Override
    public List<TutelaDocumentoSalidaEntity> tutelaListaDocumentosSalida(Long tutelaId) throws CustomHttpException {
        // Si quito esta declaracion deja de funcionar ns pq xd
        Tutela tutela = tutelasRepository.findTutelaById(tutelaId).orElseThrow(() -> new CustomHttpException("La tutela con el id " + tutelaId + " No fue encontrado", HttpStatus.NOT_FOUND));

        List<TutelaDocumentoSalidaEntity> documentosSalida = tutelaDocumentoSalidaRepository.findAllByTutelaId(tutela.getIdTutela());

        return documentosSalida;
    }
    @Override
    public TutelaDocumentoSalidaEntity tutelaBuscarDocumentosSalida(int documentoSalidaId) throws CustomHttpException {
        TutelaDocumentoSalidaEntity documentoSalida = tutelaDocumentoSalidaRepository.findById((long)documentoSalidaId).orElseThrow(() -> new CustomHttpException("El documento salida con el id " + documentoSalidaId + " No fue encontrado", HttpStatus.NOT_FOUND));
        return documentoSalida;
    }

    @Override
    public TutelaDocumentoSalidaEntity documentoSalidaFinalStatus(int idDocumentoSalida, boolean es_final) throws CustomHttpException {
        TutelaDocumentoSalidaEntity documentoSalida = tutelaDocumentoSalidaRepository.findById((long)idDocumentoSalida).orElseThrow(() -> new CustomHttpException("El documento salida con el id " + idDocumentoSalida + " No fue encontrado", HttpStatus.NOT_FOUND));
        documentoSalida.setEs_documento_final(es_final);
        tutelaDocumentoSalidaRepository.save(documentoSalida);
        return documentoSalida;
    }

    @Override
    public TutelaDocumentoSalidaEntity documentoSalidaActual(Long tutelaId) {
        return tutelaDocumentoSalidaRepository.findMostRecentByTutelaId(tutelaId);
    }

}
