package com.procesos.colas.application.Dto.tutelasDTO.tutela;

import com.prolinktic.sgdea.domain.exception.CustomHttpException;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.in.controller.tutela.dto.TutelaEnviarDto;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TutelaEnviarService {

    Tutela enviarTutela(Long id,List<MultipartFile> archivos, TutelaEnviarDto enviarDto) throws MessagingException, IOException, CustomHttpException;
    boolean enviarDoc(List<MultipartFile> archivo, String email);
}
