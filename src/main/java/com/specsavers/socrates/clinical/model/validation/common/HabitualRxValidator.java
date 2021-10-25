package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.common.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="region", havingValue = "common")
public class HabitualRxValidator extends Validator<HabitualRxDto> {

    @Override
    public void validate(HabitualRxDto item) {
        var rightRx = item.getRightEye();
        var leftRx = item.getLeftEye();

        performCombinedEyeChecks(leftRx, rightRx);
        checkEye("Right", rightRx);
        checkEye("Left", leftRx);
    }

    private void checkEye(String side, EyeRxDto eye) {
        checkRx(side, eye);
    }

    private void performCombinedEyeChecks(EyeRxDto leftRx, EyeRxDto rightRx) {
        check("cylinder values", rightRx.getCylinder(), leftRx.getCylinder()).requiredTogether();
        when("right cylinder", rightRx.getCylinder()).maxValue(0.25)
                .apply("left cylinder", leftRx.getCylinder()).maxValue(0.25);
        when("right cylinder", rightRx.getCylinder()).minValue(0.25)
                .apply("left cylinder", leftRx.getCylinder()).minValue(0.25);
    }

    private void checkRx(String side, EyeRxDto eyeRx) {
        check(side + " axis and cylinder", eyeRx.getAxis(), eyeRx.getCylinder()).requiredTogether();
        check(side + " cylinder", eyeRx.getCylinder()).between(-20, 20);
        check(side + " cylinder", eyeRx.getCylinder()).increment(0.25);
        check(side + " cylinder", eyeRx.getCylinder()).isNot(0.0);
    }
}
