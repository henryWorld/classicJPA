package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Embeddable
public class ObjectiveAndIop {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iop_left_eye_id")
    IopEye leftEye;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "iop_right_eye_id")
    IopEye rightEye;

    @Column(name = "iop_time")
    String time;

    @Column(name = "iop_notes")
    String notes;

    @Embedded
    @AttributeOverride(name = "time", column = @Column(name = "iop_drug_time"))
    @AttributeOverride(name = "drugUsed", column = @Column(name = "iop_drug_desc"))
    @AttributeOverride(name = "batchNo", column = @Column(name = "iop_drug_batch"))
    @AttributeOverride(name = "expiryDate", column = @Column(name = "iop_drug_exp_date"))
    DrugInfo drugInfo;
}
