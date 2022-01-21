package com.specsavers.socrates.clinical.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TearAssessmentInputDto {
    private String observations;
    private TearAssessmentEyeInputDto rightEye;
    private TearAssessmentEyeInputDto leftEye;
}