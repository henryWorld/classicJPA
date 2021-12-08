package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.OptionRecommendationsValidation;
import lombok.Data;

@Data
@OptionRecommendationsValidation
public class OptionRecommendationsDto {
    RxOptionTypeDto rxOptionType;
    boolean referToDoctor;
    RecommendationsDto recommendations;
}
