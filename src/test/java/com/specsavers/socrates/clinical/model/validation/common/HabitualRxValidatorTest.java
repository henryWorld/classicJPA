package com.specsavers.socrates.clinical.model.validation.common;

import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
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

    @BeforeEach
    void beforeEach() {
        habitualRx = new HabitualRxDto();
        validator = new HabitualRxValidator();
    }

    @Test
    @DisplayName("Test validation when right and left are negative and positive respectively")
    void nonMatchingCylValues() {

        var rightRx = new EyeRxDto();
        var leftRx = new EyeRxDto();
        rightRx.setCylinder("-5");
        leftRx.setCylinder("5");
        rightRx.setAxis(0.8f);
        leftRx.setAxis(0.8f);

        habitualRx.setRightEye(rightRx);
        habitualRx.setLeftEye(leftRx);

        var actual = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));

        assertEquals("When right cylinder is equal to or below 0.25, left cylinder must be equal to or less than 0.25", actual.getMessage());
    }

    @Test
    @DisplayName("When cylinder increment is incorrect")
    void testIncorrectIncrementValues() {

        var rightRx = new EyeRxDto();
        var leftRx = new EyeRxDto();
        rightRx.setCylinder("-5.30");
        leftRx.setCylinder("-5");
        rightRx.setAxis(0.8f);
        leftRx.setAxis(0.8f);

        habitualRx.setRightEye(rightRx);
        habitualRx.setLeftEye(leftRx);

        var actual = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));

        assertEquals("Right cylinder must be in increments of 0.25", actual.getMessage());
    }
}
