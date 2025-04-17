package org.personalDev.fixtures.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.personalDev.fixtures.domain.Team;
import org.personalDev.fixtures.dto.CreateTeamRequest;
import org.personalDev.fixtures.mappers.TeamMapper;
import org.personalDev.fixtures.service.TeamService;

import java.util.List;
import java.util.UUID;

@Transactional
@Path("teams")
public class TeamResource {

    @Inject
    TeamService teamService;

    @Inject
    TeamMapper teamMapper;

    @POST
    public Response createTeam(CreateTeamRequest request) {
        teamService.createTeam(request);
        return Response.noContent().build();
    }

    @GET
    @Path("{id}")
    public Response getTeam(@PathParam("id") UUID id) {
        Team team = teamService.findTeamById(id);
        return Response.ok(teamMapper.toDto(team)).build();
    }


    @GET
    public Response getTeams() {
        List<Team> teams = teamService.getTeams();
        return Response.ok(teamMapper.toDto(teams)).build();
    }

    @PUT
    @Path("{id}")
    public Response updateTeam(@PathParam("id") UUID id, CreateTeamRequest request) {
        teamService.updateTeam(id, request);
        return Response.accepted().build();
    }
}
