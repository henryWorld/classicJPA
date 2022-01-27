package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Embeddable
public class EyeHealthDrugInfo {
    @Embedded
    @AttributeOverride(name = "time", column = @Column(name = "health_drug_time"))
    @AttributeOverride(name = "drugUsed", column = @Column(name = "health_drug_desc"))
    @AttributeOverride(name = "batchNo", column = @Column(name = "health_drug_batch"))
    @AttributeOverride(name = "expiryDate", column = @Column(name = "health_drug_exp_date"))
    DrugInfo drugInfo;

    @Column(name = "health_drug_pre_pressure")
    String prePressure;

    @Column(name = "health_drug_pre_pressure_time")
    String prePressureTime;

    @Column(name = "health_drug_post_pressure")
    String postPressure;

    @Column(name = "health_drug_post_pressure_time")
    String postPressureTime;

}

