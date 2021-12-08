package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.OptionRecommendationsDto;
import com.specsavers.socrates.clinical.model.RecommendationsDto;
import com.specsavers.socrates.clinical.model.RxOptionTypeDto;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class OptionRecommendationsValidatorTest {
    private OptionRecommendationsDto optionRecommendations;
    private OptionRecommendationsValidator validator;

    @BeforeEach
    void beforeEach() {
        optionRecommendations = new OptionRecommendationsDto();
        validator = new OptionRecommendationsValidator();
    }

    @Nested
    class CombinedValidations {

        @Test
        void testRequiredFields() {
            optionRecommendations.setReferToDoctor(false);
            optionRecommendations.setRxOptionType(null);

            var exception = assertThrows(ValidationException.class, () -> validator.validate(optionRecommendations));
            assertThat(exception).hasMessage("RxOptionType or ReferToDoctor are required");
        }

        @ParameterizedTest(name = "Recommendation = {0}")
        @ValueSource(strings = {"ConsiderContactLens", "Svd", "Svi", "Svn", "Bif", "Vari",
                "UltraClear", "UltraTough", "ThinAndLight", "Tints", "React", "Polar"})
        void testRecommendationsWhenNoRx(String value) {
            optionRecommendations.setRxOptionType(RxOptionTypeDto.NO_RX_REQUIRED);
            var recommendations = new RecommendationsDto();

            ReflectionTestUtils.invokeSetterMethod(recommendations, value, true);
            optionRecommendations.setRecommendations(recommendations);

            var exception = assertThrows(ValidationException.class, () -> validator.validate(optionRecommendations));
            assertThat(exception).hasMessage("No recommendations are allowed when RxOptionType = NO_RX_REQUIRED");
        }
    }
}
