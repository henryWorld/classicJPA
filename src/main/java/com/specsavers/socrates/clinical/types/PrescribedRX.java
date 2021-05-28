package com.specsavers.socrates.clinical.types;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class PrescribedRX {
    @NonNull
    private String id;
    private String clinicianName ;
    private Integer testRoomNumber;
    private PrescribedEyeRX rightEye;
    private PrescribedEyeRX leftEye;
    private String distanceBinVisualAcuity;
    private UnaidedVA unaidedVisualAcuity;
    private Float bvd;
    private Integer recallPeriod;
    private OffsetDateTime testDate;
    private String dispenseNotes;
    private String recommendations;
}
