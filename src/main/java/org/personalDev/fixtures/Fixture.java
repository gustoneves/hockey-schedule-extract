package org.personalDev.fixtures;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Fixture {

    @Id
    private UUID id;

    @Column
    private LocalDateTime date;

    @Column
    private String homeTeam;
    @Column
    private int homeGoals;
    @Column
    private String awayTeam;
    @Column
    private int awayGoals;
    @Column
    private int matchDay;

    @Column
    private String seriesName;

}
