package org.personalDev.rides.service;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RegisterForReflection
public class RidesSummary {


    public Long count;
    private String name;
    private Double outgoing;
    private Double returning;
    private Double total;
    private Long numberOfRides;
}
