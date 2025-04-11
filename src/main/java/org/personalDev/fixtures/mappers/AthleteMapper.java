package org.personalDev.fixtures.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.personalDev.fixtures.domain.Athlete;
import org.personalDev.fixtures.dto.AthleteResponse;
import org.personalDev.fixtures.dto.CreateAthleteRequest;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AthleteMapper {

    public AthleteResponse toDto(Athlete athlete) {
        if (athlete == null) {
            return null;
        }

        return AthleteResponse.builder()
                .id(athlete.getId())
                .name(athlete.getName())
                .jersey(athlete.getJersey())
                .license(athlete.getLicense())
                .birthDate(athlete.getBirthDate())
                .phone(athlete.getPhone())
                .email(athlete.getEmail())
                .parentName(athlete.getParentName())
                .parentEmail(athlete.getParentEmail())
                .parentPhone(athlete.getParentPhone())
                .photo(athlete.getPhoto())
                .build();
    }

    public List<AthleteResponse> toDto(List<Athlete> athletes) {
        if (athletes == null) {
            return null;
        }

        return athletes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Athlete toDomain(CreateAthleteRequest request) {
        if (request == null) {
            return null;
        }

        return Athlete.builder()
                .name(request.getName())
                .jersey(request.getJersey())
                .license(request.getLicense())
                .birthDate(request.getBirthDate())
                .phone(request.getPhone())
                .email(request.getEmail())
                .parentName(request.getParentName())
                .parentEmail(request.getParentEmail())
                .parentPhone(request.getParentPhone())
                .build();
    }
}
