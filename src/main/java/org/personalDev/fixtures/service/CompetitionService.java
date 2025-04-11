package org.personalDev.fixtures.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.personalDev.fixtures.domain.Competition;
import org.personalDev.fixtures.domain.Season;
import org.personalDev.fixtures.dto.CreateCompetitionRequest;
import org.personalDev.fixtures.repositories.CompetitionRepository;

import java.util.List;
import java.util.UUID;

@RequestScoped
public class CompetitionService {

    @Inject
    CompetitionRepository competitionRepository;

    @Inject
    SeasonService seasonService;

    public List<Competition> list(UUID seasonId) {
        if(seasonId == null)
            return competitionRepository.listAll();

        return competitionRepository.find("season.id", seasonId).list();
    }

    public Competition getById(UUID id) {
        return competitionRepository.findById(id);
    }

    public void createCompetition(CreateCompetitionRequest request) {
        Season season =  seasonService.getSeason(request.getSeasonId());

        if(season == null) {
            throw new NotFoundException("Season not found");
        }

        Competition competition = Competition.builder()
                .id(UUID.randomUUID())
                .season(season)
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .minBirthDate(request.getMinBirthDate())
                .maxBirthDate(request.getMaxBirthDate())
                .build();

        competitionRepository.persistAndFlush(competition);
    }

    public void updateCompetition(UUID id, CreateCompetitionRequest request) {
        Competition competition = competitionRepository.findById(id);

        if(competition == null) {
            throw new NotFoundException("Competition not found");
        }

        Season season =  seasonService.getSeason(request.getSeasonId());

        if(season == null) {
            throw new NotFoundException("Season not found");
        }

        competition.setSeason(season);
        competition.setName(request.getName());
        competition.setStartDate(request.getStartDate());
        competition.setEndDate(request.getEndDate());
        competition.setMinBirthDate(request.getMinBirthDate());
        competition.setMaxBirthDate(request.getMaxBirthDate());

        competitionRepository.persistAndFlush(competition);
    }
}
