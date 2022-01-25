package com.specsavers.socrates.clinical.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TearAssessmentDto {
    private String observations;
    private TearAssessmentEyeDto rightEye;
    private TearAssessmentEyeDto leftEye;
}