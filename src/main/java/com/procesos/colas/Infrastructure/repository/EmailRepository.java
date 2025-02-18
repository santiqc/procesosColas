package com.procesos.colas.Infrastructure.repository;

import com.procesos.colas.domain.Email;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmailRepository {
    void save(Email email);
    Page<Email> filterEmails(String recipient);
    void saveAll(List<Email> emails);
}
