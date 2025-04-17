package org.personalDev.fixtures.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.personalDev.fixtures.domain.Athlete;
import org.personalDev.fixtures.dto.AthleteResponse;
import org.personalDev.fixtures.dto.CreateAthleteRequest;
import org.personalDev.fixtures.dto.ShortAthleteResponse;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface AthleteMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "jersey", source = "jersey")
    @Mapping(target = "license", source = "license")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "parentName", source = "parentName")
    @Mapping(target = "parentEmail", source = "parentEmail")
    @Mapping(target = "parentPhone", source = "parentPhone")
    @Mapping(target = "photo", source = "photo")
    AthleteResponse toDto(Athlete athlete);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "jersey", source = "jersey")
    @Mapping(target = "license", source = "license")
    @Mapping(target = "birthDate", source = "birthDate")
    ShortAthleteResponse toShortDto(Athlete athlete);


    List<AthleteResponse> toDto(List<Athlete> athletes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "jersey", source = "jersey")
    @Mapping(target = "license", source = "license")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "parentName", source = "parentName")
    @Mapping(target = "parentEmail", source = "parentEmail")
    @Mapping(target = "parentPhone", source = "parentPhone")
    @Mapping(target = "photo", ignore = true)
    Athlete toDomain(CreateAthleteRequest request);
}
