package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Email implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String since;
    private String forTo;
    private String cc;
    private String bcc;
    private String subject;

    @Lob
    private String body;

    private Long idHistory;
    private String trackingId;
    private Boolean isLargeMail;

    private LocalDateTime sentAt;
    @Column(name = "eventdate")
    private LocalDateTime eventdate;
    private Boolean isCertificate;
    private String status;

    @Column(name = "process")
    private String process;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "email_addressee",
            joinColumns = @JoinColumn(name = "email_id"),
            inverseJoinColumns = @JoinColumn(name = "addressee_id")
    )
    private List<Addressee> addressees;

    @Column(name = "has_witness")
    private Boolean hasWitness = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id")
    private List<EmailStatusHistory> statusHistory;

}