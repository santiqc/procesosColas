package com.procesos.colas.application.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageStatusDTO {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("StatusCode")
    private int statusCode;

    @JsonProperty("StatusText")
    private String statusText;

    @JsonProperty("Message")
    private List<MessageDTO> message;

    @JsonProperty("ResultContent")
    private List<ResultContentDTO> resultContent;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDTO {
        @JsonProperty("Message")
        private String message;

        @JsonProperty("MessageId")
        private String messageId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResultContentDTO {
        @JsonProperty("TrackingId")
        private String trackingId;

        @JsonProperty("CustomerTrackingId")
        private String customerTrackingId;

        @JsonProperty("SenderName")
        private String senderName;

        @JsonProperty("SenderAddress")
        private String senderAddress;

        @JsonProperty("Date")
        private String date;

        @JsonProperty("Status")
        private String status;

        @JsonProperty("Recipients")
        private List<RecipientDTO> recipients;

        @JsonProperty("RedactedTextViewDetail")
        private String redactedTextViewDetail;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RecipientDTO {
            @JsonProperty("Address")
            private String address;

            @JsonProperty("RecipientType")
            private String recipientType;

            @JsonProperty("DeliveredDate")
            private String deliveredDate;

            @JsonProperty("OpenedDate")
            private String openedDate;

            @JsonProperty("DeliveryStatus")
            private String deliveryStatus;

            @JsonProperty("DeliveryDetail")
            private String deliveryDetail;
        }
    }
}
