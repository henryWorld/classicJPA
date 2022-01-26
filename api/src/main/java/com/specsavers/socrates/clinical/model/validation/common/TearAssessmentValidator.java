package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentEyeDto;
import com.specsavers.socrates.common.validation.FieldChecks;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class TearAssessmentValidator extends Validator<TearAssessmentDto> {

    @Override
    public void validate(TearAssessmentDto item) {
        validate(item.getLeftEye(), item.getRightEye());
        FieldChecks check = check("observations", item.getObservations());
        check.maxLength(200);
    }

    private void validate(TearAssessmentEyeDto leftEye, TearAssessmentEyeDto rightEye) {
        if (leftEye != null && rightEye != null) {
            checkEyeFieldsNotBlank("Left eye: ", leftEye);
            checkEyeFieldsNotBlank("Right eye: ", rightEye);
        }
    }


    private void checkEyeFieldsNotBlank(String side, TearAssessmentEyeDto eye) {
            checkInput(side + "tbut", eye.getTbut());
            checkInput(side + "prism", eye.getPrism());
            checkInput(side + "schirmer", eye.getSchirmer());
            checkInput(side + "scope", eye.getScope());
    }


    private void checkInput(String field, String value) {
        var check = check(field, value);
        check.notBlank();
        check.maxLength(30);
    }

}
