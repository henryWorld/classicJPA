package com.specsavers.socrates.clinical.model.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public final class PrescribedRxDto {
    private final Object id;
    private final String clinicianName;
    private final Integer testRoomNumber;
    private final PrescribedEyeRxDto rightEye;
    private final PrescribedEyeRxDto leftEye;
    private final String distanceBinVisualAcuity;
    private final UnaidedVisualAcuityDto unaidedVisualAcuity;
    private final Float bvd;
    private final Integer recallPeriod;
    private final OffsetDateTime testDate;
    private final String dispenseNotes;
    private final String recommendations;
}
