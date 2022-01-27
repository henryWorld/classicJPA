package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy2Dto;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class EyeHealthAndOphthalmoscopy2Validator extends Validator<EyeHealthAndOphthalmoscopy2Dto> {

    @Override
    public void validate(EyeHealthAndOphthalmoscopy2Dto item) {
        var lensRight = check("lensRight", item.getLensRight());
        lensRight.notBlank();
        lensRight.maxLength(400);

        var lensLeft = check("lensLeft", item.getLensLeft());
        lensLeft.notBlank();
        lensLeft.maxLength(400);

        var vitreousRight = check("vitreousRight", item.getVitreousRight());
        vitreousRight.notBlank();
        vitreousRight.maxLength(250);

        var vitreousLeft = check("vitreousLeft", item.getVitreousLeft());
        vitreousLeft.notBlank();
        vitreousLeft.maxLength(250);
    }
}
