package org.personalDev.fixtures.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.personalDev.fixtures.domain.Season;
import org.personalDev.fixtures.domain.SeasonStatus;
import org.personalDev.fixtures.dto.CreateSeasonRequest;
import org.personalDev.fixtures.repositories.SeasonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class SeasonService {

    @Inject
    SeasonRepository seasonRepository;

    public Optional<Season> getSeason(UUID seasonId) {
        return seasonRepository.findByIdOptional(seasonId);
    }

    public List<Season> listSeasons() {
        return seasonRepository.listAll();
    }

    public void createSeason(CreateSeasonRequest request) {
        Season season = Season.builder()
                .seasonId(UUID.randomUUID())
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(SeasonStatus.DRAFT)
                .build();

        seasonRepository.persistAndFlush(season);
    }

    public void updateSeason(UUID seasonId, CreateSeasonRequest request) {
        Season season = seasonRepository.findById(seasonId);

        if(season != null) {
            season.setName(request.getName());
            season.setStartDate(request.getStartDate());
            season.setEndDate(request.getEndDate());
            season.setStatus(request.getStatus());

            seasonRepository.persistAndFlush(season);
        }
    }

    public void deleteSeason(UUID seasonId) {
        Season season = seasonRepository.findById(seasonId);
        seasonRepository.delete(season);
    }
}
