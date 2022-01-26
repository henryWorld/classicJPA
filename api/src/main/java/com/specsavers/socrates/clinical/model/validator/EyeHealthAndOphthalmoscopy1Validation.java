package com.specsavers.socrates.clinical.model.validator;

import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy1Dto;
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
@Constraint(validatedBy = EyeHealthAndOphthalmoscopy1Validation.Trigger.class)
@Documented
public @interface EyeHealthAndOphthalmoscopy1Validation {
    String message() default "EyeHealthAndOphthalmoscopy1 input has failed validation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Trigger extends ValidatorTrigger<EyeHealthAndOphthalmoscopy1Validation, EyeHealthAndOphthalmoscopy1Dto> {
        public Trigger() {
            super(EyeHealthAndOphthalmoscopy1Dto.class);
        }
    }
}
