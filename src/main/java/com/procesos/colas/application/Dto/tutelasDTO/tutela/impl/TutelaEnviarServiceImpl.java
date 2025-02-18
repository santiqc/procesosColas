package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.services.tutela.TutelaEnviarService;
import com.prolinktic.sgdea.common.util.EmailService;
import com.prolinktic.sgdea.domain.exception.CustomHttpException;
import com.prolinktic.sgdea.domain.exception.DatosSalidaException;
import com.prolinktic.sgdea.domain.model.correspondencia.PuntoRadicacion;
import com.prolinktic.sgdea.domain.model.envios.EstadoEnvios;
import com.prolinktic.sgdea.domain.model.envios.GestionEnvios;
import com.prolinktic.sgdea.domain.model.juzgado.Juzgado;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.domain.service.envios.EnviosService;
import com.prolinktic.sgdea.infrastructure.dao.envios.EstadoEnviosDao;
import com.prolinktic.sgdea.infrastructure.dao.envios.FlujoSgdaDao;
import com.prolinktic.sgdea.infrastructure.dao.juzgado.JuzgadoDao;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.TutelaEnviarDto;
import com.prolinktic.sgdea.infrastructure.in.dto.email.EmailDataDTO;
import com.prolinktic.sgdea.infrastructure.out.correocertificado.CorreoCertificadoService;
import com.prolinktic.sgdea.infrastructure.out.correocertificado.dto.CorreoCorrespondenciaDto;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.pqrd.OficinaEntity;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.repository.pqrd.OficinaJpaRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TutelaEnviarServiceImpl implements TutelaEnviarService {

    private final TutelaDao tutelaDao;
    private final JuzgadoDao juzgadoDao;
    private final EmailService emailService;
    private final CorreoCertificadoService correoCertificadoService;
    private String template = "tutelas/envio-tutela.html";
    private final OficinaJpaRepository oficinaJpaRepository;
    private final EstadoEnviosDao estadoEnviosDao;
    private final EnviosService enviosService;
    private final FlujoSgdaDao flujoSgdaDao;

    @Value("${correo.notificacion.sgdea}")
    private String correoEnviosSGEDEA;
    
    public TutelaEnviarServiceImpl(TutelaDao tutelaDao, JuzgadoDao juzgadoDao, EmailService emailService, CorreoCertificadoService correoCertificadoService, OficinaJpaRepository oficinaJpaRepository, EstadoEnviosDao estadoEnviosDao, EnviosService enviosService, FlujoSgdaDao flujoSgdaDao) {
        this.tutelaDao = tutelaDao;
        this.juzgadoDao = juzgadoDao;
        this.emailService = emailService;
        this.correoCertificadoService = correoCertificadoService;
        this.oficinaJpaRepository = oficinaJpaRepository;
        this.estadoEnviosDao = estadoEnviosDao;
        this.enviosService = enviosService;
        this.flujoSgdaDao = flujoSgdaDao;
    }

    @Override
    public Tutela enviarTutela(Long id, List<MultipartFile> archivos, TutelaEnviarDto enviarDto) throws MessagingException, IOException, CustomHttpException {
        Tutela tutela = tutelaDao.obtenerTutelaPorId(id);
        Juzgado juzgado = tutela.getJuzgado();
        juzgado.setCorreo(enviarDto.getPara());
        juzgadoDao.actualizar(juzgado);
        tutela.setEstado("Enviado");
        tutela.setCorreoPositivaEnvia(enviarDto.getDe());
        tutela.setFechaEnvio(LocalDateTime.now());
        switch (tutela.getRadicadoSalida().getFormaEnvio()){
            case "Correo electrónico certificado" -> {
                envioCorreoCertificado(archivos,enviarDto,tutela);
            }
            case "Correo electrónico" ->{
                envioCorreoNormal(archivos,enviarDto,tutela);
            }
            case "Mensajería","Courier" ->{
                enviosFisicos(enviarDto,tutela);
            }

        }
        tutelaDao.guardarTutela(tutela);
        return tutela;
    }

    private void enviosFisicos(TutelaEnviarDto enviarDto,Tutela tutela) throws CustomHttpException {

        OficinaEntity oficinaEntity = oficinaJpaRepository.findByNombreIgnoreCase(tutela.getUserAprobadorId().getUsuarioRelaciones().stream().findFirst().orElseThrow(() -> new CustomHttpException(DatosSalidaException.USER_NOT_FOUND, HttpStatus.BAD_REQUEST)).getOficina().getNombre());
        PuntoRadicacion puntoRadicacion = tutela.getUserAprobadorId().getUsuarioRelaciones().stream().findFirst().orElseThrow(() -> new CustomHttpException(DatosSalidaException.USER_NOT_FOUND, HttpStatus.BAD_REQUEST)).getPuntoRadicacion();
        EstadoEnvios estadoEnvios = estadoEnviosDao.findById(1L)
                .orElseThrow(() -> new RuntimeException("Estado de envíos no encontrado con el ID: " + 1));
        String destinatario = enviarDto.getPara();

        // Construir el objeto GestionEnvios solo si la condición se cumple
        GestionEnvios envios = GestionEnvios.builder()
                .fechaCreacion(LocalDateTime.now())
                .fechaRadicadoSalida(LocalDateTime.now())  // Usamos la fecha actual como ejemplo
                .radicadoSalida(tutela.getRefRadicadoSalida() != null ? tutela.getRefRadicadoSalida() : null)
                .tipoEnvio(tutela.getRadicadoSalida().getFormaEnvio())  // Se usa la forma de envío ya validada
                .usuario(tutela.getUserId() != null ? tutela.getUserId() : null)
                .estadoEnvios(estadoEnvios)
                .oficina(oficinaEntity)
                .puntoRadicacion(puntoRadicacion)
                .destinatario(tutela.getJuzgado() != null && tutela.getJuzgado().getNombre() != null
                        ? tutela.getJuzgado().getNombre()
                        : null)  // Validar destinatario
                .flujoSgda(flujoSgdaDao.findByNombre("Tutelas"))
                .direccion(tutela.getJuzgado() != null && tutela.getJuzgado().getDireccion() != null
                        ? tutela.getJuzgado().getDireccion()
                        : null)  // Validar dirección
                .municipioDepartamento(tutela.getJuzgado() != null && tutela.getJuzgado().getDepartamento() != null
                        && tutela.getJuzgado().getMunicipio() != null
                        ? tutela.getJuzgado().getDepartamento() + "-" + tutela.getJuzgado().getMunicipio()
                        : null)  // Validar municipio y departamento
                .telefono(tutela.getJuzgado() != null && tutela.getJuzgado().getTelefono() != null
                        ? tutela.getJuzgado().getTelefono()
                        : null)  // Validar teléfono
                .nodeId(tutela.getNodeId() != null ? tutela.getNodeId() : null)
                .build();

        // Solicitar gestión de envío
        enviosService.solictarGestionEnvio(envios);
    }

    private void envioCorreoNormal(List<MultipartFile> archivos, TutelaEnviarDto enviarDto, Tutela tutela) throws MessagingException, IOException {
        Map<String, Object> datos = new HashMap<>();
        datos.put("asunto",enviarDto.getAsunto());
        datos.put("contenido", enviarDto.getContenido());
        EmailDataDTO emailDataDTO = EmailDataDTO.builder()
                .adjuntos(archivos)
                .datos(datos)
                .destinatario(enviarDto.getPara())
                .de(correoEnviosSGEDEA)
                .plantilla(template)
                .ocultas(enviarDto.getCc().split(","))
                .concopia(null)
                .build();
        emailService.sendManyFilesMessage(emailDataDTO, enviarDto.getAsunto());

    }

    private void envioCorreoCertificado(List<MultipartFile> archivos, TutelaEnviarDto enviarDto, Tutela tutela) {
        try{
            List<String> codigosAdjuntos = new ArrayList<>();
            for(MultipartFile archivo : archivos) {
                String codigo = correoCertificadoService.subirArchivoParaCorreoCertificado(archivo);
                codigosAdjuntos.add(codigo);
            }
            CorreoCorrespondenciaDto datosCorreo = CorreoCorrespondenciaDto.builder()
                    .subject(enviarDto.getAsunto())
                    .to(enviarDto.getPara())
                    .from("docu@positiva.gov.co")
                    .cc("")
                    .bcc(enviarDto.getCc())
                    .attachments(codigosAdjuntos)
                    .body(enviarDto.getContenido())
                    .isLargeMail(false)
                    .build();
            String idtrack = correoCertificadoService.envioCorreoCertificado(datosCorreo);
            tutela.setTrackEmail(idtrack);
        }catch (CustomHttpException | IOException e) {
            log.error("Error al enviar el correo", e);
        }
    }

    @Override
    public boolean enviarDoc(List<MultipartFile> archivo, String email) {
        boolean resp= false;
        try {
            Map<String, Object> datos = new HashMap<>();
            datos.put("asunto","Reporte tutelas");
            datos.put("contenido", "Reporte tutelas");
            EmailDataDTO emailDataDTO = EmailDataDTO.builder()
                    .adjuntos(archivo)
                    .datos(datos)
                    .destinatario(email)
                    .de(correoEnviosSGEDEA)
                    .plantilla(template)
                    .concopia(null)
                    .build();

            emailService.sendManyFilesMessage(emailDataDTO, "Reporte tutelas");
            resp= true;
        }catch (MessagingException e){
            log.error("Error al enviar el correo", e);
        } catch (IOException e) {
            log.error("Error al obtener adjunto de correo", e);
        }
        return resp;
    }

}
