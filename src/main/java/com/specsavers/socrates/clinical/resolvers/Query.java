package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.types.PrescribedEyeRX;
import com.specsavers.socrates.clinical.types.PrescribedRX;
import com.specsavers.socrates.clinical.types.Prism;
import com.specsavers.socrates.clinical.types.UnaidedVA;

import graphql.kickstart.tools.GraphQLQueryResolver;

public class Query implements GraphQLQueryResolver {
    
    // TODO: Create repositorie to  get prescrion data
    public PrescribedRX prescribedRX(String id, Integer testRoomNumber) {
        var prescribedRX  = new PrescribedRX(id);
        var eyeRX = new PrescribedEyeRX();
        var unaidedVA = new UnaidedVA();
        var prism = new Prism();

        prism.setHorizontal("2.00 In");
        prism.setVertical("1.00 Up");

        eyeRX.setAxis(180);
        eyeRX.setSphere("+1.50");
        eyeRX.setNearVisualAcuity("6/6");
        eyeRX.setDistancePrism(prism);

        unaidedVA.setBinocular("6/6");
        unaidedVA.setLeftEye("6/5");
        unaidedVA.setRightEye("6/6");

        prescribedRX.setClinicianName("Fake Object");
        prescribedRX.setLeftEye(eyeRX);
        prescribedRX.setRightEye(eyeRX);
        prescribedRX.setDispenseNotes("Some Notes");
        prescribedRX.setTestRoomNumber(77);
        prescribedRX.setUnaidedVisualAcuity(unaidedVA);

        return prescribedRX;
    }
}
