package org.personalDev.fixtures.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AssociationStrategy {

    @Inject
    FppFixtureService fppFixtureService;

    @Inject
    ApMinhoService apMinhoService;


    public AssociationFixtureService getService(AssociationEnum association) {
        return switch (association) {
            case FPP -> fppFixtureService;
            case AP_MINHO -> apMinhoService;
        };
    }

}
