package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelasDevolucionService;
import com.prolinktic.sgdea.common.util.EmailService;
import com.prolinktic.sgdea.domain.exception.CustomHttpException;
import com.prolinktic.sgdea.domain.model.juzgado.Juzgado;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.dao.juzgado.JuzgadoDao;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.ActualizarMetodoEnvioDevolucionDto;
import com.prolinktic.sgdea.infrastructure.in.dto.email.EmailDataDTO;
import com.prolinktic.sgdea.infrastructure.out.correocertificado.CorreoCertificadoService;
import com.prolinktic.sgdea.infrastructure.out.correocertificado.dto.CorreoCorrespondenciaDto;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TutelaDevolucionServiceImpl implements TutelasDevolucionService {

    private final TutelaDao tutelaDao;
    private final JuzgadoDao juzgadoDao;
    private final EmailService emailService;
    private final CorreoCertificadoService correoCertificadoService;

    private static final String PREFIJO_SAL = "SAL";
    private static final String PREFIJO_ENT = "ENT";
    public TutelaDevolucionServiceImpl(TutelaDao tutelaDao, JuzgadoDao juzgadoDao, EmailService emailService, CorreoCertificadoService correoCertificadoService) {
        this.tutelaDao = tutelaDao;
        this.juzgadoDao = juzgadoDao;
        this.emailService = emailService;
        this.correoCertificadoService = correoCertificadoService;
    }

    @Override
    public boolean actualizarMetodoEnvioDevolucion(Long idtutela,ActualizarMetodoEnvioDevolucionDto actualizarMetodoEnvioDevolucionDto) {
        // Objetos globales de la funcion.
        Tutela tutela = null;
        try {
            tutela = tutelaDao.obtenerTutelaPorId(idtutela);
            tutela.setCanal(actualizarMetodoEnvioDevolucionDto.getMetodoEnvioDevolucion());
            tutelaDao.guardarTutela(tutela);
        }catch (Exception e){
            log.error("Error al actualizar el registro de tutela con mensaje {}", e.getMessage(), e);
            return false;
        }
        try {
            Juzgado juzgado = tutela.getJuzgado();
            juzgado.setCorreo(actualizarMetodoEnvioDevolucionDto.getCorreoElectronico());
            juzgadoDao.actualizar(juzgado);
        }catch (Exception e){
            log.error("Error al actualizar el registro de juzgado con mensaje {}", e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean envioDeCorreoDevolucion(Long idTutela, List<MultipartFile> archivos) {
        Tutela tutela = tutelaDao.obtenerTutelaPorId(idTutela);
        String correoDestinatario = tutela.getJuzgado().getCorreo();

        // Ordenar archivos por nombre y prioridad
        List<MultipartFile> archivosOrdenados = ordenarArchivosPorNombre(archivos);

        if (tutela.getCanal().contains("certificado")) {
            return enviarCorreoCertificado(archivosOrdenados, correoDestinatario, tutela);
        } else {
            return enviarCorreoRegular(archivosOrdenados, correoDestinatario, tutela);
        }
    }

    private List<MultipartFile> ordenarArchivosPorNombre(List<MultipartFile> archivos) {
        return archivos.stream()
                .sorted(Comparator
                        .comparing(this::determinarPrioridad) // Primero por prioridad
                        .thenComparing(file -> file.getOriginalFilename())) // Luego por nombre
                .collect(Collectors.toList());
    }

    private int determinarPrioridad(MultipartFile file) {
        String nombreArchivo = file.getOriginalFilename();
        if (nombreArchivo != null) {
            if (nombreArchivo.startsWith(PREFIJO_SAL)) {
                return 1; // Prioridad máxima
            } else if (nombreArchivo.startsWith(PREFIJO_ENT)) {
                return 2; // Segunda prioridad
            }
        }
        return 3; // Prioridad por defecto
    }

    private boolean enviarCorreoCertificado(List<MultipartFile> archivos, String correoDestinatario, Tutela tutela) {
        List<String> codigosAdjuntos = new ArrayList<>();
        try {
            for (MultipartFile archivo : archivos) {
                String codigo = correoCertificadoService.subirArchivoParaCorreoCertificado(archivo);
                codigosAdjuntos.add(codigo);
            }
        } catch (Exception e) {
            log.error("Error al adjuntar documentos al correo certificado {}", e.getMessage(), e);
            return false;
        }

        CorreoCorrespondenciaDto datosCorreo = CorreoCorrespondenciaDto.builder()
                .subject("") // TODO: Asunto de correo de devolución
                .to(correoDestinatario)
                .from("javier.contreras@3tcapital.co")
                .cc("")
                .bcc("")
                .attachments(codigosAdjuntos)
                .body("") // TODO: Contenido de correo
                .isLargeMail(false)
                .build();

        try {
            String idTrackingCorreo = correoCertificadoService.envioCorreoCertificado(datosCorreo);
            tutela.setTrackEmail(idTrackingCorreo);
        } catch (CustomHttpException | IOException e) {
            log.error("Error enviando el correo certificado de devolución con mensaje {}", e.getMessage(), e);
            return false;
        }

        return true;
    }

    private boolean enviarCorreoRegular(List<MultipartFile> archivos, String correoDestinatario, Tutela tutela) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("contenido", "Información del radicado " + tutela.getRefRadicadoSalida());

        EmailDataDTO emailDataDTO = EmailDataDTO.builder()
                .adjuntos(archivos)
                .destinatario(correoDestinatario)
                .datos(datos)
                .plantilla("tutelas/envio-tutela.html") // TODO: Formato del correo
                .build();

        try {
            emailService.sendManyFilesMessage(emailDataDTO, "Información del radicado " + tutela.getRefRadicadoSalida());
        } catch (MessagingException | IOException e) {
            log.error("Error enviando el correo de devolución con mensaje {}", e.getMessage(), e);
            return false;
        }

        return true;
    }
}
