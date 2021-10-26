package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.RxNotes;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.type.RefractedRxDto;
import com.specsavers.socrates.clinical.model.type.SightTestDto;
import com.specsavers.socrates.clinical.repository.HabitualRxRepository;
import com.specsavers.socrates.clinical.repository.SightTestRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_SIGHT_TEST_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_TR_NUMBER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MutationResolverTest {
    @Mock
    private SightTestRepository sightTestRepository;

    @Mock
    private HabitualRxRepository mockHabitualRxRepository;

    @Mock
    private HabitualRxMapper mockMapper;

    @Spy
    private SightTestMapper sightTestMapper = Mappers.getMapper(SightTestMapper.class);

    @InjectMocks
    private MutationResolver mutationResolver;

    @BeforeEach
    public void setup() {
        // Make sure the repos return the same item on save, to avoid null pointers
        // when referenced in the class
        lenient().when(sightTestRepository.save(any()))
            .thenAnswer((i)-> i.getArgument(0));
    }
    @Nested
    class CreatesSightTestTest {
        //No need to test invalid values since GraphQL does the validation
        @Test
        void testCreateSightTestWithValidValues(){
            var type = SightTestType.MY_SIGHT_TEST;
            var sightTest = new SightTest();

            sightTest.setTrNumber(VALID_TR_NUMBER_ID);
            sightTest.setType(type);
            sightTest.setCreationDate(LocalDate.now());

            var result = mutationResolver.createSightTest(VALID_TR_NUMBER_ID, type);

            assertEquals(type, result.getType());
            assertEquals(VALID_TR_NUMBER_ID, result.getTrNumber());
            verify(sightTestRepository).save(sightTest);
            verify(sightTestMapper).map(sightTest);
        }
    }

    @Nested
    class UpdateHistoryAndSymptoms {
        private HistoryAndSymptomsDto historyAndSymptoms;
        private SightTest sightTest;

        @BeforeEach
        void beforeEach() {
            historyAndSymptoms = new HistoryAndSymptomsDto();
            sightTest = new SightTest();
        }

        @Test
        void throwsNotFoundWhenNoSightTest() {
            // given
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            // when
            var actual = assertThrows(NotFoundException.class, () -> mutationResolver.updateHistoryAndSymptoms(id, historyAndSymptoms));

            // then
            verify(sightTestRepository).findById(eq(id));
            assertNotNull(actual);
        }

        @Test
        void updatesHistoryAndSymptomsWhenSightTestExists() {
            // given
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(any())).thenReturn(Optional.of(sightTest));
            when(sightTestRepository.save(any())).thenReturn(sightTest);
            var dto = new SightTestDto();
            dto.setHistoryAndSymptoms(new HistoryAndSymptomsDto());
            when(sightTestMapper.map(any(SightTest.class))).thenReturn(dto);

            // when
            var actual = mutationResolver.updateHistoryAndSymptoms(id, historyAndSymptoms);

            // then
            verify(sightTestMapper).update(same(sightTest), same(historyAndSymptoms));
            verify(sightTestRepository).save(same(sightTest));
            assertSame(dto.getHistoryAndSymptoms(), actual);
        }
    }

    @Nested
    class CreateHabitualRxTest {
        @Test
        void testWithInvalidSightTest() {
            when(sightTestRepository.existsById(any())).thenReturn(false);

            assertThrows(NotFoundException.class, () -> mutationResolver.createHabitualRx(VALID_SIGHT_TEST_ID, null, null));
        }

        @Test
        void savesEntityWhenValid() {
            var inputDto = new HabitualRxDto();
            var entity = new HabitualRx();
            var savedEntity = new HabitualRx();
            var dto = new HabitualRxDto();

            when(mockMapper.toEntity(any(), anyInt(), any())).thenReturn(entity);
            when(mockHabitualRxRepository.save(any())).thenReturn(savedEntity);
            when(mockMapper.fromEntity(any())).thenReturn(dto);
            when(sightTestRepository.existsById(any())).thenReturn(true);

            var actual = mutationResolver.createHabitualRx(VALID_SIGHT_TEST_ID, 1, inputDto);

            verify(mockMapper).toEntity(eq(VALID_SIGHT_TEST_ID), eq(1), same(inputDto));
            verify(mockHabitualRxRepository).save(same(entity));
            verify(mockMapper).fromEntity(same(savedEntity));
            assertSame(dto, actual);
        }
    }

    @Nested
    class UpdateHabitualRxTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(mockHabitualRxRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateHabitualRx(id, null));
            verify(mockHabitualRxRepository).findById(eq(id));
        }

        @Test
        void testUpdatesWhenValid() {
            var entity = new HabitualRx();
            var id = UUID.randomUUID();
            when(mockHabitualRxRepository.findById(any())).thenReturn(Optional.of(entity));
            var input = new HabitualRxDto();
            var dto = new HabitualRxDto();
            when(mockMapper.fromEntity(any())).thenReturn(dto);

            var actual = mutationResolver.updateHabitualRx(id, input);

            verify(mockMapper).updateEntity(same(input), same(entity));
            verify(mockHabitualRxRepository).save(same(entity));
            verify(mockMapper).fromEntity(same(entity));
            assertEquals(dto, actual);
        }
    }

    @Nested
    class UpdateRefractedRxTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateRefractedRx(id, null));
        }

        @Test
        void testValidInput() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var input = new RefractedRxDto();
            // Sample field, other fields are tested on mapper tests
            input.setBvd(5f);
            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateRefractedRx(id, input);

            verify(sightTestMapper).update(same(input), same(sightTest.getRefractedRx()));
            verify(sightTestRepository).save(same(sightTest));
            assertEquals(input.getBvd(), actual.getBvd());
        }
    }

    @Nested
    class UpdateRefractedRxNoteTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateRefractedRxNote(id, null));
        }

        @Test
        void testNullNoteShouldDeleteNotes() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var refractedRx = new RefractedRx();

            refractedRx.setNotes(new RxNotes());
            sightTest.setRefractedRx(refractedRx);
            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.of(sightTest));
            
            var actual = mutationResolver.updateRefractedRxNote(id, null);

            assertNull(actual);
        }

        @Test
        void testValidNoteText() {
            var RX_NOTE = "RefractedRx Note";
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
   
            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.of(sightTest));
            
            var actual = mutationResolver.updateRefractedRxNote(id, RX_NOTE);

            assertEquals(RX_NOTE, actual.getText());
            //OptomName is hardcoded while user service is not integrated
            assertEquals("Will Smith", actual.getOptomName());
            assertEquals(LocalDate.now(), actual.getDate());
        }
    }
}
