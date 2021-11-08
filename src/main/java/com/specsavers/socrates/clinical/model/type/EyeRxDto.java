package com.specsavers.socrates.clinical.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class EyeRxDto {
    private String sphere;
    private String balSphere;
    private String cylinder;
    private Float axis;
    private String distanceVisualAcuity;
    private String nearVisualAcuity;
    private String visualAcuity;
    private Float pupillaryDistance;
    private Float nearAddition;
    private Float interAddition;
    private Float addition;
    private PrismDto distancePrism;
    private PrismDto nearPrism;
    private PrismDto prism;
}
