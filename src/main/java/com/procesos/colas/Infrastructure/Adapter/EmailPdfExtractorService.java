package com.procesos.colas.Infrastructure.Adapter;

import com.procesos.colas.domain.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class EmailPdfExtractorService {

    public Files extractPdfAsFile(byte[] zipBytes) {
        try {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
                 ZipInputStream zis = new ZipInputStream(bais)) {

                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().toLowerCase().endsWith(".eml")) {
                        log.info("Processing EML file: {}", entry.getName());

                        ByteArrayOutputStream emailContent = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            emailContent.write(buffer, 0, len);
                        }

                        Session session = Session.getDefaultInstance(new Properties());
                        MimeMessage message = new MimeMessage(session,
                                new ByteArrayInputStream(emailContent.toByteArray()));

                        Files foundFile = processMessagePartsToFile(message);
                        if (foundFile != null) {
                            // Si encontramos un PDF, lo retornamos inmediatamente
                            log.info("Found PDF in file: {}", entry.getName());
                            return foundFile;
                        }
                    }
                }
            }

            return null;
//            throw new RuntimeException("No PDF found in email attachments");

        } catch (Exception e) {
            log.error("Error processing ZIP content", e);
            throw new RuntimeException("Failed to process ZIP content", e);
        }
    }

    private Files processMessagePartsToFile(Part part) throws Exception {
        if (part.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) part.getContent();
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                Part bodyPart = mimeMultipart.getBodyPart(i);
                String fileName = bodyPart.getFileName();

                if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                    log.info("Found PDF attachment: {}", fileName);

                    byte[] pdfContent;
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        IOUtils.copy(bodyPart.getInputStream(), baos);
                        pdfContent = baos.toByteArray();
                    }

                    Files file = new Files();
                    file.setNameFile(fileName);
                    file.setContentType("application/pdf");
                    file.setFileData(pdfContent);
                    file.setFileSize((long) pdfContent.length);
                    file.setWitness(true);

                    return file;

                } else if (bodyPart.isMimeType("multipart/*")) {
                    Files nestedFile = processMessagePartsToFile(bodyPart);
                    if (nestedFile != null) {
                        return nestedFile;
                    }
                }
            }
        }
        return null;
    }
}