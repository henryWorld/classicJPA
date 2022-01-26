package com.specsavers.socrates.clinical.model;

import lombok.*;

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