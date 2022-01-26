package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeHealthDrugInfoDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.specsavers.socrates.clinical.model.validation.common.TestHelpers.stringOfLength;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EyeHealthDrugInfoValidatorTest {
    private EyeHealthDrugInfoDto eyeHealthDrugInfoDto;

    @Mock
    private DrugInfoValidator drugInfoValidator;

    @InjectMocks
    private EyeHealthDrugInfoValidator validator = new EyeHealthDrugInfoValidator();

    @BeforeEach
    void beforeEach() {
        eyeHealthDrugInfoDto = new EyeHealthDrugInfoDto();
        // Expiry date mandatory
        var drugInfo = new DrugInfoDto();
        drugInfo.setExpiryDate("12/2050");
        eyeHealthDrugInfoDto.setDrugInfo(drugInfo);
    }

    @Test
    void DrugInfoValidationTest(){
        var drugInfo = new DrugInfoDto();
        eyeHealthDrugInfoDto.setDrugInfo(drugInfo);

        validator.validate(eyeHealthDrugInfoDto);
        
        //Actual drugInfoValidator test already exists at DrugInfoValidatorTest class
        verify(drugInfoValidator).validate(drugInfo);
    }

    @Nested
    class PostPressureValidationTests {
        @ParameterizedTest(name = "validWhenPostPressure = {0}")
        @ValueSource(ints = {1, 10, 25})
        @NullSource
        void valid(Integer length) {
            eyeHealthDrugInfoDto.setPostPressure(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenPostPressure = {0}")
        @ValueSource(ints = {0, 26})
        void invalid(Integer length) {
            eyeHealthDrugInfoDto.setPostPressure(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }
    }

    @Nested
    class PrePressureValidationTests {
        @ParameterizedTest(name = "validWhenPrePressure = {0}")
        @ValueSource(ints = {1, 10, 25})
        @NullSource
        void valid(Integer length) {
            eyeHealthDrugInfoDto.setPrePressure(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenPrePressure = {0}")
        @ValueSource(ints = {0, 26})
        void invalid(Integer length) {
            eyeHealthDrugInfoDto.setPrePressure(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }
    }

    @Nested
    class PostPressureTimeValidationTests {
        @ParameterizedTest(name = "validWhenPostPressureTime = {0}")
        @ValueSource(strings = {"00:00", "18:30", "23:59"})
        @NullSource
        void valid(String time) {
            eyeHealthDrugInfoDto.setPostPressureTime(time);

            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenPostPressureTime = {0}")
        @ValueSource(strings = {"10:00:00", "24:00", "25:00", "XYZ"})
        void invalid(String time) {
            eyeHealthDrugInfoDto.setPostPressureTime(time);

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }
    }

    @Nested
    class PrePressureTimeValidationTests {
        @ParameterizedTest(name = "validWhenPrePressureTime = {0}")
        @ValueSource(strings = {"00:00", "18:30", "23:59"})
        @NullSource
        void valid(String time) {
            eyeHealthDrugInfoDto.setPrePressureTime(time);

            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenPrePressureTime = {0}")
        @ValueSource(strings = {"10:00:00", "24:00", "25:00", "XYZ"})
        void invalid(String time) {
            eyeHealthDrugInfoDto.setPrePressureTime(time);

            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }
    }

    @Nested
    class ExpiryDateMandatoryTests {
        @Test
        void mandatoryWhenPostPressureIsPresent(){
            eyeHealthDrugInfoDto.getDrugInfo().setExpiryDate(null);
            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));

            eyeHealthDrugInfoDto.setPostPressure("PostPressure");
            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }

        @Test
        void mandatoryWhenPostPressureTimeIsPresent(){
            eyeHealthDrugInfoDto.getDrugInfo().setExpiryDate(null);
            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));

            eyeHealthDrugInfoDto.setPostPressureTime("PostPressureTime");
            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }

        @Test
        void mandatoryWhenPrePressureIsPresent(){
            eyeHealthDrugInfoDto.getDrugInfo().setExpiryDate(null);
            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));

            eyeHealthDrugInfoDto.setPrePressure("PrePressure");
            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }

        @Test
        void mandatoryWhenPrePressureTimeIsPresent(){
            eyeHealthDrugInfoDto.getDrugInfo().setExpiryDate(null);
            assertDoesNotThrow(() -> validator.validate(eyeHealthDrugInfoDto));

            eyeHealthDrugInfoDto.setPrePressureTime("PrePressureTime");
            assertThrows(ValidationException.class, () -> validator.validate(eyeHealthDrugInfoDto));
        }
    }
}
