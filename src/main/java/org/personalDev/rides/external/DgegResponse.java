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
public class DgegResponse {

    @JsonbProperty("resultado")
    private ResponseDetail detail;

}
