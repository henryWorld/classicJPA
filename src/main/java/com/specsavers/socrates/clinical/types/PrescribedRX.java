package com.specsavers.socrates.clinical.types;

public class PrescribedRX {
    public String id;
    public String clinicianName ;
    public Integer testRoomNumber;
    public PrescribedEyeRX rightEye;
    public PrescribedEyeRX leftEye;
    public String distanceBinVisualAcuity;
    public UnaidedVA unaidedVisualAcuity;
    public Float bvd;
    public Integer recallPeriod;
    public String testDate;
    public String dispenseNotes;
    public String recommendations;
}
