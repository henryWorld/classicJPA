package com.specsavers.socrates.clinical.legacy.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class SpecificAdd {
    @Column(name = "specific_add_right")
    private Float rightEye;

    @Column(name = "specific_add_left")
    private Float leftEye;

    @Column(name = "specific_add_reason")
    private String reason;
}
