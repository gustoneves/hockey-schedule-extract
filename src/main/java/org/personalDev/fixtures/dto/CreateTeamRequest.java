package org.personalDev.fixtures.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.personalDev.fixtures.domain.Team;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequest {

    private UUID seasonId;
    private Set<UUID> athleteIds;
    private String name;
    private Team.Status status;
}
