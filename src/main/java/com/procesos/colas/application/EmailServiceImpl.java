package com.procesos.colas.application;

import com.procesos.colas.Infrastructure.Adapter.EmailPdfExtractorService;
import com.procesos.colas.Infrastructure.Adapter.EmailPersistenceAdapter;
import com.procesos.colas.Infrastructure.Adapter.SendGridAdapter;
import com.procesos.colas.Infrastructure.exception.EmailSendException;
import com.procesos.colas.application.Dto.*;
import com.procesos.colas.application.mapper.EmailMapper;
import com.procesos.colas.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final EmailPersistenceAdapter emailPersistenceAdapter;
    private final SendGridAdapter sendGridAdapter;
    private final LoginService loginService;
    private final EmailPdfExtractorService emailPdfExtractorService;

    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/Bogota"));

    public EmailServiceImpl(EmailPersistenceAdapter emailPersistenceAdapter, SendGridAdapter sendGridAdapter, LoginService loginService, EmailPdfExtractorService emailPdfExtractorService) {
        this.emailPersistenceAdapter = emailPersistenceAdapter;
        this.sendGridAdapter = sendGridAdapter;
        this.loginService = loginService;
        this.emailPdfExtractorService = emailPdfExtractorService;
    }

    @Override
    public EmailResponseDto sendEmail(RequestEmailDto emailRequest) {
        log.info("Starting email sending process for application: {}", emailRequest.getNameApplication());

        try {
            LoginCamerResponse login = loginService.login();
            String token = login.getData().getAttributes().getAccessToken();
            log.debug("Token acquired for email sending.");


            EmailResponseDto response = sendGridAdapter.sendEmail(token, emailRequest);
            log.info("Email sent successfully with tracking ID: {}", response.getData().getAttributes().getTrackingID());


            Application application = new Application();
            application.setName(emailRequest.getNameApplication());
            Application savedApplication = emailPersistenceAdapter.saveApplication(application);

            String trackingID = response.getData().getAttributes().getTrackingID();

            if (savedApplication != null && savedApplication.getId() != null) {
                log.info("Application saved successfully with ID: {}", savedApplication.getId());

                Email email = Email.builder()
                        .since(emailRequest.getFrom())
                        .forTo(emailRequest.getTo())
                        .cc(emailRequest.getCc())
                        .bcc(emailRequest.getBcc())
                        .subject(emailRequest.getSubject())
                        .body(emailRequest.getBody())
                        .trackingId(trackingID)
                        .isLargeMail(emailRequest.getIsLargeMail())
                        .sentAt(zonedDateTime.toLocalDateTime())
                        .eventdate(zonedDateTime.toLocalDateTime())
                        .isCertificate(true)
                        .process("Sin procesos")
                        .status("Enviado")
                        .application(savedApplication)
                        .hasWitness(false)
                        .build();

                emailPersistenceAdapter.saveEmail(email);
                log.info("Email record saved successfully in the database.");
            } else {
                log.warn("Application was not saved; skipping email record saving.");
            }

            return response;

        } catch (Exception e) {
            log.error("An error occurred during email sending process for application: {}", emailRequest.getNameApplication(), e);
            throw new EmailSendException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Page<EmailDto> getSentEmails(String status, String process, String documentNumber,
                                        LocalDateTime startDate, LocalDateTime endDate, String of,
                                        String to, String subject, String containsWords, Boolean containsAttachments,
                                        Integer pageNo, Integer pageSize) {

        Page<Email> emails = emailPersistenceAdapter.filterEmailsByStatusCcAndProcess(status, process, documentNumber, startDate, endDate, of, to, subject, containsWords, containsAttachments, pageNo, pageSize);
        if (emails.isEmpty()) {
            return Page.empty();
        }

        List<EmailDto> emailDtos = emails.stream()
                .map(email -> {
                    List<Files> files = emailPersistenceAdapter.findFilesByIdHistoryOrTrackingId(email.getTrackingId(), email.getIdHistory());

                    EmailDto emailDto = EmailMapper.toDTO(email);

                    if (files != null && !files.isEmpty()) {
                        List<Map<String, Object>> fileDetailsList = files.stream()
                                .map(file -> {
                                    Map<String, Object> fileMap = new HashMap<>();
                                    fileMap.put("base64", Base64.getEncoder().encodeToString(file.getFileData()));
                                    fileMap.put("nameFile", file.getNameFile());
                                    fileMap.put("contentType", file.getContentType());
                                    fileMap.put("fileSize", file.getFileSize());
                                    fileMap.put("uploadDate", file.getUploadDate());
                                    fileMap.put("witness", file.getWitness());
                                    return fileMap;
                                })
                                .collect(Collectors.toList());
                        emailDto.setFiles(fileDetailsList);
                    }

                    Long attachmentCount = files.stream()
                            .filter(file -> Boolean.FALSE.equals(file.getWitness()))
                            .count();
                    emailDto.setAttachments(attachmentCount);

                    return emailDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(emailDtos, emails.getPageable(), emails.getTotalElements());
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) {
        log.info("Initiating file upload for file: {}", file.getOriginalFilename());
        LoginCamerResponse login = loginService.login();
        String token = login.getData().getAttributes().getAccessToken();

        String response = sendGridAdapter.uploadFile(file, token);

        return FileUploadResponse.builder()
                .data(FileUploadResponse.FileData.builder()
                        .type("file")
                        .fileName(file.getOriginalFilename())
                        .attributes(FileUploadResponse.Attributes.builder()
                                .response(response)
                                .build())
                        .build())
                .build();
    }

    public String extractResponseId(FileUploadResponse response) {
        String responseValue = response.getData().getAttributes().getResponse();
        return responseValue.replace("\"", "");
    }

    @Override
    @Transactional
    public Object updateInfoAddresseeAndFiles(String trackingId, String emailAddressee, RequestAddresseeDto emailRequest) throws EmailSendException {
        try {

            Email email = emailPersistenceAdapter.findEmailByTrackingId(trackingId)
                    .orElseThrow(() -> new EmailSendException("Email not found with tracking ID: " + trackingId, HttpStatus.BAD_REQUEST));

            log.info("Found email with tracking ID: {}", trackingId);


            List<Addressee> addresseesList = new ArrayList<>();
            if (emailRequest.getAddressees() != null && !emailRequest.getAddressees().isEmpty()) {
                for (AddresseeDto dto : emailRequest.getAddressees()) {
                    boolean alreadyExists = addresseesList.stream()
                            .anyMatch(addressee -> addressee.getEmail().equals(dto.getEmail()));

                    if (!alreadyExists) {
                        Addressee addressee = emailPersistenceAdapter.findAddresseeByEmail(dto.getEmail())
                                .orElseGet(() -> new Addressee());

                        addressee.setName(dto.getName());
                        addressee.setDocumentNumber(dto.getDocumentNumber());
                        addressee.setEmail(dto.getEmail());
                        addresseesList.add(addressee);
                        log.info("Processed addressee with email: {}", dto.getEmail());
                    } else {
                        log.warn("Duplicate addressee found for document number: {}", dto.getDocumentNumber());
                    }
                }
            } else {
                Addressee addressee = emailPersistenceAdapter.findAddresseeByEmail(emailAddressee)
                        .orElseGet(() -> new Addressee());

                addressee.setDocumentNumber(emailRequest.getDocumentNumber());
                addressee.setName(emailRequest.getName());
                addressee.setEmail(emailAddressee);
                addresseesList.add(addressee);
            }
            email.setAddressees(addresseesList);

            List<MultipartFile> filesAux = emailRequest.getFile();
            List<Files> documentos = new ArrayList<>();

            if (filesAux != null && !filesAux.isEmpty()) {
                log.info("Processing {} files for tracking ID: {}", filesAux.size(), trackingId);
                for (MultipartFile aux : filesAux) {
                    Files file = new Files();
                    file.setNameFile(aux.getOriginalFilename());
                    file.setTrackingId(trackingId);
                    file.setUploadDate(zonedDateTime.toLocalDateTime());
                    file.setContentType(aux.getContentType());
                    file.setWitness(Boolean.FALSE);
                    file.setFileData(aux.getBytes());
                    file.setFileSize(aux.getSize());
                    documentos.add(file);
                    log.info("File {} processed and added to the list.", aux.getOriginalFilename());
                }
            } else {
                log.info("No files provided for tracking ID: {}", trackingId);
            }


            emailPersistenceAdapter.saveAllAddressee(addresseesList);
            log.info("Addressee updated successfully for tracking ID: {}", trackingId);

            emailPersistenceAdapter.saveEmail(email);

            if (!documentos.isEmpty()) {
                emailPersistenceAdapter.saveFiles(documentos);
                log.info("{} files saved successfully for tracking ID: {}", documentos.size(), trackingId);
            } else {
                log.info("No files to save for tracking ID: {}", trackingId);
            }

            return Map.of(
                    "message", "Update info for addressee was successful",
                    "addresseeCount", addresseesList.size(),
                    "filesCount", documentos.size()
            );

        } catch (EmailSendException e) {
            log.error("Error processing email for tracking ID {} (EmailSendException): {}", trackingId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating addressee and files for tracking ID {}: {}", trackingId, e);
            throw new EmailSendException("Unexpected error during update: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object findByIdHistoryOrTrackingId(Long idHistory, String trackingId) throws EmailSendException {
        try {
            Email email = emailPersistenceAdapter.findByIdHistoryOrTrackingId(idHistory, trackingId)
                    .orElseThrow(() -> new EmailSendException("Email not found with tracking ID: " + trackingId, HttpStatus.BAD_REQUEST));

            List<Files> files = emailPersistenceAdapter.findFilesByIdHistoryOrTrackingId(email.getTrackingId(), email.getIdHistory());


            List<Map<String, Object>> fileDetailsList = files.stream()
                    .map(file -> {
                        Map<String, Object> fileMap = new HashMap<>();
                        fileMap.put("base64", Base64.getEncoder().encodeToString(file.getFileData()));
                        fileMap.put("nameFile", file.getNameFile());
                        fileMap.put("contentType", file.getContentType());
                        fileMap.put("fileSize", file.getFileSize());
                        fileMap.put("uploadDate", file.getUploadDate());
                        fileMap.put("witness", file.getWitness());
                        return fileMap;
                    })
                    .collect(Collectors.toList());

            long attachmentCount = files.stream()
                    .filter(file -> Boolean.FALSE.equals(file.getWitness()))
                    .count();


            Map<String, Object> response = new HashMap<>();
            response.put("message", "Email and file retrieval successful");
            response.put("trackingId", email.getTrackingId());
            response.put("attachmentCount", attachmentCount);
            response.put("idHistory", email.getIdHistory());
            response.put("files", fileDetailsList);

            return response;
        } catch (EmailSendException e) {
            log.error("Error processing email for tracking ID {} (EmailSendException): {}", trackingId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while retrieving email and files for tracking ID {}: {}", trackingId, e);
            throw new EmailSendException("Unexpected error during retrieval: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EmailResponseDto sendEmail(RequestEmailCompletDto emailRequest) throws EmailSendException {
        log.info("Starting email sending process for application: {}", emailRequest.getNameApplication());

        try {
            LoginCamerResponse login = loginService.login();
            String token = login.getData().getAttributes().getAccessToken();
            log.debug("Token acquired for email sending.");

            if (emailRequest.getFiles() != null) {

                if (emailRequest.getAttachments() == null) {
                    emailRequest.setAttachments(new ArrayList<>());
                }

                for (MultipartFile file : emailRequest.getFiles()) {
                    FileUploadResponse response = uploadFile(file);
                    String idFile = extractResponseId(response);
                    emailRequest.getAttachments().add(idFile);
                }
            }


            RequestEmailDto requestEmailDto = new RequestEmailDto();
            requestEmailDto.setFrom(emailRequest.getFrom());
            requestEmailDto.setTo(emailRequest.getTo());
            requestEmailDto.setCc(emailRequest.getCc());
            requestEmailDto.setBcc(emailRequest.getBcc());
            requestEmailDto.setSubject(emailRequest.getSubject());
            requestEmailDto.setBody(emailRequest.getBody());
            requestEmailDto.setAttachments(emailRequest.getAttachments());
            requestEmailDto.setIsLargeMail(emailRequest.getIsLargeMail());

            EmailResponseDto response = sendGridAdapter.sendEmail(token, requestEmailDto);
            log.info("Email sent successfully with tracking ID: {}", response.getData().getAttributes().getTrackingID());


            Application application = new Application();
            application.setName(emailRequest.getNameApplication());
            Application savedApplication = emailPersistenceAdapter.saveApplication(application);

            String trackingID = response.getData().getAttributes().getTrackingID();

            if (savedApplication != null && savedApplication.getId() != null) {
                log.info("Application saved successfully with ID: {}", savedApplication.getId());

                Email email = Email.builder()
                        .since(emailRequest.getFrom())
                        .forTo(emailRequest.getTo())
                        .cc(emailRequest.getCc())
                        .bcc(emailRequest.getBcc())
                        .subject(emailRequest.getSubject())
                        .body(emailRequest.getBody())
                        .trackingId(trackingID)
                        .isLargeMail(emailRequest.getIsLargeMail())
                        .sentAt(zonedDateTime.toLocalDateTime())
                        .eventdate(zonedDateTime.toLocalDateTime())
                        .isCertificate(true)
                        .status("Enviado")
                        .application(savedApplication)
                        .process(Optional.ofNullable(emailRequest.getProcess()).orElse("Sin procesos"))
                        .hasWitness(false)
                        .build();

                emailPersistenceAdapter.saveEmail(email);
                RequestAddresseeDto dataAddressee = new RequestAddresseeDto();
                dataAddressee.setAddressees(emailRequest.getAddressees());
                dataAddressee.setFile(emailRequest.getFiles());

                //update AddresseeAndFiles
                updateInfoAddresseeAndFiles(trackingID, null, dataAddressee);

                log.info("Email record saved successfully in the database.");
            } else {
                log.warn("Application was not saved; skipping email record saving.");
            }

            return response;

        } catch (Exception e) {
            log.error("An error occurred during email sending process for application: {}", emailRequest.getNameApplication(), e);
            throw new EmailSendException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object generateUsageReport(UsageReportRequest request) throws EmailSendException {
        log.info("Initiating generateUsageReport: {}", request);
        LoginCamerResponse login = loginService.login();
        String token = login.getData().getAttributes().getAccessToken();
        String tokenAdmin = login.getDataAdmin().getAccessToken();

        Object response = sendGridAdapter.generateUsageReport(request, token, tokenAdmin);

        return Map.of(
                "data", response
        );
    }

    @Override
    public MessageStatusResponse getMessageStatus(String trackingId) throws EmailSendException {
        log.info("Initiating getMessageStatus: {}", trackingId);
        LoginCamerResponse login = loginService.login();
        String token = login.getData().getAttributes().getAccessToken();
        String tokenAdmin = login.getDataAdmin().getAccessToken();
        return sendGridAdapter.getMessageStatus(trackingId, token, tokenAdmin, false);
    }

    @Override
    public ReceiptResponse getReceiptByTrackId(String trackingId) throws EmailSendException {
        log.info("Initiating getReceiptByTrackId: {}", trackingId);
        LoginCamerResponse login = loginService.login();
        String token = login.getData().getAttributes().getAccessToken();
        return sendGridAdapter.getReceiptByTrackId(trackingId, token);
    }

    @Scheduled(cron = "0 0 */3 * * *")//Cada 3 horas
//    @Scheduled(cron = "*/10 * * * * *") //de 10 segundos para pruebas
    @Transactional
    public void checkMessageStatus() {
        List<Email> emails = emailPersistenceAdapter.findByHasWitnessFalse();

        LoginCamerResponse login = loginService.login();
        String token = login.getData().getAttributes().getAccessToken();
        String tokenAdmin = login.getDataAdmin().getAccessToken();

        List<Files> arrayList = new ArrayList<>();
        List<Email> emailsToUpdate = emails.stream()
                .filter(email -> email.getTrackingId() != null)
                .map(email -> {
                    String trackingId = email.getTrackingId();
                    try {
                        MessageStatusResponse response = getMessageStatusWithRetry(trackingId, token, tokenAdmin);
                        if (response.getReceiptFile() != null &&
                                response.getReceiptFile().getContent() != null &&
                                !response.getReceiptFile().getContent().isEmpty()) {

                            List<MessageStatusDTO.ResultContentDTO> resultContent = response.getMessageStatus().getResultContent();
                            for (MessageStatusDTO.ResultContentDTO content : resultContent) {
                                for (MessageStatusDTO.ResultContentDTO.RecipientDTO recipient : content.getRecipients()) {
                                    EmailStatusHistory history = EmailStatusHistory.builder()
                                            .trackingId(content.getTrackingId())
                                            .senderName(content.getSenderName())
                                            .senderAddress(content.getSenderAddress())
                                            .date(LocalDateTime.parse(content.getDate()))
                                            .status(content.getStatus())
                                            .recipientAddress(recipient.getAddress())
                                            .deliveredDate(recipient.getDeliveredDate() != null ? LocalDateTime.parse(recipient.getDeliveredDate()) : null)
                                            .openedDate(recipient.getOpenedDate() != null ? LocalDateTime.parse(recipient.getOpenedDate()) : null)
                                            .deliveryStatus(recipient.getDeliveryStatus())
                                            .deliveryDetail(recipient.getDeliveryDetail())
                                            .redactedTextViewDetail(content.getRedactedTextViewDetail())
                                            .build();

                                    email.getStatusHistory().add(history);
                                    email.setHasWitness(true);
                                }
                            }
                            Files pdfFile = emailPdfExtractorService.extractPdfAsFile(response.getReceiptFile().getContentByte());
                            if (pdfFile != null) {
                                pdfFile.setTrackingId(email.getTrackingId());
                                arrayList.add(pdfFile);
                            }

                            log.info("Email with trackingId {} updated to hasWitness = true.", trackingId);
                            return email;
                        } else {
                            log.warn("No receipt file found for trackingId {}. Email will not be updated.", trackingId);
                            return null;
                        }
                    } catch (EmailSendException e) {
                        log.error("Failed to retrieve message status for trackingId {}: {}", trackingId, e.getMessage());
                        throw new EmailSendException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
                    } catch (Exception e) {
                        log.error("Unexpected error for trackingId {}: {}", trackingId, e.getMessage());
                        throw new EmailSendException("An unexpected error occurred for trackingId " + trackingId, e, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        emailPersistenceAdapter.saveFiles(arrayList);
        emailPersistenceAdapter.saveAllEmail(emailsToUpdate);
    }


    //call this method, check for error if not call 3 times
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 60000))
    public MessageStatusResponse getMessageStatusWithRetry(String trackingId, String token, String tokenAdmin) throws EmailSendException {
        return sendGridAdapter.getMessageStatus(trackingId, token, tokenAdmin, true);
    }
}
