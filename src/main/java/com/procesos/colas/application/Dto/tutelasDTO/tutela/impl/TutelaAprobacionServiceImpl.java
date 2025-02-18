package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaAprobacionService;
import com.prolinktic.sgdea.common.util.EmailService;
import com.prolinktic.sgdea.domain.exception.CustomHttpException;
import com.prolinktic.sgdea.domain.model.autentificacion.Usuarios;
import com.prolinktic.sgdea.domain.model.envios.EstadoEnvios;
import com.prolinktic.sgdea.domain.model.reasignacion.Reasignacion;
import com.prolinktic.sgdea.domain.model.reporte.salidas.ReporteSalida;
import com.prolinktic.sgdea.domain.model.tutelas.AsignacionTutelas;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.domain.service.envios.EnviosService;
import com.prolinktic.sgdea.domain.service.secuencia.GenerarSecuenciaService;
import com.prolinktic.sgdea.infrastructure.dao.envios.EstadoEnviosDao;
import com.prolinktic.sgdea.infrastructure.dao.envios.FlujoSgdaDao;
import com.prolinktic.sgdea.infrastructure.dao.reportes.salidas.ReporteSalidaRepository;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.TutelaAprobacionDto;
import com.prolinktic.sgdea.infrastructure.in.dto.email.EmailDataDTO;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.TutelaAsignacionDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.pqrd.OficinaEntity;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.UsuarioJpaRepository;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.pqrd.OficinaJpaRepository;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.reasignacion.ReasignacionJpaRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TutelaAprobacionServiceImpl implements TutelaAprobacionService {
    private final ReasignacionJpaRepository reasignacionJpaRepository;

    private final TutelaDao tutelaDao;
    private final TutelaAsignacionDao tutelaAsignacionDao;
    private final GenerarSecuenciaService generarSecuenciaService;
    private final EmailService emailService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final String plantilla = "tutelas/aprobar-tutela.html";
    private final EnviosService enviosService;
    private final EstadoEnviosDao estadoEnviosDao;
    private final FlujoSgdaDao flujoSgdaDao;
    private final OficinaJpaRepository oficinaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final ReporteSalidaRepository reporteSalidaRepository;


    public TutelaAprobacionServiceImpl(TutelaDao tutelaDao, TutelaAsignacionDao tutelaAsignacionDao, GenerarSecuenciaService generarSecuenciaService, EmailService emailService, EnviosService enviosService, EstadoEnviosDao estadoEnviosDao, FlujoSgdaDao flujoSgdaDao, OficinaJpaRepository oficinaJpaRepository, UsuarioJpaRepository usuarioJpaRepository,
                                       ReasignacionJpaRepository reasignacionJpaRepository, ReporteSalidaRepository reporteSalidaRepository) {
        this.tutelaDao = tutelaDao;
        this.tutelaAsignacionDao = tutelaAsignacionDao;
        this.generarSecuenciaService = generarSecuenciaService;
        this.emailService = emailService;
        this.enviosService = enviosService;
        this.estadoEnviosDao = estadoEnviosDao;
        this.flujoSgdaDao = flujoSgdaDao;
        this.oficinaJpaRepository = oficinaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.reasignacionJpaRepository = reasignacionJpaRepository;
        this.reporteSalidaRepository = reporteSalidaRepository;
    }

    @Override
    public Tutela tutelaAprobacion(Long id, TutelaAprobacionDto tutelaAprobacionDto) throws CustomHttpException {

        Tutela tutela = tutelaDao.obtenerTutelaPorId(id);
        tutela.setEstado("Aprobado");
        if(tutela.getUsuarioAntiguo()!=null){
            Usuarios usuarioAntiguo = tutela.getUsuarioAntiguo();
            if (usuarioAntiguo.getUsuarioRelaciones().stream().anyMatch(item->item.getRol().getNombre().equals("Gestionador"))){
                tutela.setUserId(tutela.getUsuarioAntiguo());
                tutela.setUsuarioAntiguo(null);
            }
        }
        AsignacionTutelas asignacionTutelas = tutela.getAsignaciones().stream()
                .filter(item->item.getUsuarioAsignado().equals(tutela.getUserId().getUserName())).findFirst().orElse(null);
        if(asignacionTutelas != null){
            asignacionTutelas.setFechaAsignacion(new Date());
            tutelaAsignacionDao.guardarAsignacion(asignacionTutelas);
        }

        String secuenciaRadicadoSalida = generarSecuenciaService.generarSecuencia("SAL");
        tutela.setRefRadicadoSalida(secuenciaRadicadoSalida);

        Usuarios usuario = usuarioJpaRepository.findById(tutelaAprobacionDto.getUsuario()).orElseThrow(() -> new CustomHttpException("El usuario con el id " + tutelaAprobacionDto.getUsuario() + " No fue encontrado", HttpStatus.NOT_FOUND));

        tutela.setUserAprobadorId(usuario);
        tutela.setFirmaAprobador(tutelaAprobacionDto.getFirma());
        tutela.setFechaAprobacion(LocalDateTime.now());

        List<Reasignacion> reasignaciones = reasignacionJpaRepository
                .findAllByCasoIdAndTipoTramiteId(tutela.getIdTutela(), 2L);

        reasignaciones.forEach(reasignacion -> {
            reasignacion.setAprobadorPrevio(tutela.getUserAprobadorId());
            reasignacion.setAprobador(tutela.getUserAprobadorId());
        });

        if (!reasignaciones.isEmpty())
            reasignacionJpaRepository.saveAll(reasignaciones);

        tutelaDao.guardarTutela(tutela);

        // envio correo
        String asunto = "Radicado "+tutela.getIdRadicado() + " aprobado "+ LocalDateTime.now().format(formatter);
        Map<String,Object> datos = new HashMap<>();
        datos.put("idradicado", tutela.getIdRadicado());
        datos.put("idradicadosalida", secuenciaRadicadoSalida);
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


        EstadoEnvios estadoEnvios = estadoEnviosDao.findById(3L)
                .orElseThrow(() -> new RuntimeException("Estado de envíos no encontrado con el ID: " + 1));

        OficinaEntity oficina = oficinaJpaRepository.findByNombreIgnoreCase("GRUPO TUTELAS");

// Crear gestión de envío
// Verificar que el tipo de envío sea 'Mensajero', 'Mensajería', o 'Courier'
        String formaEnvio = tutela.getRadicadoSalida() != null ? tutela.getRadicadoSalida().getFormaEnvio() : null;

        if ("Mensajero".equalsIgnoreCase(formaEnvio) ||
                "Mensajería".equalsIgnoreCase(formaEnvio) ||
                "Courier".equalsIgnoreCase(formaEnvio)) {


        }
        guardarReporteSalida(tutela);
        
        return tutela;
    }

    private void guardarReporteSalida(Tutela tutela) {
        reporteSalidaRepository.save(ReporteSalida.builder()
                .tutela(tutela)
                .salida(true)
                .fecha(LocalDateTime.now()).build());
    }

}
