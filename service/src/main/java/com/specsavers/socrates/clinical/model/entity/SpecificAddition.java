package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class SpecificAddition {
    @Column(name = "ref_rx_specific_add_right")
    private Float rightEye;

    @Column(name = "ref_rx_specific_add_left")
    private Float leftEye;
    
    @Column(name = "ref_rx_specific_add_reason")
    private String reason;
}
