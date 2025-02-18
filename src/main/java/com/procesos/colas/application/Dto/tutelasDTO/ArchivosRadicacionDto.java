package com.procesos.colas.application.Dto.tutelasDTO;

import com.procesos.colas.domain.TutelaArchivo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchivosRadicacionDto {

    private String nodeIdPrincipal;
    private List<TutelaArchivo> archivosTutelas;
}
