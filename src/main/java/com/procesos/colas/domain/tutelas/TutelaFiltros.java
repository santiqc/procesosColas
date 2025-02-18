package com.procesos.colas.domain.tutelas;

import com.prolinktic.sgdea.infrastructure.in.dto.tutela.CamposTutela;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutelaFiltros {
    private Map<CamposTutela, String> campos;
    private Date fechaInicio;
    private Date fechaFin;
}
