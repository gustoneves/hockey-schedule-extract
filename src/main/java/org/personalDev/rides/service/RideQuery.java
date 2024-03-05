package org.personalDev.rides.service;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.ws.rs.QueryParam;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideQuery {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
