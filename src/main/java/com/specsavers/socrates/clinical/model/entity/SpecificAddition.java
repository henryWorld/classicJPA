package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class SpecificAddition {
    @Column(name = "specific_add_right")
    private Float rightEye;

    @Column(name = "specific_add_left")
    private Float leftEye;
    
    @Column(name = "specific_add_reason")
    private String reason;
}
