package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.type.UnaidedVisualAcuityDto;
import com.specsavers.socrates.common.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class PrescribedRxValidator extends Validator<PrescribedRxDto> {
    private final EyeRxValidator eyesCheck;
    private static final String BALANCED = "BAL";

    @Override
    public void validate(PrescribedRxDto item) {
        checkBvd(item.getBvd());
        checkUnaidedVisualAcuity(item.getUnaidedVisualAcuity());       
        checkRecallPeriod(item.getRecallPeriod());
        eyesCheck.checkVa("Distance Bin VA", item.getDistanceBinVisualAcuity());
        eyesCheck.validate(item.getLeftEye(), item.getRightEye());
        checkRequiredBvd("left eye ", item.getLeftEye(), item.getBvd());
        checkRequiredBvd("right eye ", item.getRightEye(), item.getBvd());
        checkBalSphere("Left eye: ", item.getLeftEye());
        checkBalSphere("Right eye: ", item.getRightEye());
    }

    private void checkRecallPeriod(Integer period) {
        var check = check("RecallPeriod", period);
        check.notNull();
        check.between(1, 36);
    }


    private void checkBvd(Float bvd) {
        var check = check("bvd", bvd);
        check.between(5, 20);
        check.increment(0.5);
    }

    private static float parseSphere(String value) {
        //Sphere was already validate, so the value is BAL or valid float
        if (value == null || BALANCED.equals(value)) {
          return 0f;
        }
        return Float.parseFloat(value);
      }
      
      private static float parseCylinder(String value) {
        if (value == null) {
          return 0f;
        }
        return Float.parseFloat(value);
      }
    
    private void checkRequiredBvd(String side, EyeRxDto eye, Float bvd) {
        if (eye != null) {
            float sphere = parseSphere(eye.getSphere());
            float cylinder = parseCylinder(eye.getCylinder());
            var powerSum = sphere + cylinder;

            when(side + "Sphere", sphere).outside(-5, 5)
                .apply("BVD", bvd).notNull();

            when(side + "Sphere and Cylinder sum", powerSum).outside(-5, 5)
                .apply("BVD", bvd).notNull();
        }
    }   

    private void checkUnaidedVisualAcuity(UnaidedVisualAcuityDto value){
        if (value == null) {
            return;
        }

        eyesCheck.checkVa("Left eye: UnaidedVisualAcuity", value.getLeftEye());
        eyesCheck.checkVa("Right eye: UnaidedVisualAcuity", value.getRightEye());
        eyesCheck.checkVa("Binocular UnaidedVisualAcuity", value.getBinocular());
    }

    private void checkBalSphere(String side, EyeRxDto eye) {
        if (eye == null) {
            return;
        }
        
        var check = check(side + "balSphere", eye.getBalSphere());
        if (BALANCED.equals(eye.getSphere())) {
            check.notNull();
            check.notBlank();
            check.between(-40, +40);
            check.increment(0.25);
        } else {
            check.isNull();
        }
    }
}
