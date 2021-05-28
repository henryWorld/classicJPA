package com.specsavers.socrates.clinical.resolvers;

import java.time.OffsetDateTime;

import com.specsavers.socrates.clinical.types.PrescribedEyeRX;
import com.specsavers.socrates.clinical.types.PrescribedRX;
import com.specsavers.socrates.clinical.types.Prism;
import com.specsavers.socrates.clinical.types.UnaidedVA;

import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Component
public class Query implements GraphQLQueryResolver {
    
    // TODO: Create repositorie to  get prescrion data
    public PrescribedRX prescribedRX(String id, Integer testRoomNumber) {        
        var prism = Prism.builder()
            .horizontal("2.00 In")
            .vertical("1.00 Up")
            .build();

        var eyeRX = PrescribedEyeRX.builder()
            .axis(180)
            .sphere("+1.50")
            .cylinder("-1.25")
            .distanceVisualAcuity("20/20")
            .nearVisualAcuity("6/6")
            .pupillaryDistance(34.5f)
            .interAddtion(2.25f)
            .nearAddition(0.25f)
            .distancePrism(prism)
            .nearPrism(prism)
            .build();

        var unaidedVA = UnaidedVA.builder()
            .binocular("6/6")
            .leftEye("6/5")
            .rightEye("6/6")
            .build();

        var prescribedRX  = PrescribedRX.builder()
            .id(id)
            .clinicianName("Clinician Name")
            .leftEye(eyeRX)
            .rightEye(eyeRX)
            .bvd(5.3f)
            .distanceBinVisualAcuity("20/20")
            .dispenseNotes("Some Dispense Notes")
            .recallPeriod(24)
            .testDate(OffsetDateTime.parse("2020-05-25T10:15:30+01:00"))
            .testRoomNumber(77)
            .unaidedVisualAcuity(unaidedVA)
            .recommendations("Always buy your spectacles on Specsavers")
            .build();

        return prescribedRX;
    }
}
