package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.EyeHealthAndOphthalmoscopy2Validation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@EyeHealthAndOphthalmoscopy2Validation
public class EyeHealthAndOphthalmoscopy2Dto {
    String lensRight;
    String vitreousRight;
    String lensLeft;
    String vitreousLeft;
}
