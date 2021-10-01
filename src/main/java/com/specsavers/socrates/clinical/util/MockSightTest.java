package com.specsavers.socrates.clinical.util;
 
import java.time.LocalDate;

import com.specsavers.socrates.clinical.model.entity.CurrentSpecsVA;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.Prism;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.Rx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SpecificAddition;

// Mock data used while task SC-287 not done
public class MockSightTest {
    //Makes Sonar happy
    private static final String DEFAULT_VA = "6/7.5";

    private MockSightTest() {
        throw new IllegalStateException("Utility class");
    }

    public static SightTest makeMockSightTest1(){
        var st = new SightTest();
        var specificAddition = new SpecificAddition();
        var currentSpecsVA = new CurrentSpecsVA();
        var refractedRx = new RefractedRx();
        var prescribedRx = new PrescribedRx();
        
        var prismR = new Prism();
        prismR.setHorizontal("3.25 Out");
        prismR.setVertical("3.25 Up");

        var prismL = new Prism();
        prismL.setHorizontal("1.5 Out");
        prismL.setVertical("1.00 Down");

        var eyeRxBuilder = RxEye.builder()
            .sphere("-2.0")
            .cylinder("+2.25")
            .axis(3f)
            .unaidedVA("6/6.66")
            .nearVA(DEFAULT_VA)
            .distanceVA("6/5")
            .pupillaryDistance(20f)
            .addition(3.25f)
            .nearAddition(1.25f)
            .interAddition(0.50f);
           
        var eyeRxR = eyeRxBuilder
            .nearPrism(prismR)
            .distancePrism(prismR)
            .build();

        var eyeRxL = eyeRxBuilder
            .nearPrism(prismL)
            .distancePrism(prismL)
            .build();

        var rx = Rx.builder()
            .bvd(5f)
            .distanceBinVA("6/6")
            .unaidedBinVA("6/10")
            .leftEye(eyeRxL)
            .rightEye(eyeRxR)
            .build();


        specificAddition.setLeftEye(6f);
        specificAddition.setRightEye(5f);
        specificAddition.setReason("Addition Reason 1");

        currentSpecsVA.setLeftEye("6/60");
        currentSpecsVA.setRightEye("6/60");

        refractedRx.setRx(rx);
        refractedRx.setSpecificAddition(specificAddition);
        refractedRx.setCurrentSpecsVA(currentSpecsVA);

        prescribedRx.setRx(rx);
        prescribedRx.setRecallPeriod(12);

        st.setRefractedRx(refractedRx);
        st.setPrescribedRx(prescribedRx);
        st.setCreationDate(LocalDate.parse("2021-09-03"));

        return st;
    }

    public static SightTest makeMockSightTest2(){
        var st = new SightTest();
        var specificAddition = new SpecificAddition();
        var currentSpecsVA = new CurrentSpecsVA();
        var refractedRx = new RefractedRx();
        var prescribedRx = new PrescribedRx();
        
        var prismR = new Prism();
        prismR.setHorizontal("30.50 In");
        prismR.setVertical("10.00 Down");

        var prismL = new Prism();
        prismL.setHorizontal("4.5 In");
        prismL.setVertical("10.00 Up");

        var eyeRxBuilder = RxEye.builder()
            .sphere("+4.00")
            .cylinder("-0.25")
            .axis(60f)
            .unaidedVA("6/10")
            .nearVA(DEFAULT_VA)
            .distanceVA(DEFAULT_VA)
            .pupillaryDistance(25f)
            .addition(3.25f)
            .nearAddition(6f)
            .interAddition(6f);

        var eyeRxR = eyeRxBuilder
            .nearPrism(prismR)
            .distancePrism(prismR)
            .build();

        var eyeRxL = eyeRxBuilder
            .nearPrism(prismL)
            .distancePrism(prismL)
            .build();

        var rx = Rx.builder()
            .bvd(7.5f)
            .distanceBinVA("6/6")
            .unaidedBinVA(DEFAULT_VA)
            .leftEye(eyeRxL)
            .rightEye(eyeRxR)
            .build();


        specificAddition.setLeftEye(6.25f);
        specificAddition.setRightEye(5.25f);
        specificAddition.setReason("Addition Reason 2");

        currentSpecsVA.setLeftEye("6/15");
        currentSpecsVA.setRightEye("6/15");

        refractedRx.setRx(rx);
        refractedRx.setSpecificAddition(specificAddition);
        refractedRx.setCurrentSpecsVA(currentSpecsVA);

        prescribedRx.setRx(rx);
        prescribedRx.setRecallPeriod(24);

        st.setRefractedRx(refractedRx);
        st.setPrescribedRx(prescribedRx);
        st.setCreationDate(LocalDate.parse("2020-05-11"));

        return st;
    }

    public static SightTest makeMockSightTest3(){
        var st = new SightTest();
        var specificAddition = new SpecificAddition();
        var currentSpecsVA = new CurrentSpecsVA();
        var refractedRx = new RefractedRx();
        var prescribedRx = new PrescribedRx();
        
        var prismR = new Prism();
        prismR.setHorizontal("1.5 In");
        prismR.setVertical("2.25 Down");

        var prismL = new Prism();
        prismL.setHorizontal("1.5 In");
        prismL.setVertical("2.25 Up");

        var eyeRxBuilder = RxEye.builder()
            .sphere("-3.50")
            .cylinder("+10")
            .axis(120.5f)
            .unaidedVA("6/24")
            .nearVA("6/12")
            .distanceVA("6/4.8")
            .pupillaryDistance(25.5f)
            .addition(3.25f)
            .nearAddition(6.75f)
            .interAddition(6.75f);

        var eyeRxR = eyeRxBuilder
            .nearPrism(prismR)
            .distancePrism(prismR)
            .build();

        var eyeRxL = eyeRxBuilder
            .nearPrism(prismL)
            .distancePrism(prismL)
            .build();

        var rx = Rx.builder()
            .bvd(10f)
            .distanceBinVA("6/6.66")
            .unaidedBinVA("6/20")
            .leftEye(eyeRxL)
            .rightEye(eyeRxR)
            .build();


        specificAddition.setLeftEye(6.25f);
        specificAddition.setRightEye(5.25f);
        specificAddition.setReason("Addition Reason 3");

        currentSpecsVA.setLeftEye("6/6");
        currentSpecsVA.setRightEye("6/6");

        refractedRx.setRx(rx);
        refractedRx.setSpecificAddition(specificAddition);
        refractedRx.setCurrentSpecsVA(currentSpecsVA);

        prescribedRx.setRx(rx);
        prescribedRx.setRecallPeriod(36);

        st.setRefractedRx(refractedRx);
        st.setPrescribedRx(prescribedRx);
        st.setCreationDate(LocalDate.parse("2019-11-27"));

        return st;
    }
}
