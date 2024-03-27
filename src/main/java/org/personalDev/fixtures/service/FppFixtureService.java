package org.personalDev.fixtures.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.personalDev.fixtures.Fixture;
import org.personalDev.fixtures.external.FppClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class FppFixtureService implements AssociationFixtureService {

    @RestClient
    @Inject
    FppClient fppClient;

    @Override
    public List<Fixture> getFixtures(String season, String championship) {
        String seriesName = getChampionshipId(season, championship);

        Response response = fppClient.getFixtures(season, championship);

        String htmlAsString = response.readEntity(String.class);
        Document doc = Jsoup.parse(htmlAsString);
        Elements elements = doc.select(".titulo-jornada");

        return fromGameWeek(elements, seriesName);

    }


    private List<Fixture> fromGameWeek(Elements elements, String seriesName) {
        Elements tableElements = Objects.requireNonNull(
                Objects.requireNonNull(
                        Objects.requireNonNull(
                                Objects.requireNonNull(elements.first())
                                        .parent()).parent()).parent()).select("tr");
        List<Fixture> fixtures = new ArrayList<>();
        int matchDay = 0;
        for (Element element : tableElements) {
            Element matchDayElement = element.select(".titulo-jornada").first();
            if (matchDayElement != null) {
                matchDay = parseMatchDay(matchDayElement.text());
            } else {
                fixtures.add(fromElement(element, seriesName, matchDay));
            }
        }
        return fixtures;
    }

    private String getChampionshipId(String season, String championship) {
        Response response = fppClient.getChampionships(season, championship);
        String htmlAsString = response.readEntity(String.class);
        Document doc = Jsoup.parse(htmlAsString);
        return Objects.requireNonNull(doc.select("h1").first()).text();
    }

    private Fixture fromElement(Element element, String seriesName, int matchDay) {
        String result = element.select(".resultado").text();
        String[] goals = result.split(" - ");
        String htmlDateTime = element.select(".coluna-data").get(1).select("p.data").text().replace(" ", "T");
        if ("-".equals(goals[0])) {
            return Fixture.builder()
                    .seriesName(seriesName)
                    .homeTeam(element.select(".visitada").text())
                    .awayTeam(element.select(".visitante").text())
                    .matchDay(matchDay)
                    .date(LocalDateTime.parse(htmlDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
        } else {
            return Fixture.builder()
                    .seriesName(seriesName)
                    .homeTeam(element.select(".visitada").text())
                    .homeGoals(Integer.parseInt(goals[0]))
                    .awayTeam(element.select(".visitante").text())
                    .awayGoals(Integer.parseInt(goals[1]))
                    .matchDay(matchDay)
                    .date(LocalDateTime.parse(htmlDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
        }


    }

    private int parseMatchDay(String matchDay) {
        return Integer.parseInt(matchDay.split("ª")[0]);
    }
}
