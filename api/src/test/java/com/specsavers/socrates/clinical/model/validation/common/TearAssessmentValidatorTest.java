package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentEyeDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TearAssessmentValidatorTest {
    private static final int VALID_CHAR_LENGTH = 30;
    //if char length is greater than 30, then it is considered invalid!
    private static final int INVALID_CHAR_LENGTH = 40;
    private TearAssessmentValidator validator;
    private List<TearAssessmentDto> validTearAssessmentFieldValues;
    private List<TearAssessmentDto> invalidTearAssessmentFieldValues;

    @BeforeEach
    void beforeEach() {
        validator = new TearAssessmentValidator();
        validTearAssessmentFieldValues = buildValidTearAssessmentFieldValues();
        invalidTearAssessmentFieldValues = buildInvalidTearAssessmentFieldValues();
    }


    @TestFactory
    Stream<DynamicTest> testValidTearAssessmentFieldValues() {
        return DynamicTest.stream(
                validTearAssessmentFieldValues.iterator(),
                tearAssessmentDto -> "must be valid values",
                tearAssessmentDto -> assertDoesNotThrow(() -> validator.validate(tearAssessmentDto)));
    }

    @TestFactory
    Stream<DynamicTest> testInvalidTearAssessmentFieldValues() {
        return DynamicTest.stream(
                invalidTearAssessmentFieldValues.iterator(),
                tearAssessmentDto -> "must be invalid values",
                tearAssessmentDto -> assertThrows(ValidationException.class,
                        () -> validator.validate(tearAssessmentDto)));
    }

    private List<TearAssessmentDto> buildValidTearAssessmentFieldValues() {
        return Arrays.asList(
                TearAssessmentDto.builder()
                        .rightEye(TearAssessmentEyeDto.builder()
                                .tbut("strings")
                                .prism("prism")
                                .scope("strings")
                                .schirmer("strings")
                                .build())
                        .leftEye(TearAssessmentEyeDto.builder()
                                .tbut("BM")
                                .prism("strings")
                                .scope("strings")
                                .schirmer("strings")
                                .build())
                        .observations("Strings")
                        .build(),
                TearAssessmentDto.builder()
                        .rightEye(TearAssessmentEyeDto.builder()
                                .tbut("two")
                                .prism("prism")
                                .scope("strings")
                                .schirmer("beep")
                                .build())
                        .leftEye(TearAssessmentEyeDto.builder()
                                .tbut("GEE")
                                .prism("strings")
                                .scope("tree")
                                .schirmer(TestHelpers.stringOfLength(VALID_CHAR_LENGTH))
                                .build())
                        .observations("Strings")
                        .build());
    }

    private List<TearAssessmentDto> buildInvalidTearAssessmentFieldValues() {
        return Arrays.asList(
                TearAssessmentDto.builder()
                        .rightEye(TearAssessmentEyeDto.builder()
                                .tbut(" ")
                                .prism(" ")
                                .scope(TestHelpers.stringOfLength(INVALID_CHAR_LENGTH))
                                .schirmer("strings")
                                .build())
                        .leftEye(TearAssessmentEyeDto.builder()
                                .tbut("BM")
                                .prism(TestHelpers.stringOfLength(INVALID_CHAR_LENGTH))
                                .scope("strings")
                                .schirmer("strings")
                                .build())
                        .observations("Strings")
                        .build(),
                TearAssessmentDto.builder()
                        .rightEye(TearAssessmentEyeDto.builder()
                                .tbut("two")
                                .prism("prism")
                                .scope("strings")
                                .schirmer("beep")
                                .build())
                        .leftEye(TearAssessmentEyeDto.builder()
                                .tbut(TestHelpers.stringOfLength(INVALID_CHAR_LENGTH))
                                .prism("strings")
                                .scope("tree")
                                .schirmer("strings")
                                .build())
                        .observations(" ")
                        .build());
    }
}