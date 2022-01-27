package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy1Dto;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class EyeHealthAndOphthalmoscopy1Validator extends Validator<EyeHealthAndOphthalmoscopy1Dto> {

    @Override
    public void validate(EyeHealthAndOphthalmoscopy1Dto item) {
        var chamberLeft = check("AnteriorChamberLeft", item.getAnteriorChamberLeft());
        chamberLeft.notBlank();
        chamberLeft.maxLength(150);

        var externalLeft = check("ExternalEyeLeft", item.getExternalEyeLeft());
        externalLeft.notBlank();
        externalLeft.maxLength(550);

        var chamberRight = check("AnteriorChamberRight", item.getAnteriorChamberRight());
        chamberRight.notBlank();
        chamberRight.maxLength(150);

        var externalRight =check("ExternalEyeRight", item.getExternalEyeRight());
        externalRight.notBlank();
        externalRight.maxLength(550);
    }
}
