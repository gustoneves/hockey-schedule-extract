package org.personalDev.rides.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideSearchResult {

    private List<RidesSummary> summaries;
    private List<RideDTO> rides;
}
