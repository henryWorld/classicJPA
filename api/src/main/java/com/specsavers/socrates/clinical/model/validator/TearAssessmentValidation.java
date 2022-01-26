package com.specsavers.socrates.clinical.model.validator;

import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.common.validation.ValidatorTrigger;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TearAssessmentValidation.Trigger.class)
@Documented
public @interface TearAssessmentValidation {

    String message() default "Tear Assessment input has failed validation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Trigger extends ValidatorTrigger<TearAssessmentValidation, TearAssessmentDto> {
        public Trigger() {
            super(TearAssessmentDto.class);
        }
    }

}
