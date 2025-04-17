package org.personalDev.fixtures.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {

    @Id
    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate maxBirthDate;
    private LocalDate minBirthDate;

    @ManyToOne
    private Team team;
}
