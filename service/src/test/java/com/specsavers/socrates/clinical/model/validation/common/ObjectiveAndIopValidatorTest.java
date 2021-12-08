package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeIopDto;
import com.specsavers.socrates.clinical.model.ObjectiveAndIopDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.BALANCED;
import static com.specsavers.socrates.clinical.util.StaticHelpers.stringOfLength;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ObjectiveAndIopValidatorTest {
    private ObjectiveAndIopDto objectiveAndIopDto;
    private ObjectiveAndIopValidator validator;

    @BeforeEach
    void beforeEach() {
        objectiveAndIopDto = new ObjectiveAndIopDto();
        validator = new ObjectiveAndIopValidator(new EyeRxValidator());
    }

    @Nested
    class TimeValidation {
        @ParameterizedTest(name = "validWhenTime = {0}")
        @ValueSource(strings = {"00:00", "18:30", "23:59"})
        @NullSource
        void valid(String time) {
            objectiveAndIopDto.setTime(time);

            assertDoesNotThrow(() -> validator.validate(objectiveAndIopDto));
        }

        @ParameterizedTest(name = "invalidWhenTime = {0}")
        @ValueSource(strings = {"10:00:00", "24:00", "25:00", "XYZ"})
        void invalid(String time) {
            objectiveAndIopDto.setTime(time);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }
    }

    @Nested
    class NoteValidation {
        @ParameterizedTest(name = "validWhenNotesLength = {0}")
        @ValueSource(ints = {1, 10, 255})
        void valid(Integer length) {
            objectiveAndIopDto.setNotes(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(objectiveAndIopDto));
        }

        @ParameterizedTest(name = "invalidWhenNotesLength = {0}")
        @ValueSource(ints = {0, 256})
        void invalid(Integer length) {
            objectiveAndIopDto.setNotes(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }
    }

    @Nested
    class SphereValidation {
        @ParameterizedTest(name = "invalidWhenSphere = {0}")
        @ValueSource(strings = {"+5.20", "-50", "41"})
        void invalid(String value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setSphere(value);

            objectiveAndIopDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }

        @ParameterizedTest(name = "validWhenSphere = {0}")
        @ValueSource(strings = {BALANCED, "+5.50", "-2.25"})
        @NullSource
        void valid(String value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setSphere(value);

            objectiveAndIopDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(objectiveAndIopDto));
        }
    }

    @Nested
    class CylinderValidation {
        @ParameterizedTest(name = "validWhenCylinder = {0}")
        @ValueSource(floats = {-20, +20, 10.75f})
        @NullSource
        void valid(Float value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setCylinder(value);
            //Cylinder and Axis are required together
            if (eyeRx.getCylinder() != null) {
                eyeRx.setAxis(10f);
            }
            
            objectiveAndIopDto.setLeftEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(objectiveAndIopDto));
        }

        @ParameterizedTest(name = "invalidWhenCylinder = {0}")
        @ValueSource(floats = {-50, +21, 5.33f})
        void invalid(Float value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setCylinder(value);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            objectiveAndIopDto.setLeftEye(eyeRx);
            objectiveAndIopDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }

        @Test
        void testCylinderAndAxisMustBeTogether() {
            var eyeRx = new EyeIopDto();
            eyeRx.setCylinder(null);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            objectiveAndIopDto.setLeftEye(eyeRx);
            objectiveAndIopDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }
    }

    @Nested
    class AxisValidation {
        @ParameterizedTest(name = "validWhenAxis = {0}")
        @ValueSource(floats =  {0, 30.50f, 180})
        @NullSource
        void valid(Float value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setAxis(value);
            //Cylinder and Axis are required together
            if (eyeRx.getAxis() != null) {
                eyeRx.setCylinder(4.25f);
            }
            
            objectiveAndIopDto.setLeftEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(objectiveAndIopDto));
        }

        @ParameterizedTest(name = "invalidWhenAxis = {0}")
        @ValueSource(floats = {-5, 30.44f, 181})
        void invalid(Float value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setAxis(value);
            //Axis and Cylinder are required together
            eyeRx.setCylinder(5.25f);

            objectiveAndIopDto.setLeftEye(eyeRx);
            objectiveAndIopDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }
    }

    @Nested
    class VaValidation {
        @ParameterizedTest(name = "validWhenVa = {0}")
        @ValueSource(strings = {"20/20", "XYZ"})
        @NullSource
        void valid(String value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setVisualAcuity(value);

            objectiveAndIopDto.setLeftEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(objectiveAndIopDto));
        }

        @ParameterizedTest(name = "invalidWhenVa = {0}")
        @ValueSource(strings = {"hasStar*", "moreThan10char"})
        void invalid(String value) {
            var eyeRx = new EyeIopDto();
            eyeRx.setVisualAcuity(value);

            objectiveAndIopDto.setLeftEye(eyeRx);
            objectiveAndIopDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }
    }
    
    @Nested
    class CombinedValidations {

        @Test
        void bothEyesShouldValidate() {
            var mockEyeRxValidator = mock(EyeRxValidator.class);
            validator = new ObjectiveAndIopValidator(mockEyeRxValidator);

            var rightEye = new EyeIopDto();
            var leftEye = new EyeIopDto();
            rightEye.setSphere("right");
            leftEye.setSphere("left");

            objectiveAndIopDto.setRightEye(rightEye);
            objectiveAndIopDto.setLeftEye(leftEye);

            validator.validate(objectiveAndIopDto);

            verify(mockEyeRxValidator).checkSphere("Right eye: ","right");
            verify(mockEyeRxValidator).checkSphere("Left eye: ","left");
        }

        @Test
        void cylinderSignMustBeSame() {
            var eyeRx = new EyeIopDto();
            eyeRx.setCylinder(+3.50f);
            eyeRx.setAxis(1f);
            objectiveAndIopDto.setLeftEye(eyeRx);
            objectiveAndIopDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(objectiveAndIopDto));

            var newEyeRx = new EyeIopDto();
            newEyeRx.setCylinder(-2.25f);
            newEyeRx.setAxis(1f);
            objectiveAndIopDto.setLeftEye(eyeRx);
            objectiveAndIopDto.setRightEye(newEyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }

        @Test
        void sphereCannotHaveBalOnBothEyes() {
            var eyeRx = new EyeIopDto();
            eyeRx.setSphere(BALANCED);

            objectiveAndIopDto.setLeftEye(eyeRx);
            objectiveAndIopDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(objectiveAndIopDto));
        }
        
    }
}
