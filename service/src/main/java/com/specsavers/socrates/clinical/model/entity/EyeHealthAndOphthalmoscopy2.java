package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class EyeHealthAndOphthalmoscopy2 {

    @Column(name = "health_lens_right")
    String lensRight;

    @Column(name = "health_vitreous_right")
    String vitreousRight;

    @Column(name = "health_lens_left")
    String lensLeft;

    @Column(name = "health_vitreous_left")
    String vitreousLeft;

}
