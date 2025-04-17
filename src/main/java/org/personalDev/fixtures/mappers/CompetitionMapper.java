package org.personalDev.fixtures.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.personalDev.fixtures.domain.Competition;
import org.personalDev.fixtures.dto.CompetitionResponse;

@Mapper(componentModel = "cdi")
public interface CompetitionMapper extends BaseMapper<Competition, CompetitionResponse> {

    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.name")
    CompetitionResponse toResponse(Competition competition);
}
