package org.personalDev.fixtures.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.personalDev.fixtures.domain.Athlete;
import org.personalDev.fixtures.domain.Season;
import org.personalDev.fixtures.domain.Team;
import org.personalDev.fixtures.dto.CreateTeamRequest;
import org.personalDev.fixtures.repositories.TeamRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class TeamService {

    @Inject
    SeasonService seasonService;

    @Inject
    AthleteService athleteService;

    @Inject
    TeamRepository teamRepository;

    public void createTeam(CreateTeamRequest request) {
        Season season = seasonService.getSeason(request.getSeasonId()).orElseThrow(() -> new NotFoundException("Season not found"));
        Set< Athlete> athletes = athleteService.getAthletesByIds(request.getAthleteIds());

        Team team = Team.builder()
                .id(UUID.randomUUID())
                .season(season)
                .name(request.getName())
                .athletes(athletes)
                .status(request.getStatus())
                .build();

        teamRepository.persistAndFlush(team);
    }

    public void updateTeam(UUID id, CreateTeamRequest request) {
        Team team = teamRepository.findByIdOptional(id).orElseThrow(()-> new NotFoundException("Team not found"));
        Season season = seasonService.getSeason(request.getSeasonId()).orElseThrow(() -> new NotFoundException("Season not found"));
        Set< Athlete> athletes = athleteService.getAthletesByIds(request.getAthleteIds());

        team.setName(request.getName());
        team.setSeason(season);
        team.setAthletes(athletes);
        team.setStatus(request.getStatus());

        teamRepository.persistAndFlush(team);
    }

    public Team findTeamById(UUID id) {
        return teamRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Team not found"));
    }

    public List<Team> getTeams() {
        return teamRepository.findAll().list();
    }
}
