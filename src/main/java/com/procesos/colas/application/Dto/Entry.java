package com.procesos.colas.application.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Entry {
    private boolean isFile;
    private String modifiedAt;
    private String nodeType;
    private String parentId;
    private String createdAt;
    private boolean isFolder;
    private String name;
    private String id;
}
