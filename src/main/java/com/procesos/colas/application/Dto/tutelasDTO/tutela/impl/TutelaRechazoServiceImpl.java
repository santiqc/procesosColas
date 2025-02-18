package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaRechazoService;
import com.prolinktic.sgdea.common.util.EmailService;
import com.prolinktic.sgdea.domain.model.autentificacion.Usuarios;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.RechazoTutelaDto;
import com.prolinktic.sgdea.infrastructure.in.dto.email.EmailDataDTO;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.TutelaAsignacionDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TutelaRechazoServiceImpl implements TutelaRechazoService {

    private final TutelaDao tutelaDao;
    private final TutelaAsignacionDao tutelaAsignacionDao;
    private final EmailService emailService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final String plantilla = "tutelas/rechazo-tutela.html";

    public TutelaRechazoServiceImpl(TutelaDao tutelaDao, TutelaAsignacionDao tutelaAsignacionDao, EmailService emailService) {
        this.tutelaDao = tutelaDao;
        this.tutelaAsignacionDao = tutelaAsignacionDao;
        this.emailService = emailService;
    }

    @Override
    public Tutela rechazoTutela(Long tutelaId, RechazoTutelaDto rechazoTutelaDto) {
        Tutela tutela = tutelaDao.obtenerTutelaPorId(tutelaId);
        tutela.setEstado("Aprobación rechazada");
        tutela.setObservacion(rechazoTutelaDto.getObservacion());
        if(tutela.getUsuarioAntiguo()!=null){
            Usuarios usuarioAntiguo = tutela.getUsuarioAntiguo();
            if (usuarioAntiguo.getUsuarioRelaciones().stream().anyMatch(item->item.getRol().getNombre().equals("Gestionador"))){
                tutela.setUserId(tutela.getUsuarioAntiguo());
                tutela.setUsuarioAntiguo(null);
            }
        }
        tutela.setFechaRechazoAprobador(LocalDateTime.now());
//        AsignacionTutelas asignacionTutelas = tutela.getAsignaciones().stream()
//                .filter(item->item.getUsuarioAsignado().equals(tutela.getUserId().getUserName())).findFirst().orElse(null);
//        if(asignacionTutelas != null){
//            asignacionTutelas.setFechaAsignacion(new Date());
//            tutelaAsignacionDao.guardarAsignacion(asignacionTutelas);
//        }
        tutelaDao.guardarTutela(tutela);
        // envio correo
        String asunto = "Rechazo por Aprobador de respuesta del radicado "+tutela.getIdRadicado()+" "+ LocalDateTime.now().format(formatter);
        Map<String,Object> datos = new HashMap<>();
        datos.put("idradicado", tutela.getIdRadicado());
        datos.put("observaciones", rechazoTutelaDto.getObservacion());
        EmailDataDTO emailDataDTO = EmailDataDTO.builder()
                .plantilla(plantilla)
                .destinatario(tutela.getUserId().getEmail())
                .datos(datos)
                .build();
        try {
            emailService.sendSimpleMessage(emailDataDTO, asunto);
        } catch (MessagingException e) {
            log.error("Error al enviar el correo de aprobacion.", e);
        } catch (IOException e) {
            log.error("Error al enviar el correo de aprobacion.", e);
        }
        return tutela;
    }
}
