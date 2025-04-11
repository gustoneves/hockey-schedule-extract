package org.personalDev.fixtures.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AthleteResponse {
    private UUID id;
    private String name;
    private int jersey;
    private String license;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String parentName;
    private String parentEmail;
    private String parentPhone;
    private byte[] photo;
}
