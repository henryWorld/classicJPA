package com.specsavers.socrates.clinical.model.validation.common;

import java.util.regex.Pattern;

import com.specsavers.socrates.clinical.model.type.CurrentSpecsVaDto;
import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.PrismDto;
import com.specsavers.socrates.clinical.model.type.RefractedRxDto;
import com.specsavers.socrates.clinical.model.type.SpecificAdditionDto;
import com.specsavers.socrates.clinical.model.type.UnaidedVisualAcuityDto;
import com.specsavers.socrates.common.exception.ValidationException;
import com.specsavers.socrates.common.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "region", havingValue = "common")
public class RefractedRxValidator extends Validator<RefractedRxDto> {
    private static final Pattern WHITESPACE = Pattern.compile("\\s");
    
    @Override
    public void validate(RefractedRxDto item) {
        var leftEye = item.getLeftEye();
        var rightEye = item.getRightEye();

        checkBvd(item.getBvd());
        checkCurrentSpecsVa(item.getCurrentSpecsVA());
        checkUnaidedVisualAcuity(item.getUnaidedVisualAcuity());       
        checkSpecificAdd(item.getSpecificAddition()); 
        checkVa("Distance Bin VA", item.getDistanceBinVisualAcuity());
        checkEye("Left eye: ", leftEye);
        checkEye("Right eye: ", rightEye); 

        if (leftEye != null && rightEye != null) {
            checkCombinedEyePrism("", leftEye.getPrism(), item.getRightEye().getPrism());
            checkCombinedEyePrism("Near", leftEye.getNearPrism(), item.getRightEye().getNearPrism());
            checkCombinedEyePrism("Distance", leftEye.getDistancePrism(), item.getRightEye().getDistancePrism());

            checkCombinedEyeCylinder(leftEye.getCylinder(), item.getRightEye().getCylinder());
        }
    }

 

    private void checkVa(String name, String value) {
        check(name, value).maxLength(10);
        if (value != null && value.indexOf('*') != -1){
            throw new ValidationException("Field " + name + " should not contain character '*'");
        }
    }

    private void checkUnaidedVisualAcuity(UnaidedVisualAcuityDto value){
        if (value == null) {
            return;
        }

        checkVa("Left eye: UnaidedVisualAcuity", value.getLeftEye());
        checkVa("Right eye: UnaidedVisualAcuity", value.getRightEye());
        checkVa("Binocular UnaidedVisualAcuity", value.getBinocular());
    }

    private void checkCurrentSpecsVa(CurrentSpecsVaDto value){
        if (value == null) {
            return;
        }

        checkVa("Left eye: CurrentSpecsVA", value.getLeftEye());
        checkVa("Right eye: CurrentSpecsVA", value.getRightEye());
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

        checkAdd("Left eye: Specific", value.getLeftEye());
        checkAdd("Right eye: Specific", value.getRightEye());
        check("SpecificAdd Reason: ", value.getReason()).maxLength(250);
    }
    
    //region EyeChecks
    private void checkEye(String side, EyeRxDto eye) {
        if (eye == null) {
            return;
        }
        
        check("Cylinder and Axis", eye.getAxis(), eye.getCylinder()).requiredTogether();
        checkSphere(side, eye.getSphere());
        checkCylinder(side, eye.getCylinder());
        checkAxis(side, eye.getAxis());
        checkAdd(side, eye.getAddition());
        checkAdd(side + "Near", eye.getNearAddition());
        checkAdd(side + "Inter", eye.getInterAddition());
        checkPd(side, eye.getPupillaryDistance());
        checkVa(side + "VA", eye.getVisualAcuity());
        checkVa(side + "Near VA", eye.getNearVisualAcuity());
        checkVa(side + "Distance VA", eye.getDistanceVisualAcuity());
        checkPrism(side, eye.getPrism());
        checkPrism(side + "Near", eye.getNearPrism());
        checkPrism(side + "Distance", eye.getDistancePrism());
    }

    

    private void checkSphere(String side, String sphere) {
        final String BAL = "BAL";

        if (!BAL.equals(sphere)) {
            var check = check(side + "Sphere", sphere);
            check.between(-40, +40);
            check.increment(-0.25);
        }
    }

    private void checkCylinder(String side, String cylinder) {
        var check = check(side + "Cylinder", cylinder);
        check.between(-20, +20);
        check.increment(0.25);
        check.isNot(0.0);
    }

    private void checkAxis(String side, Float axis) {
        var check = check(side + "Axis", axis);
        check.between(0, 180);
        check.increment(0.5);
    }

    private void checkAdd(String side, Float add) {
        var check = check(side + "Add", add);
        check.between(0.25, 8);
        check.increment(0.25);
    }
   
    private void checkPd(String side, Float pd) {
        var check = check(side + "PupillaryDistance", pd);
        check.between(20, 40);
        check.increment(0.5);
    }
    //endregion
    
    //region Prism Checks
    private void checkPrism(String side, PrismDto prism) {
        if (prism == null) {
            return;
        }

        var prismH = check(side + "Prism H", getPrismPower(prism.getHorizontal()));
        prismH.between(0.25, 50);
        prismH.increment(0.25);
        check(side + "Prism H Direction", getPrismDirection(prism.getHorizontal())).isOneOf("Out", "In");

        var prismV = check(side + "Prism V", getPrismPower(prism.getVertical()));
        prismV.between(0.25, 20);
        prismV.increment(0.25);
        check(side + "Prism V Direction", getPrismDirection(prism.getVertical())).isOneOf("Up", "Down");
    } 

    private String getPrismPower(String prism){
        if (prism == null) {
            return null;
        }
        
        return WHITESPACE.split(prism)[0];
    }

    private String getPrismDirection(String prism){
        if (prism == null) {
            return null;
        }

        var prismSlices = WHITESPACE.split(prism);
        if (prismSlices.length != 2) return "invalidDirection";
        
        return prismSlices[1];
    }
    //endregion

    //region Combined Checks 
    private void checkCombinedEyePrism(String type, PrismDto left, PrismDto right) {
        if (left == null || right == null ) {
            return;
        }

        checkCombinedEyePrismH(type, getPrismDirection(left.getHorizontal()), getPrismDirection(right.getHorizontal()));
        checkCombinedEyePrismV(type, getPrismDirection(left.getVertical()), getPrismDirection(right.getVertical()));
    }

    private void checkCombinedEyePrismH(String type, String leftDirection, String rightDirection) {
        if (leftDirection == null || rightDirection == null ) {
            return;
        }
        
        if (!leftDirection.equals(rightDirection)){
            throw new ValidationException("When " + type + " Prism H is set for both eyes, the direction must be the same");
        }
    }

    private void checkCombinedEyePrismV(String type, String leftDirection, String rightDirection) {
        if (leftDirection == null || rightDirection == null ) {
            return;
        }
        
        if (leftDirection.equals(rightDirection)){
            throw new ValidationException("When " + type + " Prism V is set for both eyes, the direction must be opposite");
        }
    }

    private void checkCombinedEyeCylinder(String leftCylinder, String rightCylinder) {
        if (leftCylinder == null || rightCylinder == null ) {
            return;
        }

        var leftSigh = Float.parseFloat(leftCylinder) > 0;
        var rightSigh = Float.parseFloat(rightCylinder) > 0;
        
        if (leftSigh != rightSigh){
            throw new ValidationException("Cylinder should have same sign for both eyes");
        }
    }
    //endregion
}
