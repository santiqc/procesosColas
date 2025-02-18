package com.procesos.colas.application.mapper;

import com.procesos.colas.application.Dto.EmailDto;
import com.procesos.colas.domain.Email;

public class EmailMapper {

    public static EmailDto toDTO(Email email, boolean mapAddresseeEmail) {
        if (email == null) {
            return null;
        }

        EmailDto emailDto = new EmailDto();
        emailDto.setId(email.getId());
        emailDto.setSince(email.getSince());
        emailDto.setForTo(email.getForTo());
        emailDto.setCc(email.getCc());
        emailDto.setBcc(email.getBcc());
        emailDto.setSubject(email.getSubject());
        emailDto.setBody(email.getBody());
        emailDto.setIdHistory(email.getIdHistory());
        emailDto.setTrackingId(email.getTrackingId());
        emailDto.setIsLargeMail(email.getIsLargeMail());
        emailDto.setSentAt(email.getSentAt());
        emailDto.setEventdate(email.getEventdate());
        emailDto.setIsCertificate(email.getIsCertificate());
        emailDto.setStatus(email.getStatus());
        emailDto.setProcess(email.getProcess());

        emailDto.setHasWitness(email.getHasWitness());
        if (email.getApplication() != null) {
            emailDto.setNameApplication(email.getApplication().getName());
        }

        if (email.getAddressees() != null) {
            emailDto.setAddressee(email.getAddressees());
        }

        emailDto.setStatusHistory(email.getStatusHistory());

        return emailDto;
    }


    public static EmailDto toDTO(Email email) {
        return toDTO(email, false);
    }

    public static Email toEntity(EmailDto emailDto) {
        if (emailDto == null) {
            return null;
        }

        return Email.builder()
                .id(emailDto.getId())
                .since(emailDto.getSince())
                .forTo(emailDto.getForTo())
                .cc(emailDto.getCc())
                .bcc(emailDto.getBcc())
                .subject(emailDto.getSubject())
                .body(emailDto.getBody())
                .idHistory(emailDto.getIdHistory())
                .trackingId(emailDto.getTrackingId())
                .isLargeMail(emailDto.getIsLargeMail())
                .sentAt(emailDto.getSentAt())
                .eventdate(emailDto.getEventdate())
                .isCertificate(emailDto.getIsCertificate())
                .status(emailDto.getStatus())
                .build();
    }
}