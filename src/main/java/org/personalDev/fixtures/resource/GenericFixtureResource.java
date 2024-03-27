package org.personalDev.fixtures.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.personalDev.fixtures.domain.Fixture;
import org.personalDev.fixtures.service.AssociationEnum;
import org.personalDev.fixtures.service.AssociationStrategy;
import org.personalDev.fixtures.external.GoogleCloudService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class GenericFixtureResource {

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
    public Response getFixturesCSV(@PathParam("association") AssociationEnum association,@PathParam("season") String season, @PathParam("championship") String championship) throws IOException, GeneralSecurityException {
        List<Fixture> matches = associationStrategy.getService(association)
                .getFixtures(season, championship);

        googleCloudService.printFixturesToSheets(matches);
        googleCloudService.createCalendarEvents(matches);

        return Response.ok(matches).build();
    }
}

