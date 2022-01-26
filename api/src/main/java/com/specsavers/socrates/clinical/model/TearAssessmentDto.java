package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.TearAssessmentValidation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TearAssessmentValidation
public class TearAssessmentDto {
    private String observations;
    private TearAssessmentEyeDto rightEye;
    private TearAssessmentEyeDto leftEye;
}