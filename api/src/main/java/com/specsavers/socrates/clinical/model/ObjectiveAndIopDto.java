package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.ObjectiveAndIopValidation;

import lombok.Data;

@Data
@ObjectiveAndIopValidation
public class ObjectiveAndIopDto {
    EyeIopDto rightEye;
    EyeIopDto leftEye;
    String time;
    String notes;
    DrugInfoDto drugInfo;
}
