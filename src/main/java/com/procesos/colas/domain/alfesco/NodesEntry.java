package com.procesos.colas.domain.alfesco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodesEntry {

    private String id;
    private String name;
    private String nodeType;
    private String parentId;
    private boolean isFolder;
    private boolean isFile;
}
