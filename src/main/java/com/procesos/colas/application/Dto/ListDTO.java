package com.procesos.colas.application.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDTO {
    @JsonProperty
    private PaginationDTO pagination;
    @JsonProperty
    private List<EntryDTO> entries;
}
