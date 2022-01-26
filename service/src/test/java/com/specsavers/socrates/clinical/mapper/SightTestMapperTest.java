package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy1Dto;
import com.specsavers.socrates.clinical.model.EyeHealthDrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeIopDto;
import com.specsavers.socrates.clinical.model.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.LifestyleDto;
import com.specsavers.socrates.clinical.model.ObjectiveAndIopDto;
import com.specsavers.socrates.clinical.model.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.RxNotesDto;
import com.specsavers.socrates.clinical.model.SightTestTypeDto;
import com.specsavers.socrates.clinical.model.entity.CurrentSpecsVA;
import com.specsavers.socrates.clinical.model.entity.DrugInfo;
import com.specsavers.socrates.clinical.model.entity.EyeHealthAndOphthalmoscopy1;
import com.specsavers.socrates.clinical.model.entity.EyeHealthDrugInfo;
import com.specsavers.socrates.clinical.model.entity.ObjectiveAndIop;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.Rx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.entity.RxNotes;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.model.entity.SpecificAddition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_TR_NUMBER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SightTestMapperTest {
    private final SightTestMapper sightTestMapper = Mappers.getMapper(SightTestMapper.class);

    @Test
    void testMapSightTest() {
        var entity = new SightTest();
        var date = LocalDate.now();
        var updated = OffsetDateTime.now();
        var id = UUID.randomUUID();
        entity.setCreationDate(date);
        entity.setId(id);
        entity.setTrNumber(VALID_TR_NUMBER_ID);
        entity.setType(SightTestType.MY_SIGHT_TEST);
        entity.setFamilyHistory("family");
        entity.setOccupation("occupation");
        entity.setUpdated(updated);
        entity.setVersion(4L);

        var actual = sightTestMapper.map(entity);

        assertEquals(date, actual.getCreationDate());
        assertEquals(id, actual.getId());
        assertEquals(VALID_TR_NUMBER_ID, actual.getTrNumber());
        assertEquals(SightTestTypeDto.MY_SIGHT_TEST, actual.getType());
        assertEquals("family", actual.getHistoryAndSymptoms().getFamilyHistory());
        assertEquals("occupation", actual.getHistoryAndSymptoms().getLifestyle().getOccupation());
        assertThat(actual.getVersion()).isEqualTo(4L);
    }

    @Test
    void defaultsHistoryAndSymptomsWhenNull() {
        var entity = new SightTest();

        var dto = sightTestMapper.map(entity);

        var actual = dto.getHistoryAndSymptoms();
        assertNotNull(actual);
        assertNotNull(actual.getLifestyle());
    }

    @Test
    void updatesHistoryAndSymptoms() {
        var entity = new SightTest();
        var history = new HistoryAndSymptomsDto();
        history.setReasonForVisit("reason");
        var lifestyle = new LifestyleDto();
        lifestyle.setHobbies("hobbies");
        history.setLifestyle(lifestyle);

        sightTestMapper.update(entity, history);

        assertEquals("reason", entity.getReasonForVisit());
        assertEquals("hobbies", entity.getHobbies());
    }

    @Test
    void testMapRxEye() {
        var entity = RxEye.builder()
                .addition(5f)
                .axis(1f)
                .cylinder("cylinder")
                .distanceVA("distanceVA")
                .interAddition(4f)
                .nearAddition(9f)
                .nearVA("nearVA")
                .pupillaryDistance(20f)
                .sphere("sphere")
                .balSphere("balSphere")
                .unaidedVA("unaidedVA")
                .visualAcuity("visualAcuity")
                .build();

        var dto = sightTestMapper.map(entity);

        assertEquals("distanceVA", dto.getDistanceVisualAcuity());
        assertEquals("nearVA", dto.getNearVisualAcuity());
        assertThat(dto)
                .usingRecursiveComparison()
                .ignoringFields("distanceVisualAcuity", "nearVisualAcuity")
                .isEqualTo(entity);

    }

    @Nested
    class RefractedRxTests {
        @Test
        void testMapRefractedRx() {
            var rx = Rx.builder()
                    .bvd(5f)
                    .distanceBinVA("distanceBinVA")
                    .notes("notes")
                    .unaidedBinVA("unaidedBinVA")
                    .build();

            var entity = new RefractedRx();
            entity.setRx(rx);
            entity.setNotes(new RxNotes("text", "optomName", LocalDate.now()));

            var dto = sightTestMapper.map(entity);

            assertEquals(rx.getDistanceBinVA(), dto.getDistanceBinVisualAcuity());
            assertEquals(rx.getBvd(), dto.getBvd());
            assertEquals(rx.getUnaidedBinVA(), dto.getUnaidedVisualAcuity().getBinocular());
            assertNull(dto.getRightEye());
            assertNull(dto.getLeftEye());
            assertThat(dto)
                    .usingRecursiveComparison()
                    .ignoringFields("distanceBinVisualAcuity", "bvd", "unaidedVisualAcuity", "rightEye", "leftEye")
                    .isEqualTo(entity);
        }

        @Test
        void testMapSpecificAddition() {
            var entity = new SpecificAddition();
            entity.setLeftEye(7.5f);
            entity.setReason("reason");
            entity.setRightEye(3.25f);

            var dto = sightTestMapper.map(entity);

            assertThat(dto)
                    .usingRecursiveComparison()
                    .isEqualTo(entity);
        }

        @Test
        void testMapCurrentSpecsVA() {
            var entity = new CurrentSpecsVA();
            entity.setLeftEye("leftEye");
            entity.setRightEye("rightEye");

            var dto = sightTestMapper.map(entity);

            assertThat(dto)
                    .usingRecursiveComparison()
                    .isEqualTo(entity);
        }

        @Test
        void testUpdateRefractedRxNullNotesAndVA() {
            var source = new RefractedRxDto();
            var target = new RefractedRx();

            //Those are sample values. Other fields are covered by "testMapRefractedRx
            source.setBvd(5.25f);
            source.setDistanceBinVisualAcuity("distanceBinVA");

            // Null notes on source should not overwrite notes on target
            source.setNotes(null);
            target.setNotes(new RxNotes("text", "optomName", LocalDate.now()));

            // Null UnaidedVisualAcuity on source should not delete RX on target
            source.setUnaidedVisualAcuity(null);
            target.setRx(new Rx());

            sightTestMapper.update(source, target);

            assertEquals(source.getBvd(), target.getRx().getBvd());
            assertEquals(source.getDistanceBinVisualAcuity(), target.getRx().getDistanceBinVA());
            assertNotNull(target.getNotes());
            assertNotNull(target.getRx());
        }

        @Test
        void testUpdateRefractedRxNewNotes() {
            var source = new RefractedRxDto();
            var target = new RefractedRx();

            //Those are sample values. Other fields are covered by "testMapRefractedRx"
            source.setBvd(5.25f);
            source.setDistanceBinVisualAcuity("distanceBinVA");

            // New notes on source should overwrite notes on target
            source.setNotes(new RxNotesDto("new text", "new optomName", LocalDate.now()));
            target.setNotes(new RxNotes("text", "optomName", LocalDate.now()));

            sightTestMapper.update(source, target);

            assertEquals(source.getBvd(), target.getRx().getBvd());
            assertEquals(source.getDistanceBinVisualAcuity(), target.getRx().getDistanceBinVA());
            assertThat(source.getNotes())
                    .usingRecursiveComparison()
                    .isEqualTo(target.getNotes());
        }
    }

    @Nested
    class PrescribedRxTests {
        @Test
        void testMapPrescribedRx() {
            var rx = Rx.builder()
                    .bvd(5f)
                    .distanceBinVA("distanceBinVA")
                    .notes("notes")
                    .unaidedBinVA("unaidedBinVA")
                    .build();

            var entity = new PrescribedRx();
            entity.setRecallPeriod(24);
            entity.setRx(rx);

            var dto = sightTestMapper.map(entity);

            assertEquals(rx.getDistanceBinVA(), dto.getDistanceBinVisualAcuity());
            assertEquals(rx.getBvd(), dto.getBvd());
            assertEquals(rx.getUnaidedBinVA(), dto.getUnaidedVisualAcuity().getBinocular());
            assertNull(dto.getRightEye());
            assertNull(dto.getLeftEye());
            assertNull(dto.getSpecificAddition());
            assertThat(dto)
                    .usingRecursiveComparison()
                    .ignoringFields("distanceBinVisualAcuity", "bvd", "unaidedVisualAcuity", "rightEye", "leftEye", "specificAddition",//Tested above
                            "id", "testDate", "dispenseNotes", "recommendations", "clinicianName", "testRoomNumber")//Not used on get SightTests
                    .isEqualTo(entity);
        }

        @Test
        void testUpdatePrescribedRxNullNotesAndVA() {
            var source = new PrescribedRxDto();
            var target = new PrescribedRx();

            //Those are sample values. Other fields are covered by "testMapPrescribedRx
            source.setBvd(5.25f);
            source.setDistanceBinVisualAcuity("distanceBinVA");

            // Null notes on source should not overwrite notes on target
            source.setNotes(null);
            target.setNotes(new RxNotes("text", "optomName", LocalDate.now()));

            // Null UnaidedVisualAcuity on source should not delete RX on target
            source.setUnaidedVisualAcuity(null);

            sightTestMapper.update(source, target);

            assertEquals(source.getBvd(), target.getRx().getBvd());
            assertEquals(source.getDistanceBinVisualAcuity(), target.getRx().getDistanceBinVA());
            assertNotNull(target.getNotes());
            assertNotNull(target.getRx());
        }

        @Test
        void testUpdatePrescribedRxNewNotes() {
            var source = new PrescribedRxDto();
            var target = new PrescribedRx();

            //Those are sample values. Other fields are covered by "testMapPrescribedRx"
            source.setBvd(5.25f);
            source.setDistanceBinVisualAcuity("distanceBinVA");

            // New notes on source should overwrite notes on target
            source.setNotes(new RxNotesDto("new text", "new optomName", LocalDate.now()));
            target.setNotes(new RxNotes("text", "optomName", LocalDate.now()));

            sightTestMapper.update(source, target);

            assertEquals(source.getBvd(), target.getRx().getBvd());
            assertEquals(source.getDistanceBinVisualAcuity(), target.getRx().getDistanceBinVA());
            assertThat(source.getNotes())
                    .usingRecursiveComparison()
                    .isEqualTo(target.getNotes());
        }
    }

    @Nested
    class ObjectiveAndIopTests {
        @Test
        void testUpdateObjectiveAndIop() {
            var source = new ObjectiveAndIopDto();
            var target = new ObjectiveAndIop();
            var eyeIop = new EyeIopDto();

            eyeIop.setIop1(1);
            eyeIop.setIop2(2);
            eyeIop.setIop3(3);
            eyeIop.setIop4(4);
            eyeIop.setAxis(5.5f);
            eyeIop.setCylinder(3.25f);
            eyeIop.setSphere("-5.25");
            eyeIop.setVisualAcuity("VA");

            source.setLeftEye(eyeIop);
            source.setRightEye(eyeIop);
            source.setNotes("notes");
            source.setTime("12:35");

            // Null drugInfo on source should not overwrite notes on target
            source.setDrugInfo(null);
            target.setDrugInfo(new DrugInfo());

            sightTestMapper.update(source, target);

            assertNotNull(target.getDrugInfo());
            assertThat(source)
                    .usingRecursiveComparison()
                    .ignoringFields("drugInfo")
                    .isEqualTo(target);
        }

        @Test
        void testMapDrugInfo() {
            var source = new DrugInfoDto();

            source.setDrugUsed("drug");
            source.setTime("13:55");
            source.setBatchNo("X123");
            source.setExpiryDate("12/2022");

            var target = sightTestMapper.map(source);

            assertThat(source)
                    .usingRecursiveComparison()
                    .isEqualTo(target);
        }
    }

    @Nested
    class EyeHealthAndOphthalmoscopy1Tests {
        @Test
        void testMapEyeHealthAndOphthalmoscopy1() {
            var target = new EyeHealthAndOphthalmoscopy1();
            var source = EyeHealthAndOphthalmoscopy1Dto.builder()
                .anteriorChamberRight("AnteriorChamberRight")
                .anteriorChamberLeft("AnteriorChamberLeft")
                .externalEyeRight("ExternalEyeRight")
                .externalEyeLeft("ExternalEyeLeft")
                .dilated(true)
                .direct(true)
                .indirect(true)
                .slitLamp(true)
                .volk(true)
                .build();

            // Null drugInfo on source should not overwrite notes on target
            source.setDrugInfoEyeHealth(null);
            target.setDrugInfoEyeHealth(new EyeHealthDrugInfo());

            sightTestMapper.update(source, target);

            assertNotNull(target.getDrugInfoEyeHealth());
            assertThat(source)
                    .usingRecursiveComparison()
                    .ignoringFields("drugInfoEyeHealth")
                    .isEqualTo(target);
        }

        @Test
        void testMapEyeHealthDrugInfo() {
            var source = EyeHealthDrugInfoDto.builder()
                .postPressureTime("12:53")
                .prePressureTime("11:12")
                .postPressure("PostPressure")
                .prePressure("PrePressure")
                .drugInfo(new DrugInfoDto())
                .build();

            var target = sightTestMapper.map(source);

            assertThat(source)
                    .usingRecursiveComparison()
                    .isEqualTo(target);
        }
    }
}
