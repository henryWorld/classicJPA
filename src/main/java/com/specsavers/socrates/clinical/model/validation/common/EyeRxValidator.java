package com.specsavers.socrates.clinical.model.validation.common;

import java.util.regex.Pattern;

import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.PrismDto;
import com.specsavers.socrates.common.exception.ValidationException;
import com.specsavers.socrates.common.validation.FieldChecks;

import org.springframework.stereotype.Component;

@Component
public class EyeRxValidator {
    private static final Pattern WHITESPACE = Pattern.compile("\\s");
    private static final String BALANCED = "BAL";

    public void validate(EyeRxDto leftEye, EyeRxDto rightEye) {
        checkEye("Left eye: ", leftEye);
        checkEye("Right eye: ", rightEye); 

        if (leftEye != null && rightEye != null) {
            checkCombinedEyePrism("", leftEye.getPrism(), rightEye.getPrism());
            checkCombinedEyePrism("Near", leftEye.getNearPrism(), rightEye.getNearPrism());
            checkCombinedEyePrism("Distance", leftEye.getDistancePrism(), rightEye.getDistancePrism());

            checkCombinedEyeCylinder(leftEye.getCylinder(), rightEye.getCylinder());
            checkCombinedEyeSphere(leftEye.getSphere(), rightEye.getSphere());
        }
    }

    private static FieldChecks check(String name, Object value) {
        return new FieldChecks(name, value);
    }

    protected FieldChecks check(String name, Object... values) {
        return new FieldChecks(name, values);
    }

    //region EyeChecks
    private void checkEye(String side, EyeRxDto eye) {
        if (eye != null) {
            check(side + "Cylinder and Axis", eye.getAxis(), eye.getCylinder()).requiredTogether();
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
    }

    void checkVa(String name, String value) {
        check(name, value).maxLength(10);
        if (value != null && value.indexOf('*') != -1){
            throw new ValidationException("Field " + name + " should not contain character '*'");
        }
    }
    

    private void checkSphere(String side, String sphere) {
        if (!BALANCED.equals(sphere)) {
            var check = check(side + "Sphere", sphere);
            check.maxLength(10);
            check.between(-40, +40);
            check.increment(0.25);
        }
    }

    private void checkCylinder(String side, String cylinder) {
        var check = check(side + "Cylinder", cylinder);
        check.maxLength(10);
        check.between(-20, +20);
        check.increment(0.25);
        check.isNot(0.0);
    }

    private void checkAxis(String side, Float axis) {
        var check = check(side + "Axis", axis);
        check.between(0, 180);
        check.increment(0.5);
    }

    void checkAdd(String side, Float add) {
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
        if (prismSlices.length != 2) {
            //Empty string to trigger the isOneOf("Up", "Down") validation
            return "";
        }
        
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

        var leftSign = Float.parseFloat(leftCylinder) > 0;
        var rightSign = Float.parseFloat(rightCylinder) > 0;
        
        if (leftSign != rightSign){
            throw new ValidationException("Cylinder should have same sign for both eyes");
        }
    }

    private void checkCombinedEyeSphere(String leftSphere, String rightSphere) {      
        if (BALANCED.equals(leftSphere) &&  BALANCED.equals(rightSphere)) {          
            throw new ValidationException("Sphere can't be BAL for both eyes");
        }
    }
    //endregion
}
