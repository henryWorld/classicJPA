package com.specsavers.socrates.clinical.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PrescribedEyeRxDto {
    private String sphere;
    private String cylinder;
    private Float axis;
    private String distanceVisualAcuity;
    private String nearVisualAcuity;
    private String visualAcuity;
    private Float pupillaryDistance;
    private Float nearAddition;
    private Float interAddtion;
    private Float addition;
    private PrismDto distancePrism;
    private PrismDto nearPrism;
    private PrismDto prism;
}
