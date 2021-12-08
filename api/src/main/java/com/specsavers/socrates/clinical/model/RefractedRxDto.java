package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.RefractedRxValidation;

import lombok.Data;

@Data
@RefractedRxValidation
public class RefractedRxDto {
    private String distanceBinVisualAcuity;
    private UnaidedVisualAcuityDto unaidedVisualAcuity;
    private Float bvd;
    private SpecificAdditionDto specificAddition;
    private CurrentSpecsVaDto currentSpecsVA;
    private EyeRxDto rightEye;
    private EyeRxDto leftEye;
    private RxNotesDto notes;
}
