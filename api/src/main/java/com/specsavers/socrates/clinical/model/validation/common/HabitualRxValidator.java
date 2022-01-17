package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.HabitualRxDto;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name="region", havingValue = "common")
public class HabitualRxValidator extends Validator<HabitualRxDto> {
    private final EyeRxValidator eyesCheck;

    @Override
    public void validate(HabitualRxDto item) {
        var clinicianName = check("ClinicianName", item.getClinicianName());
        clinicianName.notBlank();
        clinicianName.maxLength(25);

        eyesCheck.validate(item.getLeftEye(), item.getRightEye());
    }
}
