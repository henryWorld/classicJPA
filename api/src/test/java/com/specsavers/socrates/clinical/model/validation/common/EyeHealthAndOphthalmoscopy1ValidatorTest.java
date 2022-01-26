package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy1Dto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.specsavers.socrates.clinical.model.validation.common.TestHelpers.stringOfLength;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class EyeHealthAndOphthalmoscopy1ValidatorTest {
    private EyeHealthAndOphthalmoscopy1Dto eyeHealthAndOphthalmoscopy1Dto;
    private final EyeHealthAndOphthalmoscopy1Validator validator = new EyeHealthAndOphthalmoscopy1Validator();

    @BeforeEach
    void beforeEach() {
        eyeHealthAndOphthalmoscopy1Dto = new EyeHealthAndOphthalmoscopy1Dto();
    }

    @Nested
    class AnteriorChamberRightValidationTests {
        @ParameterizedTest(name = "validWhenAnteriorChamberRight = {0}")
        @ValueSource(ints = {1, 10, 150})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setAnteriorChamberRight(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }

        @ParameterizedTest(name = "invalidWhenAnteriorChamberRight = {0}")
        @ValueSource(ints = {0, 151})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setAnteriorChamberRight(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }
    }

    @Nested
    class AnteriorChamberLeftValidationTests {
        @ParameterizedTest(name = "validWhenAnteriorChamberLeft = {0}")
        @ValueSource(ints = {1, 10, 150})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setAnteriorChamberLeft(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }

        @ParameterizedTest(name = "invalidWhenAnteriorChamberLeft = {0}")
        @ValueSource(ints = {0, 151})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setAnteriorChamberLeft(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }
    }

    @Nested
    class ExternalEyeRightValidationTests {
        @ParameterizedTest(name = "validWhenExternalEyeRight = {0}")
        @ValueSource(ints = {1, 10, 550})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setExternalEyeRight(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }

        @ParameterizedTest(name = "invalidWhenExternalEyeRight = {0}")
        @ValueSource(ints = {0, 551})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setExternalEyeRight(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }
    }

    @Nested
    class ExternalEyeLeftValidationTests {
        @ParameterizedTest(name = "validWhenExternalEyeLeft = {0}")
        @ValueSource(ints = {1, 10, 550})
        @NullSource
        void valid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setExternalEyeLeft(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }

        @ParameterizedTest(name = "invalidWhenExternalEyeLeft = {0}")
        @ValueSource(ints = {0, 551})
        void invalid(Integer length) {
            eyeHealthAndOphthalmoscopy1Dto.setExternalEyeLeft(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthAndOphthalmoscopy1Dto));
        }
    }
}
