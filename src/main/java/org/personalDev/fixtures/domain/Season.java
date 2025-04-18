package org.personalDev.fixtures.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Cacheable(false)
public class Season {

    @Id
    private UUID seasonId;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Serie> series;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    @Enumerated(EnumType.STRING)
    private SeasonStatus status;

}
