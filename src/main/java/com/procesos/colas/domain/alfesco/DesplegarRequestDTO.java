package com.procesos.colas.domain.alfesco;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesplegarRequestDTO {

    private String version;
    private int idCCD = 0;

}
