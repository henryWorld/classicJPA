package com.specsavers.socrates.clinical.model.type;

import lombok.Data;

@Data
public class RefractedRxDto {
    private String distanceBinVisualAcuity;
    private UnaidedVisualAcuityDto unaidedVisualAcuity;
    private Float bvd;
    private SpecificAdditionDto specificAddition;
    private CurrentSpecsVaDto currentSpecsVA;
    private EyeRxDto rightEye;
    private EyeRxDto leftEye;
}
