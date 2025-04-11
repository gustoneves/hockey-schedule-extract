package org.personalDev.fixtures.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.personalDev.fixtures.domain.Season;
import org.personalDev.fixtures.dto.SeasonResponse;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface SeasonMapper {

    @Mapping(target = "id", source = "seasonId")
    SeasonResponse toDto(Season season);

    List<SeasonResponse> toDto(List<Season> seasons);
}
