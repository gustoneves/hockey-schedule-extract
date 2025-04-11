package org.personalDev.fixtures.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.personalDev.fixtures.dto.CreateCompetitionRequest;
import org.personalDev.fixtures.mappers.CompetitionMapper;
import org.personalDev.fixtures.service.CompetitionService;

import java.util.UUID;

@RequestScoped
@Transactional
@Path("competitions")
public class CompetitionResource {

    @Inject
    CompetitionMapper competitionMapper;

    @Inject
    CompetitionService competitionService;

    @GET
    public Response listCompetitions(@QueryParam("seasonId")UUID seasonId) {
        var competitionList = competitionMapper.toDto(competitionService.list(seasonId));
        return Response.ok(competitionList).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") UUID id) {
        return Response.ok(competitionMapper.toDto(competitionService.getById(id))).build();
    }

    @POST
    public Response createCompetition(CreateCompetitionRequest request) {
        competitionService.createCompetition(request);
        return Response.noContent().build();
    }

    @PUT
    @Path("{competitionId}")
    public Response updateCompetition(@PathParam("competitionId") UUID id, CreateCompetitionRequest request) {
        competitionService.updateCompetition(id, request);
        return Response.accepted().build();
    }
}
