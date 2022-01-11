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
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import com.specsavers.socrates.common.exception.NotFoundException;
import com.specsavers.socrates.common.exception.OutdatedEntityException;
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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_TR_NUMBER_ID;
import static java.util.Collections.singleton;
import static com.specsavers.socrates.clinical.util.TestHelpers.stringOfLength;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MutationResolverTest {
    private static final UUID SIGHT_TEST_ID = UUID.randomUUID();
    private static final long VALID_VERSION = 1L;
    private static final long INVALID_VERSION = 2L;

    @Mock
    private SightTestRepository sightTestRepository;

    @Mock
    private HabitualRxMapper mockMapper;

    @Spy
    private Clock fixedClock = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    @Spy
    private SightTestMapper sightTestMapper = Mappers.getMapper(SightTestMapper.class);

    @InjectMocks
    private MutationResolver mutationResolver;

    private SightTest sightTest;

    @BeforeEach
    public void setup() {
        // Make sure the repos return the same item on save, to avoid null pointers
        // when referenced in the class
        lenient().when(sightTestRepository.saveAndFlush(any())).thenAnswer(returnsFirstArg());
        sightTest = new SightTest();
        sightTest.setVersion(1L);
        lenient().when(sightTestRepository.findById(any())).thenReturn(Optional.of(sightTest));
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

            when(sightTestMapper.map(any(SightTest.class))).thenReturn(expectedSightTestDto);
            when(sightTestMapper.map(any(SightTestTypeDto.class))).thenReturn(SightTestType.MY_SIGHT_TEST);
            // When
            SightTestDto actual = mutationResolver.createSightTest(VALID_TR_NUMBER_ID, type);

            // Then
            ArgumentCaptor<SightTest> sightTestCaptor = ArgumentCaptor.forClass(SightTest.class);
            verify(sightTestRepository).saveAndFlush(sightTestCaptor.capture());
            SightTest captured = sightTestCaptor.getValue();
            assertThat(captured.getTrNumber()).isEqualTo(expectedSightTest.getTrNumber());
            assertThat(captured.getType()).isEqualTo(expectedSightTest.getType());
            assertThat(captured.getCreationDate()).isEqualTo(expectedSightTest.getCreationDate());
            assertThat(captured.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            verify(sightTestMapper).map(same(captured));

            assertSame(expectedSightTestDto, actual);
        }
    }

    @Nested
    class UpdateHistoryAndSymptoms {
        private HistoryAndSymptomsDto historyAndSymptoms;

        @BeforeEach
        void beforeEach() {
            historyAndSymptoms = new HistoryAndSymptomsDto();
        }

        @Test
        void throwsNotFoundWhenNoSightTest() {
            // given
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            // when
            var actual = assertThrows(NotFoundException.class,
                    () -> mutationResolver.updateHistoryAndSymptoms(SIGHT_TEST_ID, VALID_VERSION, historyAndSymptoms));

            // then
            verify(sightTestRepository).findById(SIGHT_TEST_ID);
            assertNotNull(actual);
        }

        @Test
        void throwsWhenIncorrectVersion() {
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateHistoryAndSymptoms(SIGHT_TEST_ID, INVALID_VERSION, historyAndSymptoms));
        }

        @Test
        void updatesHistoryAndSymptomsWhenSightTestExists() {
            // given
            var dto = new SightTestDto();
            dto.setHistoryAndSymptoms(new HistoryAndSymptomsDto());
            when(sightTestMapper.map(any(SightTest.class))).thenReturn(dto);

            // when
            var actual = mutationResolver.updateHistoryAndSymptoms(SIGHT_TEST_ID, VALID_VERSION, historyAndSymptoms);

            // then
            verify(sightTestMapper).update(same(sightTest), same(historyAndSymptoms));
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            assertThat(actual).isSameAs(dto);
        }
    }

    @Nested
    class UpdateHabitualRxTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateHabitualRx(SIGHT_TEST_ID, VALID_VERSION, 1, null));
            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @Test
        void testUpdatesExistingPair() {
            // given
            var pairNumber = 2;
            var dto = new HabitualRxDto();
            var rx = new HabitualRx();
            rx.setPairNumber(pairNumber);
            sightTest.setHabitualRx(new ArrayList<>(singleton(rx)));

            // when
            var actual = mutationResolver.updateHabitualRx(SIGHT_TEST_ID, VALID_VERSION, pairNumber, dto);

            // then
            verify(mockMapper).updateEntity(same(dto), same(rx));
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getHabitualRx()).hasSize(1);
            assertThat(actual.getHabitualRx())
                    .hasSize(1)
                    .first()
                    .satisfies(pair -> assertThat(pair.getPairNumber()).isEqualTo(pairNumber));
        }

        @Test
        void testAddsNewPair() {
            // given
            var pairNumber = 2;
            var dto = new HabitualRxDto();
            var rx = new HabitualRx();
            rx.setPairNumber(pairNumber - 1);
            sightTest.setHabitualRx(new ArrayList<>(singleton(rx)));

            // when
            mutationResolver.updateHabitualRx(SIGHT_TEST_ID, VALID_VERSION, pairNumber, dto);

            // then
            var captor = ArgumentCaptor.forClass(HabitualRx.class);
            verify(mockMapper).updateEntity(same(dto), captor.capture());
            var captured = captor.getValue();
            assertThat(captured.getSightTest()).isSameAs(sightTest);
            assertThat(captured.getPairNumber()).isEqualTo(pairNumber);
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getHabitualRx()).hasSize(2);
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var pairNumber = 1;
            var dto = new HabitualRxDto();
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateHabitualRx(SIGHT_TEST_ID, INVALID_VERSION, pairNumber, dto));
        }
    }

    @Nested
    class UpdateRefractedRxTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateRefractedRx(SIGHT_TEST_ID, VALID_VERSION, null));

            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @Test
        void testValidInput() {
            var input = new RefractedRxDto();
            // Sample field, other fields are tested on mapper tests
            input.setBvd(5f);

            var actual = mutationResolver.updateRefractedRx(SIGHT_TEST_ID, VALID_VERSION, input);

            verify(sightTestMapper).update(same(input), same(sightTest.getRefractedRx()));
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            assertEquals(input.getBvd(), actual.getRefractedRx().getBvd());
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var input = new RefractedRxDto();
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateRefractedRx(SIGHT_TEST_ID, INVALID_VERSION, input));
        }
    }

    @Nested
    class UpdatePrescribedRxTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updatePrescribedRx(SIGHT_TEST_ID, VALID_VERSION, null));
        }

        @Test
        void testValidInput() {
            var input = new PrescribedRxDto();
            // Sample field, other fields are tested on mapper tests
            input.setBvd(5f);

            var response = mutationResolver.updatePrescribedRx(SIGHT_TEST_ID, VALID_VERSION, input);

            var actual = response.getPrescribedRx();
            verify(sightTestRepository).findById(SIGHT_TEST_ID);
            verify(sightTestMapper).update(same(input), same(sightTest.getPrescribedRx()));
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            assertThat(actual.getBvd()).isEqualTo(input.getBvd());
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var input = new PrescribedRxDto();
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updatePrescribedRx(SIGHT_TEST_ID, INVALID_VERSION, input));
        }
    }

    @Nested
    class UpdateRefractedRxNoteTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateRefractedRxNote(SIGHT_TEST_ID, VALID_VERSION, null));

            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @ParameterizedTest(name = "invalidWhenNoteTextLength={0}")
        @ValueSource(ints = {0, 221})
        void testWithInvalidNoteText(int length) {
            String text = stringOfLength(length);
            assertThrows(ValidationException.class, () -> mutationResolver.updateRefractedRxNote(SIGHT_TEST_ID, VALID_VERSION, text));
        }

        @Test
        void testNullNoteShouldDeleteNotes() {
            var refractedRx = new RefractedRx();

            refractedRx.setNotes(new RxNotes());
            sightTest.setRefractedRx(refractedRx);

            var actual = mutationResolver.updateRefractedRxNote(SIGHT_TEST_ID, VALID_VERSION, null);

            assertThat(actual.getRefractedRx().getNotes()).isNull();
        }

        @Test
        void testValidNoteText() {
            var text = "RefractedRx Note";

            var response = mutationResolver.updateRefractedRxNote(SIGHT_TEST_ID, VALID_VERSION, text);

            var actual = response.getRefractedRx().getNotes();
            assertEquals(text, actual.getText());
            //OptomName is hardcoded while user service is not integrated
            assertEquals("Will Smith", actual.getOptomName());
            assertEquals(LocalDate.now(), actual.getDate());
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var text = "text";
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateRefractedRxNote(SIGHT_TEST_ID, INVALID_VERSION, text));
        }
    }

    @Nested
    class UpdatePrescribedRxNoteTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updatePrescribedRxNote(SIGHT_TEST_ID, VALID_VERSION, null));

            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @ParameterizedTest(name = "invalidWhenNoteTextLength={0}")
        @ValueSource(ints = {0, 221})
        void testWithInvalidNoteText(int length) {
            String text = stringOfLength(length);
            assertThrows(ValidationException.class, () -> mutationResolver.updatePrescribedRxNote(SIGHT_TEST_ID, VALID_VERSION, text));
        }

        @Test
        void testNullNoteShouldDeleteNotes() {
            var prescribedRx = new PrescribedRx();
            prescribedRx.setNotes(new RxNotes());
            sightTest.setPrescribedRx(prescribedRx);

            var response = mutationResolver.updatePrescribedRxNote(SIGHT_TEST_ID, VALID_VERSION, null);

            var actual = response.getPrescribedRx().getNotes();
            assertThat(actual).isNull();
        }

        @Test
        void testValidNoteText() {
            var text = "PrescribedRx Note";

            var response = mutationResolver.updatePrescribedRxNote(SIGHT_TEST_ID, VALID_VERSION, text);

            var actual = response.getPrescribedRx().getNotes();
            assertEquals(text, actual.getText());
            //OptomName is hardcoded while user service is not integrated
            assertEquals("Will Smith", actual.getOptomName());
            assertEquals(LocalDate.now(), actual.getDate());
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var text = "text";
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updatePrescribedRxNote(SIGHT_TEST_ID, INVALID_VERSION, text));
        }
    }

    @Nested
    class UpdateObjectiveAndIopDrugInfoTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateObjectiveAndIopDrugInfo(SIGHT_TEST_ID, VALID_VERSION, null));

            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @Test
        void testValidDrugInfo() {
            var input = new DrugInfoDto();
            // Sample field, other fields are tested on mapper tests
            input.setDrugUsed("XYZ");

            var response = mutationResolver.updateObjectiveAndIopDrugInfo(SIGHT_TEST_ID, VALID_VERSION, input);

            var actual = response.getObjectiveAndIop().getDrugInfo();
            verify(sightTestMapper).map(same(input));
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            assertEquals(input, actual);
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var input = new DrugInfoDto();
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateObjectiveAndIopDrugInfo(SIGHT_TEST_ID, INVALID_VERSION, input));
        }
    }

    @Nested
    class UpdateOptionRecommendationsTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateOptionRecommendations(SIGHT_TEST_ID, VALID_VERSION, null));
        }

        @Test
        void testValidInput() {
            var rec = new RecommendationsDto();
            rec.setPolar(true);

            var input = new OptionRecommendationsDto();
            input.setRxOptionType(RxOptionTypeDto.NEW_RX);
            input.setReferToDoctor(true);
            input.setRecommendations(rec);

            var response = mutationResolver.updateOptionRecommendations(SIGHT_TEST_ID, VALID_VERSION, input);

            var actual = response.getOptionRecommendations();
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(actual).isEqualTo(input);
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var input = new OptionRecommendationsDto();
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateOptionRecommendations(SIGHT_TEST_ID, INVALID_VERSION, input));
        }
    }

    @Nested
    class UpdateObjectiveAndIopTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateObjectiveAndIop(SIGHT_TEST_ID, VALID_VERSION, null));

            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @Test
        void testValidInput() {
            var input = new ObjectiveAndIopDto();
            // Sample field, other fields are tested on mapper tests
            input.setTime("15:20");

            var response = mutationResolver.updateObjectiveAndIop(SIGHT_TEST_ID, VALID_VERSION, input);

            var actual = response.getObjectiveAndIop();
            verify(sightTestMapper).update(same(input), same(sightTest.getObjectiveAndIop()));
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            assertEquals(input.getTime(), actual.getTime());
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var input = new ObjectiveAndIopDto();
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateObjectiveAndIop(SIGHT_TEST_ID, INVALID_VERSION, input));
        }
    }

    @Nested
    class DispenseNoteTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateOptionRecommendations(SIGHT_TEST_ID, VALID_VERSION, null));

            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @Test
        void testValidInput() {
            var rec = new RecommendationsDto();
            rec.setPolar(true);
            var input = new OptionRecommendationsDto();
            input.setRxOptionType(RxOptionTypeDto.NEW_RX);
            input.setReferToDoctor(true);
            input.setRecommendations(rec);

            var response = mutationResolver.updateOptionRecommendations(SIGHT_TEST_ID, VALID_VERSION, input);

            var actual = response.getOptionRecommendations();
            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            assertThat(actual).isEqualTo(input);
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var input = new OptionRecommendationsDto();
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateOptionRecommendations(SIGHT_TEST_ID, INVALID_VERSION, input));
        }
    }

    @Nested
    class UpdateDispenseNoteTest {
        @Test
        void testWithInvalidId() {
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> mutationResolver.updateDispenseNote(SIGHT_TEST_ID, VALID_VERSION, null));

            verify(sightTestRepository).findById(SIGHT_TEST_ID);
        }

        @Test
        void testValidInput() {
            var input = "Some Notes";

            var actual = mutationResolver.updateDispenseNote(SIGHT_TEST_ID, VALID_VERSION, input);

            verify(sightTestRepository).saveAndFlush(same(sightTest));
            assertThat(sightTest.getUpdated()).isEqualTo(OffsetDateTime.now(fixedClock));
            assertThat(actual.getDispenseNotes()).isEqualTo(input);
        }

        @Test
        void throwsWhenIncorrectVersion() {
            var input = "input";
            assertThrows(OutdatedEntityException.class,
                    () -> mutationResolver.updateDispenseNote(SIGHT_TEST_ID, INVALID_VERSION, input));
        }
    }
}
