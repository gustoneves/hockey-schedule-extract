package org.personalDev.fixtures.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.personalDev.fixtures.domain.Competition;
import org.personalDev.fixtures.dto.CompetitionResponse;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface CompetitionMapper {

    @Mapping(target = "seasonId", source = "season.id")
    @Mapping(target = "seasonName", source = "season.name")
    CompetitionResponse toDto(Competition season);

    List<CompetitionResponse> toDto(List<Competition> seasons);
}
