package org.personalDev.fixtures.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.personalDev.fixtures.domain.Team;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamResponse {
    private UUID id;
    private String name;
    private Set<ShortAthleteResponse> athletes;
    private SeasonResponse season;
    private Team.Status status;
}
