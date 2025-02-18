package com.procesos.colas.application;

import com.procesos.colas.Infrastructure.exception.EmailSendException;
import com.procesos.colas.application.Dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface EmailService {
    EmailResponseDto sendEmail(RequestEmailDto email) throws EmailSendException;

    Page<EmailDto> getSentEmails(String status, String process, String documentNumber,
                                 LocalDateTime startDate, LocalDateTime endDate, String of,
                                 String to, String subject, String containsWords, Boolean containsAttachments,
                                 Integer pageNo, Integer pageSize);

    FileUploadResponse uploadFile(MultipartFile file) throws EmailSendException;

    Object updateInfoAddresseeAndFiles(String trackingId, String email, RequestAddresseeDto emailRequest) throws EmailSendException;

    Object findByIdHistoryOrTrackingId(Long idHistory, String trackingId) throws EmailSendException;

    EmailResponseDto sendEmail(RequestEmailCompletDto emailRequest) throws EmailSendException;

    Object generateUsageReport(UsageReportRequest request) throws EmailSendException;

    MessageStatusResponse getMessageStatus(String trackingId) throws EmailSendException;

    ReceiptResponse getReceiptByTrackId(String trackingId) throws EmailSendException;

}
