package com.procesos.colas.presentation;

import com.procesos.colas.Infrastructure.exception.EmailSendException;
import com.procesos.colas.application.Dto.*;
import com.procesos.colas.application.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/email")
@Tag(name = "Email", description = "API de gestión de correos electrónicos")
public class EmailController {
    private static final Logger log = LoggerFactory.getLogger(EmailController.class);
    private final EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<EmailResponseDto> sendEmail(@RequestBody RequestEmailDto emailRequest) throws EmailSendException {
        return ResponseEntity.ok(emailService.sendEmail(emailRequest));
    }

    @PostMapping(value = "/sendEmailData", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmailResponseDto> sendEmail(
            @ModelAttribute RequestEmailCompletDto emailRequest) throws EmailSendException {

        List<AddresseeDto> addresseeDtos = null;
        try {
            addresseeDtos = objectMapper.readValue(emailRequest.getAddressee(), new TypeReference<List<AddresseeDto>>() {
            });
        } catch (JsonProcessingException e) {
            throw new EmailSendException("Error processing addressee JSON", HttpStatus.BAD_REQUEST);
        }

        emailRequest.setAddressees(addresseeDtos);
        return ResponseEntity.ok(emailService.sendEmail(emailRequest));
    }


    @GetMapping("/all")
    @Operation(summary = "Fetches all sent emails filtered by various parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sent emails retrieved successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<EmailDto>> getSentEmails(
            @Parameter(description = "Email status. Example: SENT")
            @RequestParam(required = false) String status,

            @Parameter(description = "Document number. Example: 123456789")
            @RequestParam(required = false) String nroDocument,

            @Parameter(description = "Related process. Example: PROCESS1")
            @RequestParam(required = false) String process,

            @Parameter(description = "Start date and time in ISO format. Example: 2024-10-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "End date and time in ISO format. Example: 2024-10-31T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Sender's email. Example: from@example.com")
            @RequestParam(required = false) String of,

            @Parameter(description = "Recipient's email. Example: to@example.com")
            @RequestParam(required = false) String to,

            @Parameter(description = "Email subject. Example: Report")
            @RequestParam(required = false) String subject,

            @Parameter(description = "Words that should appear in the body or subject. Example: word1 word2")
            @RequestParam(required = false) String containsWords,

            @Parameter(description = "Indicates if emails with attachments should be included. Example: true")
            @RequestParam(required = false) Boolean containsAttachments,

            @Parameter(description = "Page number for pagination. Example: 0")
            @RequestParam(defaultValue = "0") Integer pageNo,

            @Parameter(description = "Number of results per page. Example: 10")
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<EmailDto> emails = emailService.getSentEmails(status, process, nroDocument, startDate, endDate, of, to, subject, containsWords, containsAttachments, pageNo, pageSize);
        return ResponseEntity.ok(emails);
    }

    @PostMapping(value = "/uploadfile", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = emailService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/updateInfoAddresseeAndFiles/{tracking_id}/{email}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> sendEmail(
            @PathVariable(name = "tracking_id") String trackingId,
            @PathVariable(name = "email") String email,
            @ModelAttribute RequestAddresseeDto emailRequest
    ) throws EmailSendException {
        return ResponseEntity.ok(emailService.updateInfoAddresseeAndFiles(trackingId, email, emailRequest));
    }

    @GetMapping("/findFiles")
    public ResponseEntity<Object> findByIdHistoryOrTrackingId(
            @RequestParam(required = false) Long idHistory,
            @RequestParam(required = false) String trackingId) {
        log.info("Received request to find email by idHistory: {} or trackingId: {}", idHistory, trackingId);

        if (idHistory == null && trackingId == null) {
            log.warn("Both idHistory and trackingId are null. One of them is required.");
            return ResponseEntity.badRequest().body(Map.of("error", "Either 'idHistory' or 'trackingId' must be provided."));
        }
        Object result = emailService.findByIdHistoryOrTrackingId(idHistory, trackingId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/usagereport", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generateUsageReport(
            @RequestBody UsageReportRequest request) {
        Object response = emailService.generateUsageReport(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/messagestatus")
    public ResponseEntity<MessageStatusResponse> getMessageStatus(
            @RequestParam(required = false) String trackingId) {
        MessageStatusResponse response = emailService.getMessageStatus(trackingId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/receipt")
    public ResponseEntity<?> getReceiptByTrackId(
            @RequestParam(required = false) String trackingId,
            @RequestParam(defaultValue = "false") boolean download) {
        ReceiptResponse receiptResponse = emailService.getReceiptByTrackId(trackingId);

        if (download) {
            byte[] zipContent = receiptResponse.getContent().getBytes(StandardCharsets.ISO_8859_1);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", trackingId + ".zip");
            headers.setContentLength(zipContent.length);
            return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.ok(receiptResponse);
        }

    }

}
