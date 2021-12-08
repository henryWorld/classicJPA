package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CurrentSpecsVA {
    @Column(name = "ref_rx_current_specs_va_right")
    private String rightEye;
    @Column(name = "ref_rx_current_specs_va_left")
    private String leftEye;
}
