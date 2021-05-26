package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.types.PrescribedEyeRX;
import com.specsavers.socrates.clinical.types.PrescribedRX;
import com.specsavers.socrates.clinical.types.UnaidedVA;

import graphql.kickstart.tools.GraphQLQueryResolver;

public class Query implements GraphQLQueryResolver {
    
    // TODO: Create repositorie to  get prescrion data
    public PrescribedRX prescribedRX(String id, Integer testRoomNumber) {
        var prescribedRX  = new PrescribedRX();
        var eyeRX = new PrescribedEyeRX();
        var unaidedVA = new UnaidedVA();

        eyeRX.axis = 180;
        eyeRX.sphere = "+1.50";
        eyeRX.nearVisualAcuity = "6/6";

        unaidedVA.binocular = "6/6";
        unaidedVA.leftEye = "6/5";
        unaidedVA.rightEye = "6/6";

        prescribedRX.id = id;
        prescribedRX.clinicianName = "Fake Object";
        prescribedRX.leftEye = eyeRX;
        prescribedRX.rightEye = eyeRX;
        prescribedRX.dispenseNotes = "Some Notes";
        prescribedRX.testRoomNumber = 77;
        prescribedRX.unaidedVisualAcuity = unaidedVA;

        return prescribedRX;
    }
}
