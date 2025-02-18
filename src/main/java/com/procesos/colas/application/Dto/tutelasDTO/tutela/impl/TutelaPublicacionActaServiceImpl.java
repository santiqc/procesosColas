package com.procesos.colas.application.Dto.tutelasDTO.tutela.impl;

import com.prolinktic.sgdea.application.port.store.PublicacionActaStore;
import com.prolinktic.sgdea.application.services.tutela.TutelaPublicacionActaService;
import com.prolinktic.sgdea.domain.model.tutelas.Tutela;
import com.prolinktic.sgdea.infrastructure.in.controller.actas.dto.PublicacionActaDto;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.adapter.tutela.impl.TutelaDao;
import com.prolinktic.sgdea.infrastructure.persistence.postgres.entity.PublicacionActaEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class TutelaPublicacionActaServiceImpl implements TutelaPublicacionActaService {

    private final TutelaDao tutelaDao;
    private final PublicacionActaStore publicacionActaStore;

    @Override
    public boolean publicacionActa(PublicacionActaDto data) {

        try{
            Tutela tutela = tutelaDao.obtenerTutelaPorId(data.getProcesoId());

            PublicacionActaEntity acta = PublicacionActaEntity.builder()
                    .numeroActa(data.getNumeroActa())
                    .nodeId(data.getNodeId())
                    .observacion(data.getObservacion())
                    .fechaActa(LocalDateTime.now())
                    .procesoId(data.getProcesoId())
                    .build();

            publicacionActaStore.guardarPublicacionActa(acta);

            tutela.setPublicacionActa(acta);

            tutelaDao.guardarTutela(tutela);
        }catch (Exception e ){
            log.error("Error en publicacion acta tutelas.", e);
            throw new RuntimeException("Error al procesar la informacion de publicacion acta de tutelas");
        }

        return true;
    }
}
