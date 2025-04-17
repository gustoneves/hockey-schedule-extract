package org.personalDev.fixtures.mappers;

import org.mapstruct.Mapper;
import org.personalDev.fixtures.domain.Team;
import org.personalDev.fixtures.dto.TeamResponse;

import java.util.List;

@Mapper(config = MappingConfig.class, uses = {SeasonMapper.class, AthleteMapper.class})
public interface TeamMapper {


    TeamResponse toDto(Team team);

    List<TeamResponse> toDto(List<Team> teams);
}
