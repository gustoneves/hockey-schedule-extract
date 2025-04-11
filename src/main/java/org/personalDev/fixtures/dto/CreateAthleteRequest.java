package org.personalDev.fixtures.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAthleteRequest {
    private String name;
    private int jersey;
    private String license;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String parentName;
    private String parentEmail;
    private String parentPhone;
} 
