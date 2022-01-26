package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class DrugInfo {
    String time;
    String drugUsed;
    String batchNo;
    String expiryDate;
}
