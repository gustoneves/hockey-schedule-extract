package org.personalDev.rides.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Passenger extends PanacheEntity {

    @Column
    private String name;
}
