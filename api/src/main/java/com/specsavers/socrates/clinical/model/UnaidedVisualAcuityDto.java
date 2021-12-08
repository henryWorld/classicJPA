package com.specsavers.socrates.clinical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class UnaidedVisualAcuityDto {
    private String rightEye;
    private String leftEye;
    private String binocular;
}
