package com.specsavers.socrates.clinical.model.validation.common;

import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.PrismDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HabitualRxValidatorTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    private HabitualRxDto habitualRx;
    private HabitualRxValidator validator;

    private EyeRxDto rightRx;
    private EyeRxDto leftRx;

    private static final String VALID_CYLINDER = "5";
    private static final float VALID_AXIS = 0.8f;

    @BeforeEach
    void beforeEach() {
        habitualRx = new HabitualRxDto();
        validator = new HabitualRxValidator();
        rightRx = new EyeRxDto();
        leftRx = new EyeRxDto();
        habitualRx.setRightEye(rightRx);
        habitualRx.setLeftEye(leftRx);
    }

    @Test
    @DisplayName("When right and left cylinder are negative and positive respectively")
    void nonMatchingCylValues() {
        setCylinderValues("5", "-5");
        setAxisValues(VALID_AXIS, VALID_AXIS);

        var actual = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));

        assertEquals("When right cylinder is equal to or below 0.25, left cylinder must be equal to or less than 0.25", actual.getMessage());
    }

    @Test
    @DisplayName("When right and left horizontal prism directions are opposing")
    void testNonMatchingPrismHDirections() {
        setCylinderValues(VALID_CYLINDER, VALID_CYLINDER);
        setAxisValues(VALID_AXIS, VALID_AXIS);
        setPrismValues("30 Out", "30 In", "5 Up", "5 Down");

        var exception = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        var expectedMessage = "When right horizontal prism direction is one of in, left horizontal prism direction must be one of [in]";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("When right and left vertical prism directions are the same")
    void testNonMatchingPrismVDirections() {
        setCylinderValues(VALID_CYLINDER, VALID_CYLINDER);
        setAxisValues(VALID_AXIS, VALID_AXIS);
        setPrismValues("30 Out", "30 Out", "5 Up", "5 Up");

        var exception = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        var expectedMessage = "When right vertical prism direction is one of up, left vertical prism direction must be one of [down]";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("When cylinder increment is incorrect")
    void testIncorrectIncrementValues() {
        setCylinderValues(VALID_CYLINDER, "5.30");
        setAxisValues(VALID_AXIS, VALID_AXIS);

        habitualRx.setRightEye(rightRx);
        habitualRx.setLeftEye(leftRx);

        var actual = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));

        assertEquals("Right cylinder must be in increments of 0.25", actual.getMessage());
    }

    private void setCylinderValues(String left, String right) {
        leftRx.setCylinder(left);
        rightRx.setCylinder(right);
    }

    private void setAxisValues(float left, float right) {
        leftRx.setAxis(left);
        rightRx.setAxis(right);
    }

    private void setPrismValues(String hLeft, String hRight, String vLeft, String vRight) {
        var rightPrism = new PrismDto();
        var leftPrism = new PrismDto();
        leftPrism.setHorizontal(hLeft);
        rightPrism.setHorizontal(hRight);
        leftPrism.setVertical(vLeft);
        rightPrism.setVertical(vRight);
        leftRx.setPrism(leftPrism);
        rightRx.setPrism(rightPrism);
    }
}
