package org.personalDev.rides.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.personalDev.fixtures.external.GoogleCloudService;
import org.personalDev.rides.entities.Passenger;
import org.personalDev.rides.entities.PassengerRide;
import org.personalDev.rides.entities.Ride;
import org.personalDev.rides.external.DgegClient;
import org.personalDev.rides.external.DgegResponse;
import org.personalDev.rides.external.FuelPrice;
import org.personalDev.rides.repository.PassengerRepository;
import org.personalDev.rides.repository.PassengerRideRepository;
import org.personalDev.rides.repository.RideRepository;
import org.personalDev.rides.resource.TripDTO;
import org.personalDev.rides.resource.TripPassenger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class RideService {

    @Inject
    PassengerRepository passengerRepository;

    @Inject
    RideRepository rideRepository;

    @Inject
    PassengerRideRepository passengerRideRepository;

    @RestClient
    @Inject
    DgegClient dgegClient;

    @ConfigProperty(name = "oneWayDistance")
    Long oneWayDistance;

    @ConfigProperty(name = "carConsumption")
    Double carConsumption;

    @Inject
    GoogleCloudService googleCloudService;

    public void createTrip(TripDTO trip) {

        List<PassengerRide> details = getListOfPassenger(trip);

        Double price = getFuelPrice();

        Ride ride = Ride.builder()
                .date(trip.getDate())
                .passengers(details)
                .cost(price)
                .build();


        rideRepository.persist(ride);
    }

    public RideSearchResult getRidesSummary(RideQuery rideQuery, boolean printToSheet) throws GeneralSecurityException, IOException {

        List<RideDTO> rides = passengerRideRepository.searchRides(rideQuery);
        rides.sort(Comparator.comparing(RideDTO::getDate));

        if(printToSheet) {
            googleCloudService.printRideToSheet(rides);
        }

        return RideSearchResult
                .builder()
                .summaries(passengerRideRepository.getRidesSummary(rideQuery))
                .rides(rides)
                .build();
    }

    private List<PassengerRide> getListOfPassenger(TripDTO trip) {
        Double oneWayCost = calculateOneWayCost();

        Double outgoingCostPerPassenger = calculateOneWayCostPerPassenger(trip, oneWayCost);
        Double returnCostPerPassenger = calculateReturnWayCostPerPassenger(trip, oneWayCost);

        return trip.getTripPassengers()
                .stream()
                .map( p -> {
                     PassengerRide pr = PassengerRide.builder()
                            .passenger(getOrCreatePassenger(p))
                            .roundTrip(p.getRoundTrip())
                            .outgoingCost(outgoingCostPerPassenger)
                            .returningCost(p.getRoundTrip() ? returnCostPerPassenger : 0)
                            .build();
                     pr.persist();
                     return pr;
                })
                .collect(Collectors.toList());

    }
    private Passenger getOrCreatePassenger(TripPassenger tp) {

        Optional<Passenger> savedPassenger = passengerRepository.find("name = ?1", tp.getPassenger()).firstResultOptional();

        if(savedPassenger.isPresent()) {
            return savedPassenger.get();
        } else {
            Passenger passenger = Passenger.builder().name(tp.getPassenger()).build();
            passenger.persist();
            return passenger;
        }

    }


    private Double calculateOneWayCostPerPassenger(TripDTO trip, Double totalCost) {

        return totalCost / trip.getTripPassengers().size();
    }

    private Double calculateReturnWayCostPerPassenger(TripDTO trip, Double totalCost) {
        long returnPassengersCount = trip.getTripPassengers().stream().filter(TripPassenger::getRoundTrip).count();

        return totalCost / returnPassengersCount;
    }

    private Double calculateOneWayCost() {
        Double fuelPerLiter = getFuelPrice();

        Double oneWayFuelConsumption = carConsumption * oneWayDistance / 100;

        return oneWayFuelConsumption * fuelPerLiter;
    }

    private Double getFuelPrice() {
        DgegResponse priceList = dgegClient.getLiterPrice();

        Optional<FuelPrice> price = priceList.getDetail().getPrices().stream().filter(p -> p.getType().equalsIgnoreCase("Gasóleo especial")).findFirst();

        return Double.parseDouble(price.get().getPrice().replace( " €/litro", "").replace(",", "."));
    }
}
