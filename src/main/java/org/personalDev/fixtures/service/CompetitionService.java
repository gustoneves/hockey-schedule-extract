package org.personalDev.fixtures.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.personalDev.fixtures.domain.Competition;
import org.personalDev.fixtures.domain.Team;
import org.personalDev.fixtures.dto.CreateCompetitionRequest;
import org.personalDev.fixtures.repositories.CompetitionRepository;

import java.util.List;
import java.util.UUID;

@RequestScoped
public class CompetitionService {

    @Inject
    CompetitionRepository competitionRepository;

    @Inject
    TeamService teamService;

    public List<Competition> list(UUID seasonId) {
        if (seasonId == null)
            return competitionRepository.listAll();

        return competitionRepository.find("season.id", seasonId).list();
    }

    public Competition getById(UUID id) {
        return competitionRepository.findById(id);
    }

    public void createCompetition(CreateCompetitionRequest request) {
        Team team = teamService.findTeamById(request.getTeamId());

        Competition competition = Competition.builder()
                .id(UUID.randomUUID())
                .team(team)
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

        if (competition == null) {
            throw new NotFoundException("Competition not found");
        }

        Team team = teamService.findTeamById(request.getTeamId());

        competition.setTeam(team);
        competition.setName(request.getName());
        competition.setStartDate(request.getStartDate());
        competition.setEndDate(request.getEndDate());
        competition.setMinBirthDate(request.getMinBirthDate());
        competition.setMaxBirthDate(request.getMaxBirthDate());

        competitionRepository.persistAndFlush(competition);
    }
}
