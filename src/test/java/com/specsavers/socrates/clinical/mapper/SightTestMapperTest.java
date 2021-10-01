package com.specsavers.socrates.clinical.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_CUSTOMER_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_TR_NUMBER_ID;

import java.time.LocalDate;
import java.util.UUID;

import com.specsavers.socrates.clinical.model.entity.CurrentSpecsVA;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.Rx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.model.entity.SpecificAddition;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class SightTestMapperTest {
    private SightTestMapper sightTestMapper = Mappers.getMapper(SightTestMapper.class);

    @Test
    void testMapSightTest(){
        var entity = new SightTest();
        entity.setCreationDate(LocalDate.now());
        entity.setCustomerId(VALID_CUSTOMER_ID);
        entity.setId(UUID.randomUUID());
        entity.setTrNumber(VALID_TR_NUMBER_ID);
        entity.setType(SightTestType.MY_SIGHT_TEST);
        
        var dto = sightTestMapper.map(entity);

        assertThat(dto)
            .usingRecursiveComparison()
            .isEqualTo(entity);
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
