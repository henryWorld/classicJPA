package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.SightTestDto;
import com.specsavers.socrates.clinical.repository.HabitualRxRepository;
import com.specsavers.socrates.clinical.repository.SightTestRepository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_SIGHT_TEST_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_TR_NUMBER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
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

    @Mock
    private SightTestMapper sightTestMapper;

    @InjectMocks
    private MutationResolver mutationResolver;

    @Nested
    class CreatesSightTestTest {
        //No need to test invalid values since GraphQL does the validation
        @Test
        void testCreateSightTestWithValidValues(){
            var type = SightTestType.MY_SIGHT_TEST;
            var sightTestDto = new SightTestDto();
            var sightTest = new SightTest();

            sightTest.setTrNumber(VALID_TR_NUMBER_ID);
            sightTest.setType(type);
            sightTest.setCreationDate(LocalDate.now());
            when(sightTestMapper.map(sightTest)).thenReturn(sightTestDto);

            var result = mutationResolver.createSightTest(VALID_TR_NUMBER_ID, type);

            assertEquals(sightTestDto, result);
            verify(sightTestRepository).save(sightTest);
            verify(sightTestMapper).map(sightTest);
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
}
