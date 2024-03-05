package org.personalDev.rides.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideDTO {

    private String passenger;
    private Double outgoingCost;
    private Double returningCost;
    private LocalDate date;


    public Double getTotalCost() {
        return outgoingCost+returningCost;
    }
}
