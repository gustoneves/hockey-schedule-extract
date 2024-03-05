package org.personalDev;

import io.quarkus.logging.Log;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("seasons/{seasonId}/series/{serieId}/")
public class FixturesResource {

    @Inject
    @RestClient
    ApMinhoClient apMinhoClient;

    @Inject
    GoogleCloudService calendarService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFixturesForSeasonAndSeries(@PathParam("seasonId")String season, @PathParam("serieId")String serieId) throws IOException, GeneralSecurityException {

        List<List<Fixture>> matches = getFixturesList(season, serieId);
        return Response.ok(matches).build();
    }

    private List<List<Fixture>> getFixturesList(String season, String serieId) throws IOException, GeneralSecurityException {
        List<String> matchDays = numberOfMatchDays(season, serieId);
        String seriesName = getSeriesName(season,serieId);

        List<List<Fixture>> fixtures =IntStream
                .range(0, matchDays.size())
                .mapToObj(i -> fixtures(matchDays.get(i), i+1, seriesName))
                .collect(Collectors.toList());

        calendarService.printFixturesToSheets(fixtures);
        calendarService.createCalendarEvents(fixtures);

        return fixtures;

    }


    @Path("csv")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFixturesForSeasonAndSeriesHasCSV(@PathParam("seasonId")String season, @PathParam("serieId")String serieId) throws IOException, GeneralSecurityException {

        List<List<Fixture>> matches = getFixturesList(season, serieId);
        String[] headerList = {"Jornada","Data","Casa", "Golos Casa", "Golos Fora", "Fora"};

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CSVFormat format = CSVFormat.DEFAULT.withHeader(headerList);
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);

        var printableList = matches.stream().flatMap(List::stream).toList().stream().map(Fixture::printToList).toList();

        printableList.forEach(it -> {
            try {
                csvPrinter.printRecord(it);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        csvPrinter.flush();

        return Response.ok(new ByteArrayInputStream(out.toByteArray()),MediaType.TEXT_PLAIN).build();
    }

    private List<String> numberOfMatchDays(String season, String serie) {
        Response response = apMinhoClient.getSeries("epocas", season, serie);
        List<String> result = new ArrayList<>();

        String htmlAsString = response.readEntity(String.class);
        Document doc = Jsoup.parse(htmlAsString);
        Elements links = doc.getElementById("conteudo").select("a");

        int matchDays = links.size() - 1;

        for(int i = 0; i<matchDays; i++) {
            result.add(links.get(i).attr("href").split("jornada=")[1]);
        }

        Log.info("Championship has " + matchDays + " match days");

        return result;
    }

    private List<Fixture> fixtures(String matchDayId, int matchday, String seriesName) {
        Response response = apMinhoClient.getFixtures("epocas", "1058", "1091", matchDayId);
        List<Fixture> fixtures = new ArrayList<>();

        String htmlAsString = response.readEntity(String.class);
        Document doc = Jsoup.parse(htmlAsString);
        Elements lines = doc.select("table").select("tr");


        for (int i = 1; i< lines.size(); i++) {
            Element elem = lines.get(i);
            fixtures.add(createFixtureFromElement(elem, matchday, seriesName));
        }


        return fixtures.stream().filter( f -> !f.getAwayTeam().equalsIgnoreCase("Folga")
                &&!f.getAwayTeam().equals("--")
                &&!f.getHomeTeam().equalsIgnoreCase("Folga")
                &&!f.getHomeTeam().equals("--")).collect(Collectors.toList());
    }

    private Fixture createFixtureFromElement(Element elem, int matchDay, String seriesName) {
        Fixture fixture = new Fixture();

        Elements children = elem.children();

        List<String> dateSplit = Arrays.asList(children.get(1).text().split("-"));
        Collections.reverse(dateSplit);
        fixture.setMatchDay(matchDay);
        LocalDateTime time = LocalDateTime.of(LocalDate.of(Integer.parseInt(dateSplit.get(0)), Integer.parseInt(dateSplit.get(1)), Integer.parseInt(dateSplit.get(2))),
                LocalTime.parse(children.get(2).text()));
        fixture.setDate(time);
        fixture.setHomeTeam(children.get(3).text());
        fixture.setHomeGoals(Integer.parseInt(children.get(4).text()));
        fixture.setAwayTeam(children.get(6).text());
        fixture.setAwayGoals(Integer.parseInt(children.get(5).text()));
        fixture.setSeriesName(seriesName);

        return fixture;
    }

    private String getSeriesName(String season, String serie) {
        Response response = apMinhoClient.getSeries("epocas", season, serie);
        List<String> result = new ArrayList<>();

        String htmlAsString = response.readEntity(String.class);
        Document doc = Jsoup.parse(htmlAsString);
        Elements links = doc.getElementById("conteudo").select("a");

        return doc.getElementById("conteudo").select("h2").text();
    }

}
