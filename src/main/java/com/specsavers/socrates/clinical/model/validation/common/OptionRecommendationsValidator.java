package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.entity.OptionRecommendations;
import com.specsavers.socrates.clinical.model.entity.RxOptionType;
import com.specsavers.socrates.common.exception.ValidationException;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class OptionRecommendationsValidator extends Validator<OptionRecommendations> {
    @Override
    public void validate(OptionRecommendations item) {
        checkNoRx(item);
        checkRequiredFields(item);
    }
    private void checkRequiredFields(OptionRecommendations item) {
        if (item != null && item.getRxOptionType() == null && !item.isReferToDoctor()){
            throw new ValidationException("RxOptionType or ReferToDoctor are required");
        }
    }

    private void checkNoRx(OptionRecommendations item) {
        if (item != null && item.getRxOptionType() == RxOptionType.NO_RX_REQUIRED && item.getRecommendations() != null) {
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
