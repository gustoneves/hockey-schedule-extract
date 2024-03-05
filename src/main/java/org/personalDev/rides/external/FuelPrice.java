package org.personalDev.rides.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.json.bind.annotation.JsonbProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuelPrice {
    @JsonbProperty("TipoCombustivel")
    private String type;

    @JsonbProperty("Preco")
    private String price;

}
