package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.PrismDto;
import com.specsavers.socrates.common.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="region", havingValue = "common")
public class HabitualRxValidator extends Validator<HabitualRxDto> {

    private static final String DIRECTION = " direction";
    private static final String VALUES = " values";
    private static final String HORIZONTAL_PRISM = " horizontal prism";
    private static final String VERTICAL_PRISM = " vertical prism";
    private static final String RIGHT = "right";
    private static final String LEFT = "left";

    private static final String PRISM_DIR_VERTICAL_UP = "up";
    private static final String PRISM_DIR_VERTICAL_DOWN = "down";
    private static final String PRISM_DIR_HORIZONTAL_IN = "in";
    private static final String PRISM_DIR_HORIZONTAL_OUT = "out";

    private enum DirectionType {
        HORIZONTAL,
        VERTICAL
    }

    @Override
    public void validate(HabitualRxDto item) {
        var rightRx = item.getRightEye();
        var leftRx = item.getLeftEye();

        check("eye rx values", leftRx, rightRx).requiredTogether();
        if(rightRx != null && leftRx != null) {
            performCombinedEyeChecks(leftRx, rightRx);
            checkEye("Right", rightRx);
            checkEye("Left", leftRx);
        }
    }

    private void checkEye(String side, EyeRxDto eye) {
        checkRx(side, eye);
        if(eye.getPrism() != null) {
            checkPrism(side, eye.getPrism());
        }
    }

    private void performCombinedEyeChecks(EyeRxDto leftRx, EyeRxDto rightRx) {
        check("cylinder values", rightRx.getCylinder(), leftRx.getCylinder()).requiredTogether();
        if(rightRx.getCylinder() != null && leftRx.getCylinder() != null) {
            when("right cylinder", rightRx.getCylinder()).maxValue(0.25)
                    .apply("left cylinder", leftRx.getCylinder()).maxValue(0.25);
            when("right cylinder", rightRx.getCylinder()).minValue(0.25)
                    .apply("left cylinder", leftRx.getCylinder()).minValue(0.25);
        }
        check("prism values", rightRx.getPrism(), leftRx.getPrism()).requiredTogether();
        if (rightRx.getPrism() != null && leftRx.getPrism() != null) {
            performCombinedPrismChecks(leftRx.getPrism(), rightRx.getPrism());
        }
    }

    private void checkRx(String side, EyeRxDto eyeRx) {
        check(side + " axis and cylinder", eyeRx.getAxis(), eyeRx.getCylinder()).requiredTogether();
        final String cylinder = " cylinder";
        check(side + cylinder, eyeRx.getCylinder()).between(-20, 20);
        check(side + cylinder, eyeRx.getCylinder()).increment(0.25);
        check(side + cylinder, eyeRx.getCylinder()).isNot(0d);
    }

    private void checkPrism(String side, PrismDto prism) {
        check("prism values", prism.getHorizontal(), prism.getHorizontal()).requiredTogether();
        if(prism.getVertical() != null && prism.getHorizontal() != null) {
            checkPrismValues(side, prism.getVertical(), DirectionType.VERTICAL);
            checkPrismValues(side, prism.getHorizontal(), DirectionType.HORIZONTAL);
        }
    }

    private void checkPrismValues(String side, String prism, DirectionType directionType) {
        var prismValue = Double.parseDouble(prism.split(" ")[0]);
        var prismDirection = prism.split(" ")[1].toLowerCase();
        var prismDirectionStr = directionType == DirectionType.VERTICAL ? " vertical" : " horizontal";

        check(side + prismDirectionStr, prismValue).increment(0.25);
        if(directionType.equals(DirectionType.VERTICAL)) {
            check(side + prismDirectionStr, prismValue).between(0.25, 30);
            check(side + prismDirectionStr + DIRECTION, prismDirection).isOneOf(PRISM_DIR_VERTICAL_UP, PRISM_DIR_VERTICAL_DOWN);
        } else {
            check(side + prismDirectionStr, prismValue).between(0.25, 50);
            check(side + prismDirectionStr + DIRECTION, prismDirection).isOneOf(PRISM_DIR_HORIZONTAL_IN, PRISM_DIR_HORIZONTAL_OUT);
        }
    }

    private void performCombinedPrismChecks(PrismDto left, PrismDto right) {
        checkHorizontalPrismDirections(getDirection(left.getHorizontal()), getDirection(right.getHorizontal()));
        checkVerticalPrismDirections(getDirection(left.getVertical()), getDirection(right.getVertical()));
    }

    private void checkHorizontalPrismDirections(String left, String right) {
        check(HORIZONTAL_PRISM + VALUES, left, right).requiredTogether();
        if(right != null && left != null) {
            when(RIGHT + HORIZONTAL_PRISM + DIRECTION, right.toLowerCase()).isOneOf(PRISM_DIR_HORIZONTAL_IN)
                    .apply(LEFT + HORIZONTAL_PRISM + DIRECTION, left.toLowerCase()).isOneOf(PRISM_DIR_HORIZONTAL_IN);
            when(RIGHT + HORIZONTAL_PRISM + DIRECTION, right.toLowerCase()).isOneOf(PRISM_DIR_HORIZONTAL_OUT)
                    .apply(LEFT + HORIZONTAL_PRISM + DIRECTION, left.toLowerCase()).isOneOf(PRISM_DIR_HORIZONTAL_OUT);
        }
    }

    private void checkVerticalPrismDirections(String left, String right) {
        check(VERTICAL_PRISM + VALUES, left, right).requiredTogether();
        if(right != null && left != null) {
            when(RIGHT + VERTICAL_PRISM + DIRECTION, right.toLowerCase()).isOneOf(PRISM_DIR_VERTICAL_UP)
                    .apply(LEFT + VERTICAL_PRISM + DIRECTION, left.toLowerCase()).isOneOf(PRISM_DIR_VERTICAL_DOWN);
            when(RIGHT + VERTICAL_PRISM + DIRECTION, right.toLowerCase()).isOneOf(PRISM_DIR_VERTICAL_DOWN)
                    .apply(LEFT + VERTICAL_PRISM + DIRECTION, left.toLowerCase()).isOneOf(PRISM_DIR_VERTICAL_UP);
        }
    }

    private String getDirection(String prism) {
        if (prism != null && !prism.isEmpty()) {
            return prism.split(" ")[1].toLowerCase();
        }
        return null;
    }
}
