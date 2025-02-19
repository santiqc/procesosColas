package com.procesos.colas.application.Dto.alfrescoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedByUserDTO {
    private String id;
    private String displayName;
}
