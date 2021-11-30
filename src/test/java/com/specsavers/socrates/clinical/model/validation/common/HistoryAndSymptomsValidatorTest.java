package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.type.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.type.LifestyleDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.specsavers.socrates.clinical.util.StaticHelpers.stringOfLength;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HistoryAndSymptomsValidatorTest {

    private HistoryAndSymptomsDto historyAndSymptoms;
    private HistoryAndSymptomsValidator sut;

    @BeforeEach
    void beforeEach() {
        historyAndSymptoms = new HistoryAndSymptomsDto();
        historyAndSymptoms.setLifestyle(new LifestyleDto());
        sut = new HistoryAndSymptomsValidator();
    }

    @ParameterizedTest(name = "validWhenReasonForVisitLength={0}")
    @ValueSource(ints = {1, 999, 1000})
    void validWhenReasonForVisitLengthX(int length) {
        // given
        historyAndSymptoms.setReasonForVisit(stringOfLength(length));

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @Test
    void invalidWhenReasonForVisitEmptyString() {
        // given
        historyAndSymptoms.setReasonForVisit("");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("reasonForVisit must not be shorter than 1 characters", actual.getMessage());
    }

    @Test
    void invalidWhenReasonForVisitBlankString() {
        // given
        historyAndSymptoms.setReasonForVisit(" ");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("reasonForVisit must not be blank", actual.getMessage());
    }

    @ParameterizedTest(name = "invalidWhenReasonForVisitLongLength={0}")
    @ValueSource(ints = {1001, 1002})
    void invalidWhenReasonForVisitLongLengthX(int length) {
        // given
        historyAndSymptoms.setReasonForVisit(stringOfLength(length));

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("reasonForVisit must not be longer than 1000 characters", actual.getMessage());
    }

    @ParameterizedTest(name = "validWhenGeneralHealthLength={0}")
    @ValueSource(ints = {1, 999, 1000})
    void validWhenGeneralHealthLengthX(int length) {
        // given
        historyAndSymptoms.setGeneralHealth(stringOfLength(length));

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @Test
    void invalidWhenGeneralHealthEmptyString() {
        // given
        historyAndSymptoms.setGeneralHealth("");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("generalHealth must not be shorter than 1 characters", actual.getMessage());
    }

    @Test
    void invalidWhenGeneralHealthBlankString() {
        // given
        historyAndSymptoms.setGeneralHealth(" ");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("generalHealth must not be blank", actual.getMessage());
    }

    @ParameterizedTest(name = "invalidWhenGeneralHealthLongLength={0}")
    @ValueSource(ints = {1001, 1002})
    void invalidWhenGeneralHealthLongLengthX(int length) {
        // given
        historyAndSymptoms.setGeneralHealth(stringOfLength(length));

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("generalHealth must not be longer than 1000 characters", actual.getMessage());
    }

    @ParameterizedTest(name = "validWhenMedicationLength={0}")
    @ValueSource(ints = {1, 999, 1000})
    void validWhenMedicationLengthX(int length) {
        // given
        historyAndSymptoms.setMedication(stringOfLength(length));

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @Test
    void invalidWhenMedicationEmptyString() {
        // given
        historyAndSymptoms.setMedication("");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("medication must not be shorter than 1 characters", actual.getMessage());
    }

    @Test
    void invalidWhenMedicationBlankString() {
        // given
        historyAndSymptoms.setMedication(" ");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("medication must not be blank", actual.getMessage());
    }

    @ParameterizedTest(name = "invalidWhenMedicationLongLength={0}")
    @ValueSource(ints = {1001, 1002})
    void invalidWhenMedicationLongLengthX(int length) {
        // given
        historyAndSymptoms.setMedication(stringOfLength(length));

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("medication must not be longer than 1000 characters", actual.getMessage());
    }

    @ParameterizedTest(name = "validWhenOcularHistoryLength={0}")
    @ValueSource(ints = {1, 999, 1000})
    void validWhenOcularHistoryLengthX(int length) {
        // given
        historyAndSymptoms.setOcularHistory(stringOfLength(length));

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @Test
    void invalidWhenOcularHistoryEmptyString() {
        // given
        historyAndSymptoms.setOcularHistory("");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("ocularHistory must not be shorter than 1 characters", actual.getMessage());
    }

    @Test
    void invalidWhenOcularHistoryBlankString() {
        // given
        historyAndSymptoms.setOcularHistory(" ");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("ocularHistory must not be blank", actual.getMessage());
    }

    @ParameterizedTest(name = "invalidWhenOcularHistoryLongLength={0}")
    @ValueSource(ints = {1001, 1002})
    void invalidWhenOcularHistoryLongLengthX(int length) {
        // given
        historyAndSymptoms.setOcularHistory(stringOfLength(length));

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("ocularHistory must not be longer than 1000 characters", actual.getMessage());
    }

    @ParameterizedTest(name = "validWhenFamilyHistoryLength={0}")
    @ValueSource(ints = {1, 999, 1000})
    void validWhenFamilyHistoryLengthX(int length) {
        // given
        historyAndSymptoms.setFamilyHistory(stringOfLength(length));

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @Test
    void invalidWhenFamilyHistoryEmptyString() {
        // given
        historyAndSymptoms.setFamilyHistory("");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("familyHistory must not be shorter than 1 characters", actual.getMessage());
    }

    @Test
    void invalidWhenFamilyHistoryBlankString() {
        // given
        historyAndSymptoms.setFamilyHistory(" ");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("familyHistory must not be blank", actual.getMessage());
    }

    @ParameterizedTest(name = "invalidWhenFamilyHistoryLongLength={0}")
    @ValueSource(ints = {1001, 1002})
    void invalidWhenFamilyHistoryLongLength(int length) {
        // given
        historyAndSymptoms.setFamilyHistory(stringOfLength(length));

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("familyHistory must not be longer than 1000 characters", actual.getMessage());
    }

    @ParameterizedTest(name = "validWhenVduHoursPerDay={0}")
    @ValueSource(ints = {1, 2, 23, 24})
    void validWhenVduHoursPerDayX(int value) {
        // given
        historyAndSymptoms.getLifestyle().setVduHoursPerDay(value);

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @ParameterizedTest(name = "invalidWhenVduHoursPerDay={0}")
    @CsvSource({
            "0,lifestyle.vduHoursPerDay must be between 1 and 24",
            "25,lifestyle.vduHoursPerDay must be between 1 and 24"
    })
    void invalidWhenVduHoursPerDayX(int value, String message) {
        // given
        historyAndSymptoms.getLifestyle().setVduHoursPerDay(value);

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals(message, actual.getMessage());
    }

    @ParameterizedTest(name = "validWhenOccupationLength={0}")
    @ValueSource(ints = {1, 59, 60})
    void validWhenOccupationLengthX(int length) {
        // given
        historyAndSymptoms.getLifestyle().setOccupation(stringOfLength(length));

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @Test
    void invalidWhenOccupationEmptyString() {
        // given
        historyAndSymptoms.getLifestyle().setOccupation("");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("lifestyle.occupation must not be shorter than 1 characters", actual.getMessage());
    }

    @Test
    void invalidWhenOccupationBlankString() {
        // given
        historyAndSymptoms.getLifestyle().setOccupation(" ");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("lifestyle.occupation must not be blank", actual.getMessage());
    }

    @ParameterizedTest(name = "invalidWhenOccupationLongLength={0}")
    @ValueSource(ints = {61, 62})
    void invalidWhenOccupationLongLengthX(int length) {
        // given
        historyAndSymptoms.getLifestyle().setOccupation(stringOfLength(length));

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("lifestyle.occupation must not be longer than 60 characters", actual.getMessage());
    }

    @ParameterizedTest(name = "validWhenHobbiesLength={0}")
    @ValueSource(ints = {1, 59, 60})
    void validWhenHobbiesLengthX(int length) {
        // given
        historyAndSymptoms.getLifestyle().setHobbies(stringOfLength(length));

        // when
        sut.validate(historyAndSymptoms);

        // then
        // no exception
    }

    @Test
    void invalidWhenHobbiesEmptyString() {
        // given
        historyAndSymptoms.getLifestyle().setHobbies("");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("lifestyle.hobbies must not be shorter than 1 characters", actual.getMessage());
    }

    @Test
    void invalidWhenHobbiesBlankString() {
        // given
        historyAndSymptoms.getLifestyle().setHobbies(" ");

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("lifestyle.hobbies must not be blank", actual.getMessage());
    }

    @ParameterizedTest(name = "invalidWhenHobbiesLongLength={0}")
    @ValueSource(ints = {61, 62})
    void invalidWhenHobbiesLongLengthX(int length) {
        // given
        historyAndSymptoms.getLifestyle().setHobbies(stringOfLength(length));

        // when
        var actual = assertThrows(ValidationException.class, () -> sut.validate(historyAndSymptoms));

        // then
        assertEquals("lifestyle.hobbies must not be longer than 60 characters", actual.getMessage());
    }
}
