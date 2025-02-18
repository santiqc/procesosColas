package com.procesos.colas.application.Dto;

import com.procesos.colas.application.Dto.CamerFirma.AttributesLoginResponse;
import com.procesos.colas.application.Dto.CamerFirma.DataLoginResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginCamerResponse {
    private DataLoginResponse data;
    private AttributesLoginResponse dataAdmin;

    @JsonProperty("data")
    public DataLoginResponse getData() { return data; }
    @JsonProperty("data")
    public void setData(DataLoginResponse value) { this.data = value; }
}