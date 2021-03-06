package com.specsavers.socrates.clinical.legacy.model.rx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EyeRX {
    private String sphere;
    private String cylinder;
    private Float axis;
    private String distanceVisualAcuity;
    private String nearVisualAcuity;
    private Float pupillaryDistance;
    private Float nearAddition;
    private Float interAddition;
    private Prism distancePrism;
    private Prism nearPrism;
}
