package com.specsavers.socrates.clinical.model.type;

import com.specsavers.socrates.clinical.model.validator.DrugInfoValidation;

import lombok.Data;

@Data
@DrugInfoValidation
public class DrugInfoDto {
    String time;
    String drugUsed;
    String batchNo;
    String expiryDate;
}
