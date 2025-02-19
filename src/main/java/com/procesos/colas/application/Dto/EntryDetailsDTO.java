package com.procesos.colas.application.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.procesos.colas.application.Dto.alfrescoDTO.ContentDTO;
import com.procesos.colas.application.Dto.alfrescoDTO.CreatedByUserDTO;
import com.procesos.colas.application.Dto.alfrescoDTO.ModifiedByUserDTO;
import com.procesos.colas.application.Dto.alfrescoDTO.PropertiesDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class EntryDetailsDTO {
    private String createdAt;
    @JsonProperty("isFolder")
    private boolean isFolder;

    @JsonProperty("isFile")
    private boolean isFile;
    private CreatedByUserDTO createdByUser;
    private String modifiedAt;
    private ModifiedByUserDTO modifiedByUser;
    private String name;
    private String id;
    private String nodeType;
    private ContentDTO content;
    private String parentId;

    private PropertiesDTO properties;


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public CreatedByUserDTO getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(CreatedByUserDTO createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public ModifiedByUserDTO getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(ModifiedByUserDTO modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public PropertiesDTO getProperties() {
        return this.properties;
    }

    public void setProperties(PropertiesDTO properties) {
        this.properties = properties;
    }


}

