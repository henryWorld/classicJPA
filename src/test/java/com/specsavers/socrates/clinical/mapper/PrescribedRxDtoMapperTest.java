package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.legacy.model.Name;
import com.specsavers.socrates.clinical.legacy.model.OptionRecommendation;
import com.specsavers.socrates.clinical.legacy.model.PrescribedRX;
import com.specsavers.socrates.clinical.legacy.model.Record;
import com.specsavers.socrates.clinical.legacy.model.RefractedRX;
import com.specsavers.socrates.clinical.legacy.model.SightTest;
import com.specsavers.socrates.clinical.legacy.model.SpecificAdd;
import com.specsavers.socrates.clinical.legacy.model.Staff;
import com.specsavers.socrates.clinical.legacy.model.rx.EyeRX;
import com.specsavers.socrates.clinical.legacy.model.rx.Prism;
import com.specsavers.socrates.clinical.legacy.model.rx.RX;
import com.specsavers.socrates.clinical.legacy.model.rx.UnaidedVA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PrescribedRxDtoMapperTest {

    private PrescribedRxMapper sut;

    @BeforeEach
    void beforeEach() {
        sut = PrescribedRxMapper.INSTANCE;
    }

    @Test
    void mapsRx() {
        // given
        var entity = new PrescribedRX();
        entity.setId(9);
        var test = new SightTest();
        test.setTrNumber(4);
        entity.setSightTest(test);
        var rx = new RX();
        rx.setDistanceBinVisualAcuity("bin");
        rx.setBvd(1.23f);
        var record = new Record();
        var testDate = LocalDateTime.of(1991, 2, 7, 1,2,3);
        record.setCustomerArrivalTime(testDate);
        test.setRecord(record);
        test.setDispenseNotes("notes");
        entity.setRx(rx);

        // when
        var actual = sut.fromEntity(entity);

        // then
        assertEquals(9, actual.getId());
        assertEquals(4, actual.getTestRoomNumber());
        assertEquals("bin", actual.getDistanceBinVisualAcuity());
        assertEquals(1.23f, actual.getBvd());
        assertEquals(OffsetDateTime.of(testDate, ZoneOffset.UTC), actual.getTestDate());
        assertEquals("notes", actual.getDispenseNotes());
    }

    @Test
    void mapsName() {
        // given
        var entity = new PrescribedRX();
        var name = new Name();
        name.setFirstName("Bob");
        name.setLastName("Bobson");
        var staff = new Staff();
        staff.setName(name);
        var test = new SightTest();
        test.setStaff(staff);
        entity.setSightTest(test);

        // when
        var actual = sut.fromEntity(entity);

        // then
        assertEquals("Bob Bobson", actual.getClinicianName());
    }

    @Test
    void mapsRecommendations() {
        // given
        var entity = new PrescribedRX();
        var test = new SightTest();
        var recommendations = asList(optionRecommendation("one"), optionRecommendation("two"));
        test.setOptionRecommendations(recommendations);
        entity.setSightTest(test);

        // when
        var actual = sut.fromEntity(entity);

        // then
        assertEquals("one, two", actual.getRecommendations());
    }

    @Test
    void mapsLeftEye() {
        // given
        var entity = new PrescribedRX();
        RX rx = new RX();
        EyeRX eye = new EyeRX();
        eye.setSphere("sphere");
        eye.setCylinder("cylinder");
        eye.setAxis(1.23f);
        eye.setDistanceVisualAcuity("dva");
        eye.setNearVisualAcuity("nva");
        eye.setPupillaryDistance(4.56f);
        eye.setNearAddition(7.89f);
        eye.setInterAddition(10.12f);
        eye.setDistancePrism(prism("dh", "dv"));
        eye.setNearPrism(prism("nh", "nv"));
        rx.setLeftEye(eye);
        entity.setRx(rx);

        // when
        var actual = sut.fromEntity(entity).getLeftEye();

        // then
        assertEquals("sphere", actual.getSphere());
        assertEquals("cylinder", actual.getCylinder());
        assertEquals(1.23f, actual.getAxis());
        assertEquals("dva", actual.getDistanceVisualAcuity());
        assertEquals("nva", actual.getNearVisualAcuity());
        assertNull(actual.getVisualAcuity());
        assertEquals(4.56f, actual.getPupillaryDistance());
        assertEquals(7.89f, actual.getNearAddition());
        assertEquals(10.12f, actual.getInterAddition());
        assertNull(actual.getAddition());
        assertEquals("dh", actual.getDistancePrism().getHorizontal());
        assertEquals("dv", actual.getDistancePrism().getVertical());
        assertEquals("nh", actual.getNearPrism().getHorizontal());
        assertEquals("nv", actual.getNearPrism().getVertical());
        assertNull(actual.getPrism());
    }

    @Test
    void mapsUnaidedVisualAcuity() {
        // given
        var entity = new PrescribedRX();
        var rx = new RX();
        var va = new UnaidedVA();
        va.setLeftEye("left");
        va.setRightEye("right");
        va.setBinocular("binocular");
        rx.setUnaidedVisualAcuity(va);
        entity.setRx(rx);

        // when
        var actual = sut.fromEntity(entity).getUnaidedVisualAcuity();

        // then
        assertEquals("left", actual.getLeftEye());
        assertEquals("right", actual.getRightEye());
        assertEquals("binocular", actual.getBinocular());
    }

    @Test
    void mapsSpecificAdd() {
        var specificAdd = new SpecificAdd();
        specificAdd.setLeftEye(1.2F);
        specificAdd.setRightEye(2.3F);
        specificAdd.setReason("This is the reason");

        var refractedRX = new RefractedRX();
        refractedRX.setSpecificAddition(specificAdd);

        var sightTest = new SightTest();
        sightTest.setRefractedRx(refractedRX);

        var entity = new PrescribedRX();
        entity.setSightTest(sightTest);

        var actual = sut.fromEntity(entity).getSpecificAddition();

        assertEquals(1.2F, actual.getLeftEye());
        assertEquals(2.3F, actual.getRightEye());
        assertEquals("This is the reason", actual.getReason());
    }

    private static OptionRecommendation optionRecommendation(String text) {
        var recommendation = new OptionRecommendation();
        recommendation.setText(text);
        return recommendation;
    }

    private static Prism prism(String horizontal, String vertical) {
        Prism prism = new Prism();
        prism.setHorizontal(horizontal);
        prism.setVertical(vertical);
        return prism;
    }
}