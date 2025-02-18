package com.procesos.colas.domain.alfesco;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class CreateNode {
    private String name;
    private String description;
    private String nodeType;
    private Map<String, Object> properties;
}
