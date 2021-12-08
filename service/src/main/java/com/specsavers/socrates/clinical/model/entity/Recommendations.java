package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Recommendations {
    @Column(name = "opt_consider_contact_lens")
    boolean considerContactLens;

    @Column(name = "opt_svd")
    boolean svd;

    @Column(name = "opt_svi")
    boolean svi;

    @Column(name = "opt_svn")
    boolean svn;

    @Column(name = "opt_bif")
    boolean bif;

    @Column(name = "opt_vari")
    boolean vari;

    @Column(name = "opt_ultra_clear")
    boolean ultraClear;

    @Column(name = "opt_ultra_tough")
    boolean ultraTough;

    @Column(name = "opt_thin_and_light")
    boolean thinAndLight;

    @Column(name = "opt_tints")
    boolean tints;

    @Column(name = "opt_react")
    boolean react;

    @Column(name = "opt_polar")
    boolean polar;
}
