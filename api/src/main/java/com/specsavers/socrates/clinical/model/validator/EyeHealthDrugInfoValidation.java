package com.specsavers.socrates.clinical.model.validator;

import com.specsavers.socrates.clinical.model.EyeHealthDrugInfoDto;
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
@Constraint(validatedBy = EyeHealthDrugInfoValidation.Trigger.class)
@Documented
public @interface EyeHealthDrugInfoValidation {
    String message() default "EyeHealthDrugInfo input has failed validation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Trigger extends ValidatorTrigger<EyeHealthDrugInfoValidation, EyeHealthDrugInfoDto> {
        public Trigger() {
            super(EyeHealthDrugInfoDto.class);
        }
    }
}
