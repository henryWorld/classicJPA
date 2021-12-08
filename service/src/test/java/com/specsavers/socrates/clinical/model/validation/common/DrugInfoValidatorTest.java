package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static com.specsavers.socrates.clinical.util.StaticHelpers.stringOfLength;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DrugInfoValidatorTest {
    private DrugInfoDto drugInfoDto;
    private DrugInfoValidator validator;
    private final Clock clock = Clock.fixed(Instant.parse("2021-11-01T00:00:00.00Z"), ZoneId.systemDefault());

    @BeforeEach
    void beforeEach() {
        drugInfoDto = new DrugInfoDto();
        // Expiry date mandatory
        drugInfoDto.setExpiryDate("12/2050");
        validator = new DrugInfoValidator(clock);
    }

    @Nested
    class ExpiryDateValidation {
        @ParameterizedTest(name = "validWhenExpiryDate = {0}")
        @ValueSource(strings = {"01/2050","11/2021", "12/9999", "06/2070"})
        @NullSource
        void valid(String date) {
            drugInfoDto.setExpiryDate(date);

            assertDoesNotThrow(() -> validator.validate(drugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenExpiryDate = {0}")
        @ValueSource(strings = {"30/12/2020", "10/2021", "00/2050", "XYZ"})
        void invalid(String date) {
            drugInfoDto.setExpiryDate(date);

            assertThrows(ValidationException.class, () -> validator.validate(drugInfoDto));
        }

        @Test
        void mandatoryWhenOtherFieldsPresent(){
            drugInfoDto.setExpiryDate(null);
            assertDoesNotThrow(() -> validator.validate(drugInfoDto));

            drugInfoDto.setDrugUsed("drugName");
            assertThrows(ValidationException.class, () -> validator.validate(drugInfoDto));
        }
    }

    @Nested
    class TimeValidation {
        @ParameterizedTest(name = "validWhenTime = {0}")
        @ValueSource(strings = {"00:00", "18:30", "23:59"})
        @NullSource
        void valid(String time) {
            drugInfoDto.setTime(time);

            assertDoesNotThrow(() -> validator.validate(drugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenTime = {0}")
        @ValueSource(strings = {"10:00:00", "24:00", "25:00", "XYZ"})
        void invalid(String time) {
            drugInfoDto.setTime(time);

            assertThrows(ValidationException.class, () -> validator.validate(drugInfoDto));
        }
    }

    @Nested
    class BatchNoValidation {
        @ParameterizedTest(name = "validWhenBatchNoLength = {0}")
        @ValueSource(ints = {1, 10, 25})
        void valid(Integer length) {
            drugInfoDto.setBatchNo(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(drugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenBatchNoLength = {0}")
        @ValueSource(ints = {0, 26, 500})
        void invalid(Integer length) {
            drugInfoDto.setBatchNo(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(drugInfoDto));
        }
    }

    @Nested
    class DrugUsedValidation {
        @ParameterizedTest(name = "validWhenDrugUsedLength = {0}")
        @ValueSource(ints = {1, 10, 25})
        void valid(Integer length) {
            drugInfoDto.setDrugUsed(stringOfLength(length));

            assertDoesNotThrow(() -> validator.validate(drugInfoDto));
        }

        @ParameterizedTest(name = "invalidWhenDrugUsedLength = {0}")
        @ValueSource(ints = {0, 26, 500})
        void invalid(Integer length) {
            drugInfoDto.setBatchNo(stringOfLength(length));

            assertThrows(ValidationException.class, () -> validator.validate(drugInfoDto));
        }
    }
}
