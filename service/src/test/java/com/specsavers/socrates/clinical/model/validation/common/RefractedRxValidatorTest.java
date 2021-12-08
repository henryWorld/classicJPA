package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeRxDto;
import com.specsavers.socrates.clinical.model.PrismDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.SpecificAdditionDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.BALANCED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RefractedRxValidatorTest {
    private RefractedRxDto refractedRxDto;
    private RefractedRxValidator validator;

    @BeforeEach
    void beforeEach() {
        refractedRxDto = new RefractedRxDto();
        validator = new RefractedRxValidator(new EyeRxValidator());
    }

    @Nested
    class BvdValidation {
        @ParameterizedTest(name = "validWhenBvd = {0}")
        @ValueSource(floats = {5f, 6.5f, 20f})
        @NullSource
        void valid(Float bvd) {
            refractedRxDto.setBvd(bvd);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenBvd = {0}")
        @ValueSource(floats = {0f, 10.25f, -4.5f, 21f})
        void invalid(float bvd) {
            refractedRxDto.setBvd(bvd);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }
    }

    @Nested
    class SpecificAddValidation {
        @ParameterizedTest(name = "validWhenSpecificAdd = {0}")
        @ValueSource(floats = {0.25f, 5f, 8f})
        @NullSource
        void valid(Float value) {
            var specAdd = new SpecificAdditionDto();
            specAdd.setLeftEye(value);
            specAdd.setRightEye(value);
            refractedRxDto.setSpecificAddition(specAdd);
            
            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenSpecificAdd = {0}")
        @ValueSource(floats = {0f, 5.8f, -4.5f, 21f})
        void invalid(Float value) {
            var specAdd = new SpecificAdditionDto();
            specAdd.setLeftEye(value);
            specAdd.setRightEye(value);
            refractedRxDto.setSpecificAddition(specAdd);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }
    }

    @Nested
    class RxSphereValidation {
        @ParameterizedTest(name = "invalidWhenRxSphere = {0}")
        @ValueSource(strings = {"+5.20", "-50", "41"})
        void invalid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(value);

            refractedRxDto.setLeftEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "validWhenRxSphere = {0}")
        @ValueSource(strings = {"BAL", "+5.50", "-2.25"})
        @NullSource
        void valid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(value);
            refractedRxDto.setLeftEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
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
            
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxCylinder = {0}")
        @ValueSource(strings = {"-50", "+21", "5.33"})
        void invalid(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder(value);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        @Test
        void testCylinderAndAxisMustBeTogether() {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder(null);
            //Cylinder and Axis are required together
            eyeRx.setAxis(10f);

            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
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
            //Axis and Axis are required together
            if (eyeRx.getAxis() != null) {
                eyeRx.setCylinder("+5.25");
            }
            
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxAxis = {0}")
        @ValueSource(floats = {-5, 30.44f, 181})
        void invalid(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAxis(value);
            //Axis and Cylinder are required together
            eyeRx.setCylinder("+5.25");

            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }
    }

    @Nested
    class RxAdditionValidation {
        @ParameterizedTest(name = "validWhenRxAddition = {0}")
        @ValueSource(floats =  {0.25f, 3.50f, 8})
        @NullSource
        void valid(Float value) {
            setupRxAddition(value);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxAddition = {0}")
        @ValueSource(floats = {-5, 3.44f, 9})
        void invalid(Float value) {
            setupRxAddition(value);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        private void setupRxAddition(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setAddition(value);
            eyeRx.setNearAddition(value);
            eyeRx.setInterAddition(value);
                
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);
        }
    }

    @Nested
    class RxPdValidation {
        @ParameterizedTest(name = "validWhenRxPd = {0}")
        @ValueSource(floats =  {20, 30.50f, 40})
        @NullSource
        void valid(Float value) {
            setupRxPd(value);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxPd = {0}")
        @ValueSource(floats = {-5, 3.25f, 41})
        void invalid(Float value) {
            setupRxPd(value);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        private void setupRxPd(Float value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setPupillaryDistance(value);
                
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);
        }
    }

    @Nested
    class RxVaValidation {
        @ParameterizedTest(name = "validWhenVa = {0}")
        @ValueSource(strings = {"20/20", "XYZ"})
        @NullSource
        void valid(String value) {
            setupVa(value);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenVa = {0}")
        @ValueSource(strings = {"hasStar*", "moreThan10char"})
        void invalid(String value) {
            setupVa(value);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        private void setupVa(String value) {
            var eyeRx = new EyeRxDto();
            eyeRx.setVisualAcuity(value);
            eyeRx.setNearVisualAcuity(value);
            eyeRx.setDistanceVisualAcuity(value);
                
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);
            refractedRxDto.setDistanceBinVisualAcuity(value);
        }
    }

    @Nested
    class RxPrismHorizontalValidation {
        @ParameterizedTest(name = "validWhenRxPrismHorizontal = {0}")
        @ValueSource(strings = {"0.25 Out", "5.50 In", "50 Out"})
        @NullSource
        void valid(String value) {
            setupRxPrismH(value);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxPrismHorizontal = {0}")
        @ValueSource(strings = {"0 In", "5.50 X", "51 Out", "3.45 Out", "2.50 Out X", "5.50" })
        void invalid(String value) {
            setupRxPrismH(value);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        private void setupRxPrismH(String value) {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setHorizontal(value);
            
            eyeRx.setPrism(prism);
            eyeRx.setNearPrism(prism);
            eyeRx.setDistancePrism(prism);
                
            refractedRxDto.setLeftEye(eyeRx);
        }
    }

    @Nested
    class RxPrismVerticalValidation { 
        @ParameterizedTest(name = "validWhenRxPrismVertical = {0}")
        @ValueSource(strings = {"0.25 Down", "5.50 Up", "20 Down"})
        @NullSource
        void valid(String value) {
            setupRxPrismH(value);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @ParameterizedTest(name = "invalidWhenRxPrismVertical = {0}")
        @ValueSource(strings = {"0 Up", "5.50 X", "21 Down", "3.45 Down"})
        void invalid(String value) {
            setupRxPrismH(value);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        private void setupRxPrismH(String value) {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setVertical(value);
            
            eyeRx.setPrism(prism);
            eyeRx.setNearPrism(prism);
            eyeRx.setDistancePrism(prism);
                
            refractedRxDto.setLeftEye(eyeRx);
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
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));

            var newEyeRx = new EyeRxDto();
            var newPrism = new PrismDto();
            newPrism.setHorizontal("3.25 Out");

            newEyeRx.setPrism(newPrism);
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(newEyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }

        @Test
        void prismVerticalDirectionMustBeOpposite() {
            var eyeRx = new EyeRxDto();
            var prism = new PrismDto();
            prism.setVertical("3.25 Up");

            eyeRx.setPrism(prism);
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));

            var newEyeRx = new EyeRxDto();
            var newPrism = new PrismDto();
            newPrism.setVertical("3.25 Down");

            newEyeRx.setPrism(newPrism);
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(newEyeRx);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));
        }

        @Test
        void cylinderSignMustBeSame() {
            var eyeRx = new EyeRxDto();
            eyeRx.setCylinder("+3.50");
            eyeRx.setAxis(1f);
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertDoesNotThrow(() -> validator.validate(refractedRxDto));

            var newEyeRx = new EyeRxDto();
            newEyeRx.setCylinder("-2.25");
            newEyeRx.setAxis(1f);
            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(newEyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }
        
        @Test
        void sphereCannotHaveBalOnBothEyes() {
            var eyeRx = new EyeRxDto();
            eyeRx.setSphere(BALANCED);

            refractedRxDto.setLeftEye(eyeRx);
            refractedRxDto.setRightEye(eyeRx);

            assertThrows(ValidationException.class, () -> validator.validate(refractedRxDto));
        }
    }
}
