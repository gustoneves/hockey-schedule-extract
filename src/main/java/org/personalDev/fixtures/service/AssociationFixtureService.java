package org.personalDev.fixtures.service;

import org.personalDev.fixtures.Fixture;

import java.util.List;

public interface AssociationFixtureService {
    List<Fixture> getFixtures(String season, String championship);
}
