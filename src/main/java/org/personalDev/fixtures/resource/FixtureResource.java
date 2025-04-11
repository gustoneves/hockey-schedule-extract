package org.personalDev.fixtures.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.personalDev.fixtures.domain.Fixture;
import org.personalDev.fixtures.external.GoogleCloudService;
import org.personalDev.fixtures.service.AssociationEnum;
import org.personalDev.fixtures.service.AssociationStrategy;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class FixtureResource {

    @Inject
    AssociationStrategy associationStrategy;

    @Inject
    GoogleCloudService googleCloudService;

    @GET
    @Path("{association}/seasons/{seasonId}/series/{serieId}/")
    public Response getFixtures(@PathParam("association") AssociationEnum association,
                                @PathParam("seasonId") String season,
                                @PathParam("serieId") String serieId) {

        var service = associationStrategy.getService(association);

        var matches = service.getFixtures(season, serieId);

        return Response.ok(matches).build();
    }

    @GET
    @Path("{association}/season/{season}/championship/{championship}/csv")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFixturesCSV(@PathParam("association") AssociationEnum association,
                                   @PathParam("season") String season,
                                   @PathParam("championship") String championship,
                                   @QueryParam("sheetRange") String sheetRange
    ) throws IOException, GeneralSecurityException {
        List<Fixture> matches = associationStrategy.getService(association)
                .getFixtures(season, championship);

        String range = sheetRange != null ? sheetRange :   "Teste!A1:Z1000";
        googleCloudService.printFixturesToSheets(matches, range);
        googleCloudService.createCalendarEvents(matches);

        return Response.ok(matches).build();
    }
}

