package com.specsavers.socrates.clinical.model.validator;

import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy2Dto;
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
@Constraint(validatedBy = EyeHealthAndOphthalmoscopy2Validation.Trigger.class)
@Documented
public @interface EyeHealthAndOphthalmoscopy2Validation {
    String message() default "EyeHealthAndOphthalmoscopy2 input has failed validation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Trigger extends ValidatorTrigger<EyeHealthAndOphthalmoscopy2Validation, EyeHealthAndOphthalmoscopy2Dto> {
        public Trigger() {
            super(EyeHealthAndOphthalmoscopy2Dto.class);
        }
    }
}
