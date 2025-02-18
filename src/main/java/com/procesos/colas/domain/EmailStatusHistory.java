package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmailStatusHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String trackingId;

    @Column(columnDefinition = "TEXT")
    private String senderName;

    @Column(columnDefinition = "TEXT")
    private String senderAddress;

    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String status;

    @Column(columnDefinition = "TEXT")
    private String recipientAddress;

    private LocalDateTime deliveredDate;
    private LocalDateTime openedDate;

    @Column(columnDefinition = "TEXT")
    private String deliveryStatus;

    @Column(columnDefinition = "TEXT")
    private String deliveryDetail;

    @Column(columnDefinition = "TEXT")
    private String redactedTextViewDetail;
}
