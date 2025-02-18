package com.procesos.colas.domain.alfesco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    private String status;
    private String message;
    private String parentId;
    private String createdAt;
    private String name;
    private String id;
}
