package com.procesos.colas.application.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddresseeDto {

    private String name;
    private String documentNumber;
    @JsonIgnore
    private List<AddresseeDto> addressees;
    private List<MultipartFile> file;
}
