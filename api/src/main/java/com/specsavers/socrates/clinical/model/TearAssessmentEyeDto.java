package com.specsavers.socrates.clinical.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class TearAssessmentEyeDto {
    private String tbut;
    private String prism;
    private String scope;
    private String schirmer;
}