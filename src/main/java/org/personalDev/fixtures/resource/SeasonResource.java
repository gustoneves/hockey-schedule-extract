package org.personalDev.fixtures.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.personalDev.fixtures.dto.CreateSeasonRequest;
import org.personalDev.fixtures.dto.SeasonResponse;
import org.personalDev.fixtures.mappers.SeasonMapper;
import org.personalDev.fixtures.service.SeasonService;

import java.util.List;
import java.util.UUID;

@Path("seasons")
@Transactional
public class SeasonResource {

    @Inject
    SeasonService seasonService;

    @Inject
    SeasonMapper seasonMapper;

    @GET
    public Response listSeasons() {
        List<SeasonResponse> seasons = seasonMapper.toDto(seasonService.listSeasons());
        return Response.ok(seasons).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id")UUID id) {
        var season = seasonMapper.toDto(seasonService.getSeason(id));
        return Response.ok(season).build();
    }

    @POST
    public Response createSeason(CreateSeasonRequest request) {
        seasonService.createSeason(request);
        return Response.ok().build();
    }

    @PUT
    @Path("{seasonId}")
    public Response updateSeason(@PathParam("seasonId")UUID seasonId, CreateSeasonRequest request) {
        seasonService.updateSeason(seasonId, request);
        return Response.accepted().build();
    }

    @DELETE
    @Path("{seasonId}")
    public Response deleteSeason(@PathParam("seasonId") UUID seasonId) {
        seasonService.deleteSeason(seasonId);
        return Response.noContent().build();
    }

}
