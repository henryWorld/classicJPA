package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.LifestyleDto;
import com.specsavers.socrates.common.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "region", havingValue = "common")
public class HistoryAndSymptomsValidator extends Validator<HistoryAndSymptomsDto> {
    @Override
    public void validate(HistoryAndSymptomsDto item) {
        validateNotes("reasonForVisit", item.getReasonForVisit());
        validateNotes("generalHealth", item.getGeneralHealth());
        validateNotes("medication", item.getMedication());
        validateNotes("ocularHistory", item.getOcularHistory());
        validateNotes("familyHistory", item.getFamilyHistory());
        validateLifestyle(item.getLifestyle());
    }

    private void validateNotes(String name, String value) {
        validateText(name, value, 1000);
    }

    private void validateLifestyle(LifestyleDto lifestyle) {
        check("lifestyle.vduHoursPerDay", lifestyle.getVduHoursPerDay())
                .between(1, 24);

        validateText("lifestyle.occupation", lifestyle.getOccupation(), 60);
        validateText("lifestyle.hobbies", lifestyle.getHobbies(), 60);
    }

    private void validateText(String name, String value, int maxLength) {
        var check = check(name, value);
        check.minLength(1);
        check.notBlank();
        check.maxLength(maxLength);
    }
}
