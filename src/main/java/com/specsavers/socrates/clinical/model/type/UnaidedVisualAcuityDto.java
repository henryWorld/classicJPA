package com.specsavers.socrates.clinical.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public final class UnaidedVisualAcuityDto {
    private final String rightEye;
    private final String leftEye;
    private final String binocular;
}
