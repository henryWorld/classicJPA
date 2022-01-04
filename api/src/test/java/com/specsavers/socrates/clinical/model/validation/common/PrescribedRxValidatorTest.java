package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeRxDto;
import com.specsavers.socrates.clinical.model.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.PrismDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.specsavers.socrates.clinical.model.validation.common.TestHelpers.BALANCED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrescribedRxValidatorTest {
    private PrescribedRxDto prescribedRxDto;
    private PrescribedRxValidator validator;

    @BeforeEach
    void beforeEach() {
        prescribedRxDto = new PrescribedRxDto();
        // RecallPeriod is mandatory
        prescribedRxDto.setRecallPeriod(24);
        validator = new PrescribedRxValidator(new EyeRxValidator());
    }

    @Nested
    class BvdValidation {
        @ParameterizedTest(name = "validWhenBvd = {0}")
        @ValueSource(floats = {5f, 6.5f, 20f})
        @NullSource
        void valid(Float bvd) {
            prescribedRxDto.setBvd(bvd);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenBvd = {0}")
        @ValueSource(floats = {0f, 10.25f, -4.5f, 21f})
        void invalid(float bvd) {
            prescribedRxDto.setBvd(bvd);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }
    }

    @Nested
    class RecallPeriodValidation {
        @ParameterizedTest(name = "validWhenRecallPeriod = {0}")
        @ValueSource(ints = {1, 12, 24, 36})
        void valid(Integer period) {
            prescribedRxDto.setRecallPeriod(period);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRecallPeriod = {0}")
        @ValueSource(ints = {0, 37, -4})
        @NullSource
        void invalid(Integer period) {
            prescribedRxDto.setRecallPeriod(period);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }
    }

    @Nested
    class RxBalSphereValidation {
        @ParameterizedTest(name = "invalidWhenRxBalSphere = {0}")
        @ValueSource(strings = {"+5.20", "-50", "41", BALANCED, "XYZ"})
        @NullSource
        void invalid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(BALANCED);
            eyeRx.setBalSphere(value);

            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "validWhenRxBalSphere = {0}")
        @ValueSource(strings = {"-40", "+5.50", "-2.25", "+40"})
        void valid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(BALANCED);
            eyeRx.setBalSphere(value);
            
            prescribedRxDto.setLeftEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }
    }

    @Nested
    class RxSphereValidation {
        @ParameterizedTest(name = "invalidWhenRxSphere = {0}")
        @ValueSource(strings = {"+5.20", "-50", "41"})
        void invalid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(value);

            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "validWhenRxSphere = {0}")
        @ValueSource(strings = {BALANCED, "+5.50", "-2.25"})
        @NullSource
        void valid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(value);
            if (BALANCED.equals(value)) {
                eyeRx.setBalSphere("+2.25");
            }
            
            //BVD is required when Sphere <= -5 and >= +5
            prescribedRxDto.setBvd(5f);
            prescribedRxDto.setLeftEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }
    }

    @Nested
    class RxCylinderValidation {
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
            
            //BVD is required when Cylinder <= -5 and >= +5
            prescribedRxDto.setBvd(5f);
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxCylinder = {0}")
        @ValueSource(strings = {"-50", "+21", "5.33"})
        void invalid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder(value);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        @Test
        void testCylinderAndAxisMustBeTogether() {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder(null);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }
    }

    @Nested
    class RxAxisValidation {
        @ParameterizedTest(name = "validWhenRxAxis = {0}")
        @ValueSource(floats =  {0, 30.50f, 180})
        @NullSource
        void valid(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAxis(value);
            //Cylinder and Axis are required together
            if (eyeRx.getAxis() != null) {
                eyeRx.setCylinder("+4.25");
            }
            
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxAxis = {0}")
        @ValueSource(floats = {-5, 30.44f, 181})
        void invalid(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAxis(value);
            //Axis and Cylinder are required together
            eyeRx.setCylinder("+5.25");

            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }
    }

    @Nested
    class RxAdditionValidation {
        @ParameterizedTest(name = "validWhenRxAddition = {0}")
        @ValueSource(floats =  {0.25f, 3.50f, 8})
        @NullSource
        void valid(Float value) {
            setupRxAddition(value);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxAddition = {0}")
        @ValueSource(floats = {-5, 3.44f, 9})
        void invalid(Float value) {
            setupRxAddition(value);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        private void setupRxAddition(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAddition(value);
            eyeRx.setNearAddition(value);
            eyeRx.setInterAddition(value);
                
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);
        }
    }

    @Nested
    class RxPdValidation {
        @ParameterizedTest(name = "validWhenRxPd = {0}")
        @ValueSource(floats =  {20, 30.50f, 40})
        @NullSource
        void valid(Float value) {
            setupRxPd(value);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxPd = {0}")
        @ValueSource(floats = {-5, 3.25f, 41})
        void invalid(Float value) {
            setupRxPd(value);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        private void setupRxPd(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setPupillaryDistance(value);
                
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);
        }
    }

    @Nested
    class RxVaValidation {
        @ParameterizedTest(name = "validWhenVa = {0}")
        @ValueSource(strings = {"20/20", "XYZ"})
        @NullSource
        void valid(String value) {
            setupVa(value);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenVa = {0}")
        @ValueSource(strings = {"hasStar*", "moreThan10char"})
        void invalid(String value) {
            setupVa(value);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        private void setupVa(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setVisualAcuity(value);
            eyeRx.setNearVisualAcuity(value);
            eyeRx.setDistanceVisualAcuity(value);
                
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);
            prescribedRxDto.setDistanceBinVisualAcuity(value);
        }
    }

    @Nested
    class RxPrismHorizontalValidation {
        @ParameterizedTest(name = "validWhenRxPrismHorizontal = {0}")
        @ValueSource(strings = {"0.25 Out", "5.50 In", "50 Out"})
        @NullSource
        void valid(String value) {
            setupRxPrismH(value);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxPrismHorizontal = {0}")
        @ValueSource(strings = {"0 In", "5.50 X", "51 Out", "3.45 Out", "2.50 Out X", "5.50" })
        void invalid(String value) {
            setupRxPrismH(value);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        private void setupRxPrismH(String value) {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setHorizontal(value);
            
            eyeRx.setPrism(prism);
            eyeRx.setNearPrism(prism);
            eyeRx.setDistancePrism(prism);
                
            prescribedRxDto.setLeftEye(eyeRx);
        }
    }

    @Nested
    class RxPrismVerticalValidation { 
        @ParameterizedTest(name = "validWhenRxPrismVertical = {0}")
        @ValueSource(strings = {"0.25 Down", "5.50 Up", "20 Down"})
        @NullSource
        void valid(String value) {
            setupRxPrismH(value);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxPrismVertical = {0}")
        @ValueSource(strings = {"0 Up", "5.50 X", "21 Down", "3.45 Down"})
        void invalid(String value) {
            setupRxPrismH(value);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        private void setupRxPrismH(String value) {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setVertical(value);
            
            eyeRx.setPrism(prism);
            eyeRx.setNearPrism(prism);
            eyeRx.setDistancePrism(prism);
                
            prescribedRxDto.setLeftEye(eyeRx);
        }
    }

    @Nested
    class CombinedValidations { 
        @Test
        void prismHorizontalDirectionMustBeSame() {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setHorizontal("3.25 In");

            eyeRx.setPrism(prism);
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));

            var newEyeRx = new EyeRxDto();
            var newPrism = new PrismDto();
            newPrism.setHorizontal("3.25 Out");

            newEyeRx.setPrism(newPrism);
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(newEyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        @Test
        void prismVerticalDirectionMustBeOpposite() {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setVertical("3.25 Up");

            eyeRx.setPrism(prism);
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));

            var newEyeRx = new EyeRxDto();
            var newPrism = new PrismDto();
            newPrism.setVertical("3.25 Down");

            newEyeRx.setPrism(newPrism);
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(newEyeRx);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @Test
        void cylinderSignMustBeSame() {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder("+3.50");
            eyeRx.setAxis(1f);
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));

            var newEyeRx = new EyeRxDto();
            newEyeRx.setCylinder("-2.25");
            newEyeRx.setAxis(1f);
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(newEyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "sphere={0}; cylinder={1}")
        @CsvSource({"+5.5,+1.25", "-2.50,-3.25", "0,+5", "+2.50,-10.75","-5,+2", "+5,-2"})
        void bvdRequiredWhenSphCylGreater5(String sphere, String cylinder) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(sphere);
            eyeRx.setCylinder(cylinder);
            eyeRx.setAxis(1f);
            
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
            
            prescribedRxDto.setBvd(5f);
            assertDoesNotThrow(() -> validator.validate(prescribedRxDto));
        }

        @ParameterizedTest(name = "sphere={0}; balSphere={1}")
        @CsvSource({"BAL,","BAL,' '", "2.25,-3.25", "2.25,' '"})
        void balSphereRequiredWhenSphEqualBAL(String sphere, String balSphere) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(sphere);
            eyeRx.setBalSphere(balSphere);
            
            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }

        @Test
        void sphereCannotHaveBalOnBothEyes() {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(BALANCED);

            prescribedRxDto.setLeftEye(eyeRx);
            prescribedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(prescribedRxDto));
        }
        
    }
}
