package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.entity.CurrentSpecsVA;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.Rx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.model.entity.SpecificAddition;
import com.specsavers.socrates.clinical.model.type.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.type.LifestyleDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_TR_NUMBER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SightTestMapperTest {
    private SightTestMapper sightTestMapper = Mappers.getMapper(SightTestMapper.class);

    @Test
    void testMapSightTest() {
        var entity = new SightTest();
        var date = LocalDate.now();
        var id = UUID.randomUUID();
        entity.setCreationDate(date);
        entity.setId(id);
        entity.setTrNumber(VALID_TR_NUMBER_ID);
        entity.setType(SightTestType.MY_SIGHT_TEST);
        entity.setFamilyHistory("family");
        entity.setOccupation("occupation");

        var actual = sightTestMapper.map(entity);

        assertEquals(date, actual.getCreationDate());
        assertEquals(id, actual.getId());
        assertEquals(VALID_TR_NUMBER_ID, actual.getTrNumber());
        assertEquals(SightTestType.MY_SIGHT_TEST, actual.getType());
        assertEquals("family", actual.getHistoryAndSymptoms().getFamilyHistory());
        assertEquals("occupation", actual.getHistoryAndSymptoms().getLifestyle().getOccupation());
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
    void testMapRefractedRx(){
        var rx =  Rx.builder()
            .bvd(5f)
            .distanceBinVA("distanceBinVA")
            .notes("notes")
            .unaidedBinVA("unaidedBinVA")
            .build();

        var entity = new RefractedRx();
        entity.setId(UUID.randomUUID());
        entity.setRx(rx);
        
        var dto = sightTestMapper.map(entity);

        assertEquals(rx.getDistanceBinVA(), dto.getDistanceBinVisualAcuity());
        assertEquals(rx.getBvd(), dto.getBvd());
        assertEquals(rx.getUnaidedBinVA(), dto.getUnaidedVisualAcuity().getBinocular());
        assertEquals(rx.getRightEye(), dto.getRightEye());
        assertEquals(rx.getLeftEye(), dto.getLeftEye());
        assertThat(dto)
            .usingRecursiveComparison()
            .ignoringFields("distanceBinVisualAcuity","bvd","unaidedVisualAcuity", "rightEye","leftEye")
            .isEqualTo(entity);
    }

    @Test
    void testMapSpecificAddition(){
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
    void testMapCurrentSpecsVA(){
        var entity = new CurrentSpecsVA();
        entity.setLeftEye("leftEye");
        entity.setRightEye("rightEye");
        
        var dto = sightTestMapper.map(entity);

        assertThat(dto)
            .usingRecursiveComparison()
            .isEqualTo(entity);
    }

    @Test
    void testMapPrescribedRx(){
        var rx =  Rx.builder()
            .bvd(5f)
            .distanceBinVA("distanceBinVA")
            .notes("notes")
            .unaidedBinVA("unaidedBinVA")
            .build();

        var entity = new PrescribedRx();
        entity.setId(UUID.randomUUID());
        entity.setRecallPeriod(24);
        entity.setRx(rx);
        
        var dto = sightTestMapper.map(entity);

        assertEquals(rx.getDistanceBinVA(), dto.getDistanceBinVisualAcuity());
        assertEquals(rx.getBvd(), dto.getBvd());
        assertEquals(rx.getUnaidedBinVA(), dto.getUnaidedVisualAcuity().getBinocular());
        assertEquals(rx.getRightEye(), dto.getRightEye());
        assertEquals(rx.getLeftEye(), dto.getLeftEye());
        assertThat(dto)
            .usingRecursiveComparison()
            .ignoringFields("distanceBinVisualAcuity","bvd","unaidedVisualAcuity", "rightEye","leftEye",//Tested above
            "testDate", "dispenseNotes", "recommendations", "clinicianName", "testRoomNumber")//Not used on get SightTests
            .isEqualTo(entity);
    }

    @Test
    void testMapRxEye(){
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
}