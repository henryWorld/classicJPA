package com.specsavers.socrates.clinical.types;

import lombok.Data;

@Data
public class PrescribedEyeRX {
    private String sphere;
    private String cylinder;
    private Integer axis;
    private String distanceVisualAcuity;
    private String nearVisualAcuity;
    private Float pupillaryDistance;
    private Float nearAddition;
    private Float interAddtion;
    private Prism distancePrism;
    private Prism nearPrism;
}
