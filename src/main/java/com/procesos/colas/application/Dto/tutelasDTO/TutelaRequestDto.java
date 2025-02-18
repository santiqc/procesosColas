package com.procesos.colas.application.Dto.tutelasDTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutelaRequestDto {

    private List<MultipartFile> archivos;

    private TutelaBodyRequestDto body;

}
