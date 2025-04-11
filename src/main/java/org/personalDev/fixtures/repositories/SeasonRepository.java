package org.personalDev.fixtures.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import org.personalDev.fixtures.domain.Season;

import java.util.UUID;

@RequestScoped
public class SeasonRepository implements PanacheRepositoryBase<Season, UUID> {

}
