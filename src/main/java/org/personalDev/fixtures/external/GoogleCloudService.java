package org.personalDev.fixtures.external;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.personalDev.fixtures.domain.Fixture;
import org.personalDev.fixtures.domain.TeamColors;
import org.personalDev.rides.service.RideDTO;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
public class GoogleCloudService {

    public static final String SUB_15_CALENDAR_ID = "7i8k2e1c3cihmb807gfnancgt4@group.calendar.google.com";
    public static final String SPREDSHEET_ID = "1Ib8svjEvLZyPEx5h7NMSXJr1Q5yCI9UzTGQURSn5Z4Q";

    @Inject
    @ConfigProperty(name = "relevantTeam")
    List<String> relevantTeamNames;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS, CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credential.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleCloudService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory
                        (new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void createCalendarEvents(List<Fixture> fixtures) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Calendar calendarService = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("sports-project-cart")
                .build();

        DateTime now = new DateTime(System.currentTimeMillis());
        Events existingEvents = calendarService.events().list(SUB_15_CALENDAR_ID).setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Fixture> relevantFixtures = fixtures.stream()
                .filter(this::isRelevantTeam)
                .toList();

        List<Event> events = relevantFixtures.stream().map(this::convertToEvent).toList();

        for (Event e : events) {
            if (!alreadyExists(existingEvents, e)) {
                calendarService.events().insert(SUB_15_CALENDAR_ID, e).execute();
            }
        }
    }

    private Boolean alreadyExists(Events existingEvents, Event event) {
        return existingEvents.getItems().stream().anyMatch(e -> e.getSummary().equalsIgnoreCase(event.getSummary()));
    }

    private Boolean isRelevantTeam(Fixture fixture) {
        //If home Team or away team name is contained in relevantTeamNames
        Boolean result = false;

        for (String teamName : relevantTeamNames) {
            if (fixture.getHomeTeam().contains(teamName) || fixture.getAwayTeam().contains(teamName)) {
                return true;
            }
        }

        return result;
    }

    private Event convertToEvent(Fixture fixture) {
        fixture.getDate().format(DATE_FORMATTER);
        String description = "(" + fixture.getSeriesName()
                + ") J." + fixture.getMatchDay()
                + " " + fixture.getHomeTeam()
                + " - " + fixture.getAwayTeam();

        TeamColors teamColor = getColorId(fixture);

        Event event = new Event()
                .setSummary(description)
                .setDescription(description)
                .setColorId(teamColor.getColorId());

        Date st = Date.from(fixture.getDate().atZone(ZoneId.systemDefault()).toInstant());


        EventDateTime startDate = new EventDateTime().setDateTime(new DateTime(false, st.getTime(), 1));
        startDate.setTimeZone("UTC");

        EventDateTime endDate = new EventDateTime().setDateTime(new DateTime(fixture.getDate().plusMinutes(90).format(DATE_FORMATTER)));

        event.setStart(startDate);
        event.setEnd(endDate);

        return event;
    }

    private static TeamColors getColorId(Fixture fixture) {
        if (fixture.getSeriesName().contains("15")) {
            if (fixture.getAwayTeam().equals("H.C. Braga (B)") || fixture.getHomeTeam().equals("H.C. Braga (B)")) {
                return TeamColors.SUB_15_B;
            } else {
                return TeamColors.SUB_15;
            }
        } else {
            return TeamColors.SUB_17;
        }
    }

    public void printFixturesToSheets(List<Fixture> fixtures, String sheetRange) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("sports-project-cart")
                .build();

        try {
            List<List<Object>> values = fixtures.stream()
                    .map(this::fixtureToStringList)
                    .collect(Collectors.toList());

            ValueRange valueRange = new ValueRange().setValues(values);

            sheetsService.spreadsheets()
                    .values()
                    .update(SPREDSHEET_ID, sheetRange, valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        } catch (TokenResponseException e) {
            //delete token and try again
            Log.warn("Token expired, deleting token and trying again", e);
            deleteToken();
            throw e;
        }

    }

