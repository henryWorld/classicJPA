package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeHealthDrugInfoDto;
import com.specsavers.socrates.common.exception.ValidationException;
import com.specsavers.socrates.common.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@Component
@ConditionalOnProperty(name = "region", havingValue = "common")
public class EyeHealthDrugInfoValidator extends Validator<EyeHealthDrugInfoDto> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    @Autowired
    private DrugInfoValidator drugInfoValidator;

    @Override
    public void validate(EyeHealthDrugInfoDto item) {
        checkTime("PrePressureTime", item.getPrePressureTime());
        checkTime("PostPressureTime", item.getPostPressureTime());

        var prePressure = check("PrePressure", item.getPrePressure());
        prePressure.notBlank();
        prePressure.maxLength(25);

        var postPressure = check("PostPressure", item.getPostPressure());
        postPressure.notBlank();
        postPressure.maxLength(25);

        checkMandatoryExpiryDate(item);
        drugInfoValidator.validate(item.getDrugInfo());
    }

    private void checkTime(String field, String value) {
        if (value != null) {
            try {
                TIME_FORMATTER.parse(value);
            } catch (DateTimeParseException e) {
                throw new ValidationException(field + " is not in correct format 'HH:mm'");
            }
        }
    }

    private void checkMandatoryExpiryDate(EyeHealthDrugInfoDto info) {
        var isAnyFieldNotNull = (
                info.getPostPressure() != null ||
                info.getPostPressureTime() != null ||
                info.getPrePressure() != null ||
                info.getPrePressureTime() != null);

        if ((info.getDrugInfo() == null || info.getDrugInfo().getExpiryDate() == null) &&  isAnyFieldNotNull){
            throw new ValidationException("ExpiryDate is mandatory when any other field has value");
        }
    }
}
