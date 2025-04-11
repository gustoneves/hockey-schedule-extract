package org.personalDev.fixtures.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.personalDev.fixtures.domain.Athlete;

import java.util.UUID;

@ApplicationScoped
public class AthleteRepository implements PanacheRepositoryBase<Athlete, UUID> {
} 