package com.specsavers.socrates.clinical.types;

import lombok.Data;
import lombok.NonNull;

@Data
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
    private String testDate;
    private String dispenseNotes;
    private String recommendations;
}
