package org.personalDev.fixtures.resource;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.personalDev.fixtures.dto.AthleteResponse;
import org.personalDev.fixtures.dto.CreateAthleteRequest;
import org.personalDev.fixtures.mappers.AthleteMapper;
import org.personalDev.fixtures.service.AthleteService;

import java.util.List;
import java.util.UUID;

@Path("athletes")
@RequestScoped
@Transactional
public class AthleteResource {

    @Inject
    AthleteService athleteService;

    @Inject
    AthleteMapper athleteMapper;

    @GET
    public Response listAthletes() {
        List<AthleteResponse> athletes = athleteMapper.toDto(athleteService.listAthletes());
        return Response.ok(athletes).build();
    }

    @GET
    @Path("{id}")
    public Response getAthlete(@PathParam("id") UUID id) {
        AthleteResponse athlete = athleteMapper.toDto(athleteService.getAthlete(id));
        return Response.ok(athlete).build();
    }

    @GET
    @Path("{id}/photo")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getAthletePhoto(@PathParam("id") UUID id) {
        try {
            byte[] photo = athleteService.getAthletePhoto(id);
            Log.info("Sending photo of size: " + (photo != null ? photo.length : 0) + " bytes");
            return Response.ok(photo)
                    .type("image/jpeg")
                    .header("Content-Disposition", "inline; filename=\"athlete-photo.jpg\"")
                    .build();
        } catch (Exception e) {
            Log.error("Error retrieving photo: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving photo: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response createAthlete(CreateAthleteRequest request) {
        athleteService.createAthlete(request);
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    public Response updateAthlete(@PathParam("id") UUID id, CreateAthleteRequest request) {
        athleteService.updateAthlete(id, request);
        return Response.accepted().build();
    }

    @PUT
    @Path("{id}/photo")
    @Consumes("image/jpeg")
    public Response updateAthletePhoto(@PathParam("id") UUID id, byte[] photo) {
        try {
            if (photo == null || photo.length == 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Photo data is empty")
                        .build();
            }

            Log.info("Received photo of size: " + photo.length + " bytes");
            athleteService.updateAthletePhoto(id, photo);
            return Response.accepted().build();
        } catch (Exception e) {
            Log.error("Error updating photo: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating photo: " + e.getMessage())
                    .build();
        }
    }
}
