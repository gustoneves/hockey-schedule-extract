package org.personalDev.rides.resource;

import com.opencsv.CSVWriter;
import org.personalDev.rides.service.*;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequestScoped
@Path("rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideResource {

    @Inject
    RideService rideService;

    @POST
    @Transactional
    public Response addTrip(TripDTO trip){
        rideService.createTrip(trip);
        return Response.created(URI.create("rides")).build();
    }


    @GET
    public Response getTrips(@QueryParam("startDate") LocalDate startDate,
                             @QueryParam("endDate") LocalDate endDate,
                             @QueryParam("passenger") String passenger) throws GeneralSecurityException, IOException {

        RideQuery query = RideQuery
                .builder()
                .name(passenger)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return Response.ok(rideService.getRidesSummary(query, false)).build();
    }


    @GET
    @Path("csv")
    @Produces("text/csv")
    public Response getTripsCSV(@QueryParam("startDate") LocalDate startDate,
                             @QueryParam("endDate") LocalDate endDate,
                             @QueryParam("passenger") String passenger,
                                @QueryParam("writeToCloud") Boolean writeToCloud) throws GeneralSecurityException, IOException {

        RideQuery query = RideQuery
                .builder()
                .name(passenger)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return exportAsCSV(rideService.getRidesSummary(query, writeToCloud));
    }

    private Response exportAsCSV(RideSearchResult rideSearchResult) {
        try (StringWriter stringWriter = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(stringWriter, ';', ' ', ' ', "\n")) {

            // Write the CSV header
            String[] header = {"Data", "Custo"};
            csvWriter.writeNext(header);

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00000", symbols);


            // Write DTO data to CSV
            for (RideDTO dto : rideSearchResult.getRides()) {
                String[] data = {dto.getDate().toString(), decimalFormat.format(dto.getTotalCost())};
                csvWriter.writeNext(data);
            }

            Double summary = rideSearchResult.getSummaries().stream().mapToDouble(RidesSummary::getTotal).sum();

            String[] data = {"Total", decimalFormat.format(summary)};
            csvWriter.writeNext(data);

            // Get the CSV content as a string
            String csvContent = stringWriter.toString();

            return Response
                    .ok(csvContent)
                    .header("Content-Disposition", "attachment; filename=data.csv") // Optional: Set the filename
                    .build();
        } catch (Exception e) {
            // Handle exceptions
            return Response.serverError().entity("Error generating CSV").build();
        }
    }
}
