package com.specsavers.socrates.clinical.model.type;

import com.specsavers.socrates.clinical.model.validator.HistoryAndSymptomsValidation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@HistoryAndSymptomsValidation
public class HistoryAndSymptomsDto {
    private String reasonForVisit;
    private String generalHealth;
    private String medication;
    private String ocularHistory;
    private String familyHistory;
    private LifestyleDto lifestyle;
}
