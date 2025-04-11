package org.personalDev.fixtures.external;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "fpp")
public interface FppClient {

    @GET
    @Consumes("application/x-www-form-urlencoded")
    @Path("Competicao/{competicaoId}")
    Response getCompetition(@PathParam("competicaoId") String competitionId);

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("calendario.php")
    Response getFixtures(@QueryParam("epoca") String season, @FormParam("competicaoID") String championship);


    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("competicao.php")
    Response getChampionships(@QueryParam("epoca") String season, @FormParam("competicaoID") String championship);
}
