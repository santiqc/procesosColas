package com.procesos.colas.application.Dto;

import com.procesos.colas.domain.Addressee;
import com.procesos.colas.domain.EmailStatusHistory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmailDto {
    private Long id;
    private String since;
    private String forTo;
    private String cc;
    private String bcc;
    private String subject;

    private String body;

    private Long idHistory;
    private String trackingId;
    private Boolean isLargeMail;
    private LocalDateTime sentAt;
    private LocalDateTime eventdate;
    private Boolean isCertificate;
    private String status;

    private Object files;
    private List<Addressee> addressee;

    private Long attachments;
    private String nameApplication;
    private String process;
    private Boolean hasWitness;
    private List<EmailStatusHistory> statusHistory;
}
