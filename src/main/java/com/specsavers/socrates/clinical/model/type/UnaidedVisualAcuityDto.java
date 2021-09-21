package com.specsavers.socrates.clinical.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UnaidedVisualAcuityDto {
    private final String rightEye;
    private final String leftEye;
    private final String binocular;
}
