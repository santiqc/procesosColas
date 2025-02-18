package com.procesos.colas.domain.alfesco;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpedienteAlfresco {
    private String idProceso;
    private String dependencia;
    private String subSerie;
    private String tipoDocumental;
    private String docNumber;
    private String docType;
    private String serie;
}
