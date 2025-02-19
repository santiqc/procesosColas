package com.procesos.colas.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.procesos.colas.application.Dto.TutelaMessage;
import com.procesos.colas.application.services.TutelaServiceProcesamientoImpl;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TutelaProcesamientoConsumer {

    @Autowired
    private TutelaServiceProcesamientoImpl tutelaService;

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "tutela-procesamiento")
    @JmsListener(destination = "tutela-procesamiento")
    public void procesarTutela(Message message) {
        try {
            String jsonContent = null;
            if (message instanceof TextMessage) {
                jsonContent = ((TextMessage) message).getText();
                log.debug("JSON recibido: {}", jsonContent);

                // Convertir el JSON a TutelaMessage
                TutelaMessage tutelaMessage = objectMapper.readValue(jsonContent, TutelaMessage.class);
                tutelaService.procesarTutelaQueue(tutelaMessage);
                log.info("Tutela {} procesada correctamente", tutelaMessage.getRadicado());
            } else {
                log.error("Tipo de mensaje no soportado: {}", message.getClass());
            }
        } catch (Exception e) {
            log.error("Error procesando tutela: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando mensaje", e);
        }
    }
}
