package org.personalDev.fixtures.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.personalDev.fixtures.domain.Athlete;
import org.personalDev.fixtures.dto.CreateAthleteRequest;
import org.personalDev.fixtures.mappers.AthleteMapper;
import org.personalDev.fixtures.repositories.AthleteRepository;

import java.util.List;
import java.util.UUID;

@RequestScoped
public class AthleteService {

    @Inject
    AthleteRepository athleteRepository;

    @Inject
    AthleteMapper athleteMapper;

    public List<Athlete> listAthletes() {
        return athleteRepository.find("ORDER BY jersey").list();
    }

    public Athlete getAthlete(UUID id) {
        Athlete athlete = athleteRepository.findById(id);
        if (athlete == null) {
            throw new NotFoundException("Athlete not found");
        }
        return athlete;
    }

    public byte[] getAthletePhoto(UUID id) {
        Athlete athlete = athleteRepository.findById(id);
        if (athlete == null) {
            throw new NotFoundException("Athlete not found");
        }
        if (athlete.getPhoto() == null) {
            throw new NotFoundException("Photo not found");
        }
        Log.info("Retrieved photo of size: " + (athlete.getPhoto() != null ? athlete.getPhoto().length : 0) + " bytes");
        return athlete.getPhoto();
    }

    public void createAthlete(CreateAthleteRequest request) {
        Athlete athlete = athleteMapper.toDomain(request);
        athlete.setId(UUID.randomUUID());
        athleteRepository.persistAndFlush(athlete);
    }

    public void updateAthlete(UUID id, CreateAthleteRequest request) {
        Athlete athlete = athleteRepository.findById(id);
        if (athlete == null) {
            throw new NotFoundException("Athlete not found");
        }

        athlete.setName(request.getName());
        athlete.setJersey(request.getJersey());
        athlete.setLicense(request.getLicense());
        athlete.setBirthDate(request.getBirthDate());
        athlete.setPhone(request.getPhone());
        athlete.setEmail(request.getEmail());
        athlete.setParentName(request.getParentName());
        athlete.setParentEmail(request.getParentEmail());
        athlete.setParentPhone(request.getParentPhone());

        athleteRepository.persistAndFlush(athlete);
    }

    public void updateAthletePhoto(UUID id, byte[] photo) {
        if (photo == null || photo.length == 0) {
            throw new IllegalArgumentException("Photo cannot be null or empty");
        }

        Athlete athlete = athleteRepository.findById(id);
        if (athlete == null) {
            throw new NotFoundException("Athlete not found");
        }

        Log.info("Updating photo with size: " + photo.length + " bytes");
        athlete.setPhoto(photo);
        athleteRepository.persistAndFlush(athlete);

        // Verify the photo was stored correctly
        Athlete updatedAthlete = athleteRepository.findById(id);
        Log.info("Stored photo size: " + (updatedAthlete.getPhoto() != null ? updatedAthlete.getPhoto().length : 0)
                + " bytes");
    }
}