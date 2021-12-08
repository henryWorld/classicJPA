package com.specsavers.socrates.clinical.model;


import com.specsavers.socrates.clinical.model.validator.PrescribedRxValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PrescribedRxValidation
public final class PrescribedRxDto {
    private Object id;
    private String clinicianName;
    private Integer testRoomNumber;
    private EyeRxDto rightEye;
    private EyeRxDto leftEye;
    private String distanceBinVisualAcuity;
    private UnaidedVisualAcuityDto unaidedVisualAcuity;
    private Float bvd;
    private SpecificAdditionDto specificAddition;
    private Integer recallPeriod;
    private OffsetDateTime testDate;
    private String dispenseNotes;
    private String recommendations;
    private RxNotesDto notes;
}
