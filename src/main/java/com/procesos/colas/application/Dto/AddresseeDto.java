package com.procesos.colas.application.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddresseeDto {
    private String name;
    private String email;
    private String documentNumber;
}