package org.personalDev.rides.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PassengerRide extends PanacheEntity {
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Ride ride;


    @Column
    private Boolean roundTrip;

    @Column
    private Double outgoingCost;

    @Column
    private Double returningCost;
}
