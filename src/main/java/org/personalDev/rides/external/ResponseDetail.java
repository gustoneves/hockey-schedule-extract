package org.personalDev.rides.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDetail {

    @JsonbProperty("Combustiveis")
    private List<FuelPrice> prices;
}
