package org.personalDev.rides.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ride extends PanacheEntity {

    @OneToMany
    @JoinColumn(name = "ride_id")
    private List<PassengerRide> passengers;

    @Column
    private Double cost;

    @Column
    private LocalDate date;
}
