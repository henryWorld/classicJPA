package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.type.EyeIopDto;
import com.specsavers.socrates.clinical.model.type.ObjectiveAndIopDto;
import com.specsavers.socrates.common.exception.ValidationException;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class ObjectiveAndIopValidator extends Validator<ObjectiveAndIopDto> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private final EyeRxValidator eyesCheck;

    @Override
    public void validate(ObjectiveAndIopDto item) {
        var leftEye = item.getLeftEye();
        var rightEye = item.getRightEye();

        //Specific eye checks
        checkEye("Left eye: ", leftEye);
        checkEye("Right eye: ", rightEye);

        //No specific eye checks
        checkTime(item.getTime());
        var notesCheck = check("Notes", item.getNotes());
        notesCheck.notBlank();
        notesCheck.maxLength(255);

        if (leftEye != null && rightEye != null) {
            eyesCheck.checkCombinedEyeCylinder(leftEye.getCylinder(), rightEye.getCylinder());
            eyesCheck.checkCombinedEyeSphere(leftEye.getSphere(), rightEye.getSphere());
        }

    }

    private void checkEye(String side, EyeIopDto eye) {
        if (eye != null) {
            check(side + "Cylinder and Axis", eye.getAxis(), eye.getCylinder()).requiredTogether();
            eyesCheck.checkSphere(side, eye.getSphere());
            eyesCheck.checkCylinder(side, eye.getCylinder());
            eyesCheck.checkAxis(side, eye.getAxis());
            eyesCheck.checkVa(side + "VisualAcuity", eye.getVisualAcuity());
            checkIop(side, 1, eye.getIop1());
            checkIop(side, 2, eye.getIop2());
            checkIop(side, 3, eye.getIop3());
            checkIop(side, 4, eye.getIop4());
        }
    }

    private void checkTime(String time) {
        if (time != null) {
            try {
                TIME_FORMATTER.parse(time);
            } catch (DateTimeParseException e) {
                throw new ValidationException("Time is not in correct format 'HH:mm'");
            }
        }
    }

    private void checkIop(String side, int iopNum, Integer iopValue) {
       check(side+"IOP"+iopNum, iopValue).between(0,99);
    }

}
