package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import com.specsavers.socrates.clinical.model.entity.Rx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.PrescribedEyeRxDto;
import com.specsavers.socrates.clinical.model.type.PrismDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class HabitualRxDtoMapperTest {

    private HabitualRxMapper sut;

    @BeforeEach
    void beforeEach() {
        sut = HabitualRxMapper.INSTANCE;
    }

    @Nested
    class ToEntityTest {

        @Test
        void mapsToEntityFromInput() {
            // given
            var input = new HabitualRxDto();
            input.setAge(4f);
            input.setClinicianName("name");
            var sightTestId = UUID.randomUUID();

            // when
            var actual = sut.toEntity(sightTestId, 3, input);

            // then
            assertNull(actual.getId());
            assertEquals(sightTestId, actual.getSightTestId());
            assertEquals(4f, actual.getAge());
            assertEquals("name", actual.getClinicianName());
            assertEquals(3, actual.getPairNumber());
        }

        @Test
        void mapsToEntityRxFromInput() {
            // given
            var input = new HabitualRxDto();
            input.setNotes("some notes");

            // when
            var actual = sut.toEntity(UUID.randomUUID(), 1, input).getRx();

            // then
            assertNull(actual.getId());
            assertEquals("some notes", actual.getNotes());
        }

        @Test
        void mapsEyeEntitiesFromInput() {
            // given
            var input = new HabitualRxDto();
            var eye = new PrescribedEyeRxDto();
            eye.setSphere("sphere");
            eye.setCylinder("cylinder");
            eye.setAxis(1.23f);
            eye.setVisualAcuity("va");
            eye.setPupillaryDistance(4.56f);
            eye.setAddition(5.67f);
            var prism = new PrismDto();
            prism.setHorizontal("horizontal");
            prism.setVertical("vertical");
            eye.setPrism(prism);
            input.setLeftEye(eye);
            input.setRightEye(new PrescribedEyeRxDto());

            // when
            var actual = sut.toEntity(UUID.randomUUID(), 2, input).getRx().getLeftEye();

            // then
            assertNull(actual.getId());
            assertEquals("sphere", actual.getSphere());
            assertEquals("cylinder", actual.getCylinder());
            assertEquals(1.23f, actual.getAxis());
            assertEquals("va", actual.getVisualAcuity());
            assertEquals(4.56f, actual.getPupillaryDistance());
            assertEquals(5.67f, actual.getAddition());
            assertEquals("horizontal", actual.getPrismHorizontal());
            assertEquals("vertical", actual.getPrismVertical());
        }
    }

    @Nested
    class FromEntityTest {

        @Test
        void mapsHabitualRx() {
            // given
            HabitualRx entity = new HabitualRx();
            UUID id = UUID.randomUUID();
            entity.setId(id);
            UUID sightTest = UUID.randomUUID();
            entity.setSightTestId(sightTest);
            entity.setPairNumber(2);
            entity.setAge(2.5f);
            entity.setClinicianName("name");
            Rx rx = new Rx();
            rx.setNotes("notes!");
            entity.setRx(rx);

            // when
            var actual = sut.fromEntity(entity);

            // then
            assertEquals(id, actual.getId());
            assertEquals(sightTest, actual.getSightTestId());
            assertEquals(2, actual.getPairNumber());
            assertEquals(2.5f, actual.getAge());
            assertEquals("name", actual.getClinicianName());
            assertEquals("notes!", actual.getNotes());
        }

        @Test
        void mapsEye() {
            // given
            HabitualRx entity = new HabitualRx();
            Rx rx = new Rx();
            RxEye eye = new RxEye();
            eye.setSphere("sphere");
            eye.setCylinder("cylinder");
            eye.setAxis(1.23f);
            eye.setVisualAcuity("va");
            eye.setPupillaryDistance(4.56f);
            eye.setAddition(6.78f);
            eye.setPrismHorizontal("horizontal");
            eye.setPrismVertical("vertical");
            rx.setLeftEye(eye);
            entity.setRx(rx);

            // when
            var actual = sut.fromEntity(entity).getLeftEye();

            // then
            assertEquals("sphere", actual.getSphere());
            assertEquals("cylinder", actual.getCylinder());
            assertEquals(1.23f, actual.getAxis());
            assertNull(actual.getDistanceVisualAcuity());
            assertNull(actual.getNearVisualAcuity());
            assertEquals("va", actual.getVisualAcuity());
            assertEquals(4.56f, actual.getPupillaryDistance());
            assertNull(actual.getNearAddition());
            assertNull(actual.getInterAddtion());
            assertEquals(6.78f, actual.getAddition());
            assertNull(actual.getDistancePrism());
            assertNull(actual.getNearPrism());
            assertEquals("horizontal", actual.getPrism().getHorizontal());
            assertEquals("vertical", actual.getPrism().getVertical());
        }
    }

    @Nested
    class UpdateEntityTest {
        @Test
        void updatesExistingEntityWhenUpdate() {
            // given
            var entity = new HabitualRx();
            var id = UUID.randomUUID();
            entity.setId(id);
            entity.setPairNumber(1);
            entity.setSightTestId(UUID.randomUUID());
            var rx = new Rx();
            rx.setId(UUID.randomUUID());
            var eye = new RxEye();
            eye.setId(UUID.randomUUID());
            rx.setLeftEye(eye);
            entity.setRx(rx);
            var input = new HabitualRxDto();
            input.setAge(3f);
            input.setClinicianName("bob");
            input.setLeftEye(new PrescribedEyeRxDto());

            // when
            sut.updateEntity(input, entity);

            // then
            assertEquals(id, entity.getId());
            assertEquals(3f, entity.getAge());
            assertEquals("bob", entity.getClinicianName());
            assertNotNull(entity.getRx().getId());
            assertNotNull(entity.getRx().getLeftEye().getId());
            assertNotNull(entity.getPairNumber());
            assertNotNull(entity.getSightTestId());
        }
    }
}
