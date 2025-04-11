package org.personalDev.fixtures.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.personalDev.fixtures.domain.Competition;

import java.util.UUID;

@ApplicationScoped
public class CompetitionRepository implements PanacheRepositoryBase<Competition, UUID> {
}
