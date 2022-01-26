package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name="region", havingValue = "common")
public class TearAssessmentValidator extends Validator<TearAssessmentDto> {
    private final TearAssessmentEyeValidator eyesCheck;

    @Override
    public void validate(TearAssessmentDto item) {
        eyesCheck.validate(item.getLeftEye(), item.getRightEye());
    }
}
