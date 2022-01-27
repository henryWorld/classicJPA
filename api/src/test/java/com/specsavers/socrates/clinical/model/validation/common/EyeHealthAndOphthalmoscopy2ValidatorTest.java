package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy2Dto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.specsavers.socrates.clinical.model.validation.common.TestHelpers.stringOfLength;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EyeHealthAndOphthalmoscopy2ValidatorTest {
    private EyeHealthAndOphthalmoscopy2Dto eyeHealthAndOphthalmoscopy2Dto;
    private final EyeHealthAndOphthalmoscopy2Validator validator = new EyeHealthAndOphthalmoscopy2Validator();

    @BeforeEach
    void beforeEach(){
        eyeHealthAndOphthalmoscopy2Dto = EyeHealthAndOphthalmoscopy2Dto.builder().build();
    }

    @Nested
    class LensRightValidationTests{
        @ParameterizedTest(name = "valid when LensRight length is {0}")
        @ValueSource(ints = {1, 200, 400})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setLensRight(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }

        @ParameterizedTest(name = "invalid when LensRight length is {0}")
        @ValueSource(ints = {0, 401})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setLensRight(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }
    }


    @Nested
    class LensLeftValidationTests{
        @ParameterizedTest(name = "valid when LensLeft length is {0}")
        @ValueSource(ints = {1, 200, 400})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setLensLeft(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }

        @ParameterizedTest(name = "invalid when LensLeft length is {0}")
        @ValueSource(ints = {0, 401})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setLensLeft(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }
    }


    @Nested
    class VitreousRightValidationTests{
        @ParameterizedTest(name = "valid when vitreousRight length is {0}")
        @ValueSource(ints = {1, 175, 250})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setLensRight(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }

        @ParameterizedTest(name = "invalid when vitreousRight length is {0}")
        @ValueSource(ints = {0, 251})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setVitreousRight(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }
    }


    @Nested
    class VitreousLeftValidationTests{
        @ParameterizedTest(name = "valid when VitreousLeft length is {0}")
        @ValueSource(ints = {1, 175, 250})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setVitreousLeft(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }

        @ParameterizedTest(name = "invalid when VitreousLeft length is {0}")
        @ValueSource(ints = {0, 251})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy2Dto.setVitreousLeft(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy2Dto));
        }
    }
}