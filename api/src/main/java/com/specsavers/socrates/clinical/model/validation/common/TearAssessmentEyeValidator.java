package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.TearAssessmentEyeDto;
import com.specsavers.socrates.common.validation.FieldChecks;
import org.springframework.stereotype.Component;

@Component
public class TearAssessmentEyeValidator {

    public void validate(TearAssessmentEyeDto leftEye, TearAssessmentEyeDto rightEye) {
        if (leftEye != null && rightEye != null) {
            checkEyeFieldsNotBlank("Left eye: ", leftEye);
            checkEyeFieldsNotBlank("Right eye: ", rightEye);
        }
    }


    //region EyeChecks
    private void checkEyeFieldsNotBlank(String side, TearAssessmentEyeDto eye) {
        if (eye != null) {
            checkInput(side + "tbut", eye.getTbut());
            checkInput(side + "prism", eye.getPrism());
            checkInput(side + "schirmer", eye.getSchirmer());
            checkInput(side + "scope", eye.getScope());
        }      
    }


    private void checkInput(String field, String value) {
        var check = check(field,value);
        check.notBlank();
    }

    private  FieldChecks check(String field, Object value) {
        return new FieldChecks(field, value);
    }

}
