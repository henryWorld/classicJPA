package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.CurrentSpecsVaDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.SpecificAdditionDto;
import com.specsavers.socrates.clinical.model.UnaidedVisualAcuityDto;
import com.specsavers.socrates.common.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class RefractedRxValidator extends Validator<RefractedRxDto> {
    private final EyeRxValidator eyesCheck;
    
    @Override
    public void validate(RefractedRxDto item) {
        checkBvd(item.getBvd());
        checkCurrentSpecsVa(item.getCurrentSpecsVA());
        checkUnaidedVisualAcuity(item.getUnaidedVisualAcuity());       
        checkSpecificAdd(item.getSpecificAddition()); 
        eyesCheck.checkVa("Distance Bin VA", item.getDistanceBinVisualAcuity());
    
        eyesCheck.validate(item.getLeftEye(), item.getRightEye());
    }

    private void checkUnaidedVisualAcuity(UnaidedVisualAcuityDto value){
        if (value == null) {
            return;
        }

        eyesCheck.checkVa("Left eye: UnaidedVisualAcuity", value.getLeftEye());
        eyesCheck.checkVa("Right eye: UnaidedVisualAcuity", value.getRightEye());
        eyesCheck.checkVa("Binocular UnaidedVisualAcuity", value.getBinocular());
    }

    private void checkCurrentSpecsVa(CurrentSpecsVaDto value){
        if (value == null) {
            return;
        }

        eyesCheck.checkVa("Left eye: CurrentSpecsVA", value.getLeftEye());
        eyesCheck.checkVa("Right eye: CurrentSpecsVA", value.getRightEye());
    }

    private void checkBvd(Float bvd) {
        var check = check("bvd", bvd);
        check.between(5, 20);
        check.increment(0.5);
    }

    private void checkSpecificAdd(SpecificAdditionDto value) {
        if (value == null) {
            return;
        }

        eyesCheck.checkAdd("Left eye: Specific", value.getLeftEye());
        eyesCheck.checkAdd("Right eye: Specific", value.getRightEye());
        check("SpecificAdd Reason: ", value.getReason()).maxLength(250);
    }
    
    
}
