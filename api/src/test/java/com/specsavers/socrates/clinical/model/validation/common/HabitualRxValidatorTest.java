package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeRxDto;
import com.specsavers.socrates.clinical.model.HabitualRxDto;
import com.specsavers.socrates.clinical.model.PrismDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.specsavers.socrates.clinical.model.validation.common.TestHelpers.BALANCED;
import static com.specsavers.socrates.clinical.model.validation.common.TestHelpers.stringOfLength;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HabitualRxValidatorTest {

    private HabitualRxDto habitualRx;
    private HabitualRxValidator validator;

    @BeforeEach
    void beforeEach() {
        habitualRx = new HabitualRxDto();
        validator = new HabitualRxValidator(new EyeRxValidator());
        habitualRx.setRightEye(new EyeRxDto());
        habitualRx.setLeftEye(new EyeRxDto());
    }

    @Nested
    class RxSphereValidationTest {
        @ParameterizedTest(name = "invalidWhenRxSphere = {0}")
        @ValueSource(strings = {"+5.20", "-50", "41"})
        void invalid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(value);

            habitualRx.setLeftEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "validWhenRxSphere = {0}")
        @ValueSource(strings = {"BAL", "+5.50", "-2.25"})
        @NullSource
        void valid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(value);
            habitualRx.setLeftEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }
    }

    @Nested
    class RxCylinderValidationTest {
        @ParameterizedTest(name = "validWhenRxCylinder = {0}")
        @ValueSource(strings = {"-20", "+20", "10.75"})
        @NullSource
        void valid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder(value);
            //Cylinder and Axis are required together
            if (eyeRx.getCylinder() != null) {
                eyeRx.setAxis(10f);
            }

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "invalidWhenRxCylinder = {0}")
        @ValueSource(strings = {"-50", "+21", "5.33"})
        void invalid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder(value);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }

        @Test
        void testCylinderAndAxisMustBeTogether() {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder(null);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }
    }

    @Nested
    class RxAxisValidationTest {
        @ParameterizedTest(name = "validWhenRxAxis = {0}")
        @ValueSource(floats =  {0, 30.50f, 180})
        @NullSource
        void valid(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAxis(value);
            //Axis and Axis are required together
            if (eyeRx.getAxis() != null) {
                eyeRx.setCylinder("+5.25");
            }

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "invalidWhenRxAxis = {0}")
        @ValueSource(floats = {-5, 30.44f, 181})
        void invalid(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAxis(value);
            //Axis and Cylinder are required together
            eyeRx.setCylinder("+5.25");

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }
    }

    @Nested
    class RxAdditionValidationTest {
        @ParameterizedTest(name = "validWhenRxAddition = {0}")
        @ValueSource(floats =  {0.25f, 3.50f, 8})
        @NullSource
        void valid(Float value) {
            setupRxAddition(value);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "invalidWhenRxAddition = {0}")
        @ValueSource(floats = {-5, 3.44f, 9})
        void invalid(Float value) {
            setupRxAddition(value);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }

        private void setupRxAddition(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAddition(value);

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);
        }
    }

    @Nested
    class RxVaValidationTest {
        @ParameterizedTest(name = "validWhenVa = {0}")
        @ValueSource(strings = {"20/20", "XYZ"})
        @NullSource
        void valid(String value) {
            setupVa(value);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "invalidWhenVa = {0}")
        @ValueSource(strings = {"hasStar*", "moreThan10char"})
        void invalid(String value) {
            setupVa(value);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }

        private void setupVa(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setVisualAcuity(value);

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);
        }
    }

    @Nested
    class ClinicianNameTest {
        @ParameterizedTest(name = "validWhenClinicianNameLength = {0}")
        @ValueSource(ints = {1, 10, 25})
        void valid(Integer length) {
            habitualRx.setClinicianName(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "invalidWhenClinicianNameLength = {0}")
        @ValueSource(ints = {0, 26, 500})
        void invalid(Integer length) {
            habitualRx.setClinicianName(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }

        private void setupVa(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setVisualAcuity(value);

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);
        }
    }

    @Nested
    class RxPrismHorizontalValidationTest {
        @ParameterizedTest(name = "validWhenRxPrismHorizontal = {0}")
        @ValueSource(strings = {"0.25 Out", "5.50 In", "50 Out"})
        @NullSource
        void valid(String value) {
            setupRxPrismH(value);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "invalidWhenRxPrismHorizontal = {0}")
        @ValueSource(strings = {"0 In", "5.50 X", "51 Out", "3.45 Out", "2.50 Out X", "5.50" })
        void invalid(String value) {
            setupRxPrismH(value);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }

        private void setupRxPrismH(String value) {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setHorizontal(value);
            eyeRx.setPrism(prism);

            habitualRx.setLeftEye(eyeRx);
        }
    }

    @Nested
    class RxPrismVerticalValidationTest {
        @ParameterizedTest(name = "validWhenRxPrismVertical = {0}")
        @ValueSource(strings = {"0.25 Down", "5.50 Up", "20 Down"})
        @NullSource
        void valid(String value) {
            setupRxPrismH(value);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @ParameterizedTest(name = "invalidWhenRxPrismVertical = {0}")
        @ValueSource(strings = {"0 Up", "5.50 X", "21 Down", "3.45 Down"})
        void invalid(String value) {
            setupRxPrismH(value);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }

        private void setupRxPrismH(String value) {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setVertical(value);
            eyeRx.setPrism(prism);

            habitualRx.setLeftEye(eyeRx);
        }
    }

    @Nested
    class CombinedValidationsTest {
        @Test
        void prismHorizontalDirectionMustBeSame() {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setHorizontal("3.25 In");

            eyeRx.setPrism(prism);
            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(habitualRx));

            var newEyeRx = new EyeRxDto();
            var newPrism = new PrismDto();
            newPrism.setHorizontal("3.25 Out");

            newEyeRx.setPrism(newPrism);
            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(newEyeRx);

            var actual = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));

            assertEquals("When  Prism H is set for both eyes, the direction must be the same", actual.getMessage());
        }

        @Test
        void prismVerticalDirectionMustBeOpposite() {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setVertical("3.25 Up");

            eyeRx.setPrism(prism);
            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            var actual = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));

            assertEquals("When  Prism V is set for both eyes, the direction must be opposite", actual.getMessage());

            var newEyeRx = new EyeRxDto();
            var newPrism = new PrismDto();
            newPrism.setVertical("3.25 Down");

            newEyeRx.setPrism(newPrism);
            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(newEyeRx);

            assertDoesNotThrow(() -> validator.validate(habitualRx));
        }

        @Test
        void cylinderSignMustBeSame() {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder("+3.50");
            eyeRx.setAxis(1f);
            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(habitualRx));

            var newEyeRx = new EyeRxDto();
            newEyeRx.setCylinder("-2.25");
            newEyeRx.setAxis(1f);
            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(newEyeRx);

            var actual = assertThrows(ValidationException.class, () -> validator.validate(habitualRx));

            assertEquals("Cylinder should have same sign for both eyes", actual.getMessage());
        }

        @Test
        void sphereCannotHaveBalOnBothEyes() {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(BALANCED);

            habitualRx.setLeftEye(eyeRx);
            habitualRx.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(habitualRx));
        }
    }
}
