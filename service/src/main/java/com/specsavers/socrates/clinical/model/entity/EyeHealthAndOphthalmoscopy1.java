package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Embeddable
public class EyeHealthAndOphthalmoscopy1 {

    @Column(name = "health_direct")
    Boolean direct;

    @Column(name = "health_indirect")
    Boolean indirect;

    @Column(name = "health_volk")
    Boolean volk;

    @Column(name = "health_dilated")
    Boolean dilated;

    @Column(name = "health_slit_lamp")
    Boolean slitLamp;

    @Column(name = "health_ext_eye_right")
    String externalEyeRight;

    @Column(name = "health_anterior_chamber_right")
    String anteriorChamberRight;

    @Column(name = "health_ext_eye_left")
    String externalEyeLeft;

    @Column(name = "health_anterior_chamber_left")
    String anteriorChamberLeft;

    @Embedded
    EyeHealthDrugInfo drugInfoEyeHealth;
}