package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.clinical.model.HabitualRxDto;
import com.specsavers.socrates.clinical.model.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.ObjectiveAndIopDto;
import com.specsavers.socrates.clinical.model.OptionRecommendationsDto;
import com.specsavers.socrates.clinical.model.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.RecommendationsDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.RxOptionTypeDto;
import com.specsavers.socrates.clinical.model.SightTestDto;
import com.specsavers.socrates.clinical.model.SightTestTypeDto;
import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.RxNotes;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.repository.HabitualRxRepository;
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import com.specsavers.socrates.common.exception.NotFoundException;
import com.specsavers.socrates.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_SIGHT_TEST_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_TR_NUMBER_ID;
import static com.specsavers.socrates.clinical.util.TestHelpers.stringOfLength;
import static org.assertj.core.api.Assertions.assertThat;
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
                .thenAnswer((i) -> i.getArgument(0));
    }

    @Nested
    class CreatesSightTestTest {
        //No need to test invalid values since GraphQL does the validation
        @Test
        void testCreateSightTestWithValidValues() {
            // Given
            final SightTestTypeDto type = SightTestTypeDto.MY_SIGHT_TEST;
            SightTest expectedSightTest = new SightTest();
            expectedSightTest.setTrNumber(VALID_TR_NUMBER_ID);
            expectedSightTest.setType(SightTestType.MY_SIGHT_TEST);
            expectedSightTest.setCreationDate(LocalDate.now());

            SightTestDto expectedSightTestDto = new SightTestDto();

            when(sightTestRepository.save(any())).then(inv -> inv.getArguments()[0]);
            when(sightTestMapper.map(any(SightTest.class))).thenReturn(expectedSightTestDto);
            when(sightTestMapper.map(any(SightTestTypeDto.class))).thenReturn(SightTestType.MY_SIGHT_TEST);
            // When
            SightTestDto actual = mutationResolver.createSightTest(VALID_TR_NUMBER_ID, type);

            // Then
            ArgumentCaptor<SightTest> sightTestCaptor = ArgumentCaptor.forClass(SightTest.class);
            verify(sightTestRepository).save(sightTestCaptor.capture());
            SightTest sightTestCaptorValue = sightTestCaptor.getValue();
            assertEquals(expectedSightTest.getTrNumber(), sightTestCaptorValue.getTrNumber());
            assertEquals(expectedSightTest.getType(), sightTestCaptorValue.getType());
            assertEquals(expectedSightTest.getCreationDate(), sightTestCaptorValue.getCreationDate());

            verify(sightTestMapper).map(same(sightTestCaptorValue));

            assertSame(expectedSightTestDto, actual);
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
            var actual = assertThrows(NotFoundException.class,
                    () -> mutationResolver.updateHistoryAndSymptoms(id, historyAndSymptoms));

            // then
            verify(sightTestRepository).findById(id);
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

            assertThrows(NotFoundException.class,
                    () -> mutationResolver.createHabitualRx(VALID_SIGHT_TEST_ID, null, null));
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
            verify(mockHabitualRxRepository).findById(id);
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
            when(sightTestRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateRefractedRx(id, null));
        }

        @Test
        void testValidInput() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var input = new RefractedRxDto();
            // Sample field, other fields are tested on mapper tests
            input.setBvd(5f);
            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateRefractedRx(id, input);

            verify(sightTestMapper).update(same(input), same(sightTest.getRefractedRx()));
            verify(sightTestRepository).save(same(sightTest));
            assertEquals(input.getBvd(), actual.getBvd());
        }
    }

    @Nested
    class UpdatePrescribedRxTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updatePrescribedRx(id, null));
        }

        @Test
        void testValidInput() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var input = new PrescribedRxDto();
            // Sample field, other fields are tested on mapper tests
            input.setBvd(5f);
            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updatePrescribedRx(id, input);

            verify(sightTestMapper).update(same(input), same(sightTest.getPrescribedRx()));
            verify(sightTestRepository).save(same(sightTest));
            assertEquals(input.getBvd(), actual.getBvd());
        }
    }

    @Nested
    class UpdateRefractedRxNoteTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateRefractedRxNote(id, null));
        }

        @ParameterizedTest(name = "invalidWhenNoteTextLength={0}")
        @ValueSource(ints = {0, 221})
        void testWithInvalidNoteText(int length) {
            var id = UUID.randomUUID();

            String text = stringOfLength(length);
            assertThrows(ValidationException.class, () -> mutationResolver.updateRefractedRxNote(id, text));
        }

        @Test
        void testNullNoteShouldDeleteNotes() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var refractedRx = new RefractedRx();

            refractedRx.setNotes(new RxNotes());
            sightTest.setRefractedRx(refractedRx);
            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateRefractedRxNote(id, null);

            assertNull(actual);
        }

        @Test
        void testValidNoteText() {
            final var RX_NOTE = "RefractedRx Note";
            var id = UUID.randomUUID();
            var sightTest = new SightTest();

            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateRefractedRxNote(id, RX_NOTE);

            assertEquals(RX_NOTE, actual.getText());
            //OptomName is hardcoded while user service is not integrated
            assertEquals("Will Smith", actual.getOptomName());
            assertEquals(LocalDate.now(), actual.getDate());
        }
    }

    @Nested
    class UpdatePrescribedRxNoteTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updatePrescribedRxNote(id, null));
        }

        @ParameterizedTest(name = "invalidWhenNoteTextLength={0}")
        @ValueSource(ints = {0, 221})
        void testWithInvalidNoteText(int length) {
            var id = UUID.randomUUID();

            String text = stringOfLength(length);
            assertThrows(ValidationException.class, () -> mutationResolver.updatePrescribedRxNote(id, text));
        }

        @Test
        void testNullNoteShouldDeleteNotes() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var prescribedRx = new PrescribedRx();

            prescribedRx.setNotes(new RxNotes());
            sightTest.setPrescribedRx(prescribedRx);
            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updatePrescribedRxNote(id, null);

            assertNull(actual);
        }

        @Test
        void testValidNoteText() {
            var RX_NOTE = "PrescribedRx Note";
            var id = UUID.randomUUID();
            var sightTest = new SightTest();

            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updatePrescribedRxNote(id, RX_NOTE);

            assertEquals(RX_NOTE, actual.getText());
            //OptomName is hardcoded while user service is not integrated
            assertEquals("Will Smith", actual.getOptomName());
            assertEquals(LocalDate.now(), actual.getDate());
        }
    }

    @Nested
    class UpdateObjectiveAndIopTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateObjectiveAndIop(id, null));
        }

        @Test
        void testWithInvalidIdDrugInfo() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateObjectiveAndIopDrugInfo(id, null));
        }

        @Test
        void testValidInput() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var input = new ObjectiveAndIopDto();
            // Sample field, other fields are tested on mapper tests
            input.setTime("15:20");
            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateObjectiveAndIop(id, input);

            verify(sightTestMapper).update(same(input), same(sightTest.getObjectiveAndIop()));
            verify(sightTestRepository).save(same(sightTest));
            assertEquals(input.getTime(), actual.getTime());
        }


        @Test
        void testValidDrugInfo() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var input = new DrugInfoDto();

            // Sample field, other fields are tested on mapper tests
            input.setDrugUsed("XYZ");
            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateObjectiveAndIopDrugInfo(id, input);

            verify(sightTestMapper).map(same(input));
            verify(sightTestRepository).save(same(sightTest));
            assertEquals(input, actual);
        }
    }

    @Nested
    class UpdateOptionRecommendationsTest {
        @Test
        void testWithInvalidId() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateOptionRecommendations(id, null));
        }

        @Test
        void testWithInvalidIdDispenseNote() {
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateDispenseNote(id, null));
        }

        @Test
        void testValidInput() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var rec = new RecommendationsDto();
            rec.setPolar(true);

            var input = new OptionRecommendationsDto();
            input.setRxOptionType(RxOptionTypeDto.NEW_RX);
            input.setReferToDoctor(true);
            input.setRecommendations(rec);

            when(sightTestRepository.findById(id)).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateOptionRecommendations(id, input);

            verify(sightTestRepository).save(same(sightTest));
            assertThat(actual).isEqualTo(input);
        }


        @Test
        void testValidDispenseNote() {
            var id = UUID.randomUUID();
            var sightTest = new SightTest();
            var input = "Some Notes";

            when(sightTestRepository.findById(eq(id))).thenReturn(Optional.of(sightTest));

            var actual = mutationResolver.updateDispenseNote(id, input);

            verify(sightTestRepository).save(same(sightTest));
            assertEquals(input, actual);
        }
    }
}
