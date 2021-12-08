package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.OptionRecommendationsDto;
import com.specsavers.socrates.clinical.model.RxOptionTypeDto;
import com.specsavers.socrates.common.exception.ValidationException;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class OptionRecommendationsValidator extends Validator<OptionRecommendationsDto> {
    @Override
    public void validate(OptionRecommendationsDto item) {
        checkNoRx(item);
        checkRequiredFields(item);
    }
    private void checkRequiredFields(OptionRecommendationsDto item) {
        if (item != null && item.getRxOptionType() == null && !item.isReferToDoctor()){
            throw new ValidationException("RxOptionType or ReferToDoctor are required");
        }
    }

    private void checkNoRx(OptionRecommendationsDto item) {
        if (item != null && item.getRxOptionType() == RxOptionTypeDto.NO_RX_REQUIRED && item.getRecommendations() != null) {
            var r = item.getRecommendations();
            var hasAnyRecommendation =
                    r.isConsiderContactLens() ||
                    r.isSvd() ||
                    r.isSvi() ||
                    r.isSvn() ||
                    r.isBif() ||
                    r.isVari() ||
                    r.isUltraClear() ||
                    r.isUltraTough() ||
                    r.isThinAndLight() ||
                    r.isTints() ||
                    r.isReact() ||
                    r.isPolar();

            if (hasAnyRecommendation) {
                throw new ValidationException("No recommendations are allowed when RxOptionType = NO_RX_REQUIRED");
            }
        }
    }
}
