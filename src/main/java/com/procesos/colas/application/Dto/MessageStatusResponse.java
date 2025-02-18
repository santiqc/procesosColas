package com.procesos.colas.application.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageStatusResponse {
    private MessageStatusDTO messageStatus;
    private ReceiptFile receiptFile;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageStatusRequest {
        @JsonProperty("TrackingId")
        private String TrackingId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptFile {
        private String content;
        private String mediaType;
        private byte[] contentByte;
    }

}