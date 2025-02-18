package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.application.port.in.reportes.EnvioEmailsUseCase;
import com.prolinktic.sgdea.application.port.out.devoluciones.DevolucionesFlujoStore;
import com.prolinktic.sgdea.application.services.TutelasServices;
import com.prolinktic.sgdea.common.util.EmailService;
import com.prolinktic.sgdea.domain.model.autentificacion.Usuarios;
import com.prolinktic.sgdea.domain.model.envios.FlujoSgda;
import com.prolinktic.sgdea.domain.model.tutelas.AsignacionTutelas;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.dao.autenticacion.UsuariosDao;
import com.prolinktic.sgdea.infrastructure.dao.envios.FlujoSgdaDao;
import com.prolinktic.sgdea.infrastructure.in.dto.email.EmailDataDTO;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.devoluciones.DevolucionesFlujoEntity;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.tutela.TutelasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

@Component
@Slf4j
public class TutelasDevolucionesScheduler {

    private final TutelaDao tutelaDao;
    private final FlujoSgdaDao flujoSgdaDao;
    private final DevolucionesFlujoStore devolucionesFlujoStore;
    private final TransactionTemplate transactionTemplate;
    private final TutelasRepository tutelaRepository;
    private final EmailService emailService;
    private final UsuariosDao usuariosDao;
    private final TutelasServices tutelasServices;
    private final EnvioEmailsUseCase envioEmailsUseCase;


    public TutelasDevolucionesScheduler(TutelaDao tutelaDao, FlujoSgdaDao flujoSgdaDao, DevolucionesFlujoStore devolucionesFlujoStore, TransactionTemplate transactionTemplate, TutelasRepository tutelasRepository, EmailService emailSerivce, UsuariosDao usuariosdao, TutelasServices tutelasServices, EnvioEmailsUseCase envioEmailsUseCase) {
        this.tutelaDao = tutelaDao;
        this.flujoSgdaDao = flujoSgdaDao;
        this.devolucionesFlujoStore = devolucionesFlujoStore;
        this.transactionTemplate = transactionTemplate;
        this.tutelaRepository = tutelasRepository;
        this.emailService = emailSerivce;
        this.usuariosDao = usuariosdao;
        this.tutelasServices = tutelasServices;
        this.envioEmailsUseCase = envioEmailsUseCase;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void devolucionesTutelas() {
        transactionTemplate.execute(transactionStatus -> {
            try {
                FlujoSgda flujoSgda = flujoSgdaDao.findByNombre("Tutelas");
                List<DevolucionesFlujoEntity> devoluciones = devolucionesFlujoStore.encontrarRegistrosPorFlujo(flujoSgda.getId());
                List<Long> devolucionesEliminar = new ArrayList<>();
                if (!devoluciones.isEmpty()) {
                    for (DevolucionesFlujoEntity devolucion : devoluciones) {
                        Tutela tutela = tutelaDao.obtenerTutelaPorRadicadoSalida(devolucion.getRadicadoSalida());
                        if (tutela == null) {
                            log.info("Tutela con radicado de salida {} no fue encontrada.", devolucion.getRadicadoSalida());
                            continue;
                        }
                        tutela.setEstado("Devolucion");
                        tutela.incrementarNumeroDevoluciones(); // Increment the numeroDevoluciones
                        tutelaDao.guardarTutela(tutela);
                        devolucionesEliminar.add(devolucion.getId());
                    }
                    devolucionesEliminar.forEach(devolucionesFlujoStore::eliminar);
                }
                return true;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        });
    }

    @Scheduled(cron = "0 0 */10 * * ?")
    public void notificacionRadicadosPorVencer() {
        transactionTemplate.execute(transactionStatus -> {
            try {
                Date now = new Date();
                long unDiaEnMilisegundos = 24 * 60 * 60 * 1000; // 24 horas en milisegundos
                Date fechaLimite = new Date(now.getTime() + unDiaEnMilisegundos); // Calcular la fecha límite
                List<Tutela> tutelas = tutelaRepository.findTutelasProximasAVencer(fechaLimite);

                for (Tutela tutela : tutelas) {
                    List<AsignacionTutelas> asignaciones = new ArrayList<>(tutela.getAsignaciones());
                    // Encontrar la asignación más reciente
                    Optional<AsignacionTutelas> asignacionMasReciente = asignaciones.stream()
                            .max(Comparator.comparing(AsignacionTutelas::getFechaAsignacion));

                    if (asignacionMasReciente.isEmpty()) {
                        break;
                    }

                    String userAsignadoUser = asignacionMasReciente.get().getUsuarioAsignado();

                    Optional<Usuarios> usuario = usuariosDao.findByUserName(userAsignadoUser);

                    if (usuario.isEmpty()) {
                        log.error("ERROR AL BUSCAR AL USUARIO ASIGNADO PARA ENVIARLE EL CORREO {}", userAsignadoUser);
                        break;
                    }


                    EmailDataDTO emailDataDTO = new EmailDataDTO();
                    emailDataDTO.setPlantilla("/tutelas/notificar-tutela-asignada-vencimiento.html");
                    String asuntoCorreo = "Urgente - Radicados pròximo a vencer";

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("idRadicado", tutela.getIdRadicado());
                    datos.put("usuario", usuario.get().getFirstname());
                    emailDataDTO.setDestinatario(usuario.get().getEmail());
                    emailDataDTO.setDatos(datos);
                    emailService.sendSimpleMessage(emailDataDTO, asuntoCorreo);
                }

                return true;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        });
    }

    @Scheduled(cron = "0 0 8 * * *") // Se ejecuta todos los días a las 8:00 AM
    public void enviarMailsAsignacionPrimerCorte() {
        // Llamar a enviarMailsAsignacion() con el filtro para el primer corte
        envioEmailsUseCase.showLogs(tutelasServices.enviarMailsAsignacionPrimerCorte());
    }

    @Scheduled(cron = "0 0 12 * * *") // Se ejecuta todos los días a las 12:00 PM
    public void enviarMailsAsignacionSegundoCorte() {
        // Llamar a enviarMailsAsignacion() con el filtro para el segundo corte
        envioEmailsUseCase.showLogs(tutelasServices.enviarMailsAsignacionSegundoCorte());
    }
}
