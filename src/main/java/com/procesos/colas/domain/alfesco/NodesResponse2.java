package com.procesos.colas.domain.alfesco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodesResponse2 {
    private ArrayList<String> aspectNames;
    private String createdAt;
    private boolean isFolder;
    private boolean isFile;
    private LinkedHashMap<String, Object> createdByUser;
    private String modifiedAt;
    private LinkedHashMap<String, Object> modifiedByUser;
    private String name;
    private String id;
    private String nodeType;
    private String parentId;

    // Getters y setters
}
