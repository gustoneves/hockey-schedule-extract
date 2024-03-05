package org.personalDev.rides.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.personalDev.rides.entities.Passenger;
import org.personalDev.rides.entities.Ride;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PassengerRepository implements PanacheRepository<Passenger> {
}
