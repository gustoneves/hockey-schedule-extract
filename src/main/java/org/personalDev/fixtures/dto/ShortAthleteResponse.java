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
public class ShortAthleteResponse {
    private UUID id;
    private String name;
    private int jersey;
    private String license;
    private LocalDate birthDate;
}
