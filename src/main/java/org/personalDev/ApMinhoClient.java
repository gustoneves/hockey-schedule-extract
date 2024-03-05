package org.personalDev;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey = "apminho")
public interface ApMinhoClient {

    @GET
    Response getSeries(@QueryParam("zona") String zone, @QueryParam("epoca") String season, @QueryParam("campeonato") String championship);

    @GET
    Response getFixtures(@QueryParam("zona") String zone, @QueryParam("epoca") String season, @QueryParam("campeonato") String championship, @QueryParam("jornada") String fixtures);
}   

