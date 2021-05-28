package com.specsavers.socrates.clinical.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnaidedVA {
    private String rightEye;
    private String leftEye;
    private String binocular;
}