    private List<Object> fixtureToStringList(Fixture fixture) {
        List<Object> result;

        if (fixture.getDate().isBefore(LocalDateTime.now())) {
            result = Arrays.asList(String.valueOf(fixture.getMatchDay()),
                    fixture.getDate().toString(), fixture.getHomeTeam(), String.valueOf(fixture.getHomeGoals()), String.valueOf(fixture.getAwayGoals()), fixture.getAwayTeam());
        } else {
            result = Arrays.asList(String.valueOf(fixture.getMatchDay()),
                    fixture.getDate().toString(), fixture.getHomeTeam(), "", "", fixture.getAwayTeam());
        }

        return result;
    }

    @Retry(delay = 1000, retryOn = TokenResponseException.class)
    public void printRideToSheet(List<RideDTO> rideList) throws GeneralSecurityException, IOException {
        List<Request> requests = new ArrayList<>();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("sports-project-cart")
                .build();

        // Get the spreadsheet
        try {
            Spreadsheet spreadsheet = sheetsService.spreadsheets().get(SPREDSHEET_ID).execute();

            Integer sheetId = getSheetIdByName(spreadsheet, "Viagens");


            // Data rows
            for (int i = 0; i < rideList.size(); i++) {
                RideDTO ride = rideList.get(i);

                requests.add(new Request()
                        .setUpdateCells(new UpdateCellsRequest()
                                .setStart(new GridCoordinate()
                                        .setSheetId(sheetId) // Assuming the first sheet
                                        .setRowIndex(i) // Skip header row
                                        .setColumnIndex(0) // Assuming the first column
                                )
                                .setRows(Collections.singletonList(
                                        new RowData().setValues(Arrays.asList(new CellData().setUserEnteredValue(
                                                        new ExtendedValue().setStringValue(String.valueOf(ride.getDate()))),
                                                new CellData().setUserEnteredValue(
                                                        new ExtendedValue().setNumberValue(Double.valueOf(String.valueOf(ride.getTotalCost().doubleValue())))
                                                )
                                        ))))
                                .setFields("userEnteredValue.stringValue,userEnteredFormat.numberFormat")
                        ));
            }

            requests.add(new Request()
                    .setUpdateCells(new UpdateCellsRequest()
                            .setStart(new GridCoordinate()
                                    .setSheetId(sheetId) // Assuming the first sheet
                                    .setRowIndex(rideList.size()) // Skip header row
                                    .setColumnIndex(0) // Assuming the first column
                            )
                            .setRows(Collections.singletonList(
                                    new RowData().setValues(Arrays.asList(new CellData().setUserEnteredValue(
                                                            new ExtendedValue().setStringValue("Total")),
                                                    new CellData().setUserEnteredValue(
                                                            new ExtendedValue().setFormulaValue("=Sum(B1:B" + rideList.size() + ")"))
                                            )
                                    )))
                            .setFields("userEnteredValue.stringValue")
                    ));


            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);

            sheetsService.spreadsheets()
                    .batchUpdate(SPREDSHEET_ID, batchUpdateRequest)
                    .execute();
        } catch (TokenResponseException e) {
            //delete token and try again
            Log.warn("Token expired, deleting token and trying again", e);
            deleteToken();
            throw e;
        }

    }

    private void deleteToken() {
        //delete token from dir tokens
        File file = new File(TOKENS_DIRECTORY_PATH + "/StoredCredential");
        file.delete();
    }

    private static Integer getSheetIdByName(Spreadsheet spreadsheet, String sheetName) {
        List<Sheet> sheets = spreadsheet.getSheets();

        for (Sheet sheet : sheets) {
            if (sheet.getProperties().getTitle().equals(sheetName)) {
                return sheet.getProperties().getSheetId();
            }
        }

        return null; // Sheet not found
    }

}
