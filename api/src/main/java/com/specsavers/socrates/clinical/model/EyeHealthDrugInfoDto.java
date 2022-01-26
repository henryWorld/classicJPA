package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.EyeHealthDrugInfoValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EyeHealthDrugInfoValidation
public class EyeHealthDrugInfoDto {
    DrugInfoDto drugInfo;
    String prePressure;
    String prePressureTime;
    String postPressure;
    String postPressureTime;
}
