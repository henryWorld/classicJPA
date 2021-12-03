package com.specsavers.socrates.clinical.model.entity;

import com.specsavers.socrates.clinical.model.validator.OptionRecommendationsValidation;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
@OptionRecommendationsValidation
public class OptionRecommendations {
    @Enumerated(EnumType.STRING)
    @Column(name = "opt_rx_option_type")
    RxOptionType rxOptionType;

    @Column(name = "opt_refer_to_doctor")
    boolean referToDoctor;

    @Embedded
    Recommendations recommendations;
}
