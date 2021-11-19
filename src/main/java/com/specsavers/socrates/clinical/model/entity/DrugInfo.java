package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class DrugInfo {
    @Column(name = "iop_drug_time")
    String time;

    @Column(name = "iop_drug_desc")
    String drugUsed;

    @Column(name = "iop_drug_batch")
    String batchNo;

    @Column(name = "iop_drug_exp_date")
    String expiryDate;
}
