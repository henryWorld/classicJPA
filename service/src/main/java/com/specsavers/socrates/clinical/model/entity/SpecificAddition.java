package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class SpecificAddition {
    @Column(name = "ref_rx_specific_add_right", insertable=false, updatable = false)
    private Float rightEye;

    @Column(name = "ref_rx_specific_add_left", insertable = false, updatable = false)
    private Float leftEye;
    
    @Column(name = "ref_rx_specific_add_reason", insertable = false, updatable = false)
    private String reason;
}
