package org.personalDev.fixtures.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import org.personalDev.fixtures.domain.Team;

import java.util.UUID;

@RequestScoped
public class TeamRepository implements PanacheRepositoryBase<Team, UUID> {
}
