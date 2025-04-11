package org.personalDev.fixtures.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Athlete {

    @Id
    private UUID id;
    private String name;
    private int jersey;
    private String license;
    private LocalDate birthDate;

    @Column(columnDefinition = "bytea")
    private byte[] photo;

    private String phone;
    private String email;

    private String parentName;
    private String parentEmail;
    private String parentPhone;

    enum Status {
        TERMINATED, ACTIVE
    }
}
