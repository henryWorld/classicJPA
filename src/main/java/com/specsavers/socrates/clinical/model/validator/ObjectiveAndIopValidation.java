package com.specsavers.socrates.clinical.model.validator;

import com.specsavers.socrates.clinical.model.type.ObjectiveAndIopDto;
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
@Constraint(validatedBy = ObjectiveAndIopValidation.Trigger.class)
@Documented
public @interface ObjectiveAndIopValidation {
    String message() default "ObjectiveAndIop input has failed validation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Trigger extends ValidatorTrigger<ObjectiveAndIopValidation, ObjectiveAndIopDto> {
        public Trigger() {
            super(ObjectiveAndIopDto.class);
        }
    }
}
