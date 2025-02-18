package com.procesos.colas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Addressee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "email")
    private String email;

    @ManyToMany(mappedBy = "addressees", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Email> emails;

    @Override
    public String toString() {
        return "Addressee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
