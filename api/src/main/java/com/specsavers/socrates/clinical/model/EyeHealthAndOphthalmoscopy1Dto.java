package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.EyeHealthAndOphthalmoscopy1Validation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EyeHealthAndOphthalmoscopy1Validation
public class EyeHealthAndOphthalmoscopy1Dto {
    Boolean direct;
    Boolean indirect;
    Boolean volk;
    Boolean dilated;
    Boolean slitLamp;
    String externalEyeRight;
    String anteriorChamberRight;
    String externalEyeLeft;
    String anteriorChamberLeft;
    EyeHealthDrugInfoDto drugInfoEyeHealth;
}
