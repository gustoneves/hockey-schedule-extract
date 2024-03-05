package org.personalDev.rides.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.json.bind.annotation.JsonbProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripPassenger {

    @JsonbProperty("name")
    private String passenger;
    private Boolean roundTrip = true;
}
