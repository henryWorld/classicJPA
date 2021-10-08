package com.specsavers.socrates.clinical.model.validator;

import com.specsavers.socrates.clinical.model.type.HistoryAndSymptomsDto;
import com.specsavers.socrates.common.validation.ValidatorTrigger;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HistoryAndSymptomsValidation.Trigger.class)
@Documented
public @interface HistoryAndSymptomsValidation {
    String message() default "History and symptoms input has failed validation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Trigger extends ValidatorTrigger<HistoryAndSymptomsValidation, HistoryAndSymptomsDto> {
        public Trigger() {
            super(HistoryAndSymptomsDto.class);
        }
    }
}
