package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.legacy.model.PrescribedRX;
import com.specsavers.socrates.clinical.legacy.model.rx.RX;
import com.specsavers.socrates.clinical.legacy.repository.PrescribedRxRepository;
import com.specsavers.socrates.clinical.mapper.PrescribedRxMapper;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.SightTestDto;
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import com.specsavers.socrates.common.exception.NotFoundException;
import com.specsavers.socrates.common.exception.UnexpectedSocratesException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.NOT_FOUND_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_CUSTOMER_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_PRESCRIBED_RX_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_TR_NUMBER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryResolverTest {

    @Mock
    private PrescribedRxRepository prescribedRxRepository;

    @Mock
    private SightTestRepository sightTestRepository;

    @Spy
    private SightTestMapper sightTestMapper = Mappers.getMapper(SightTestMapper.class);

    @Mock
    private PrescribedRxMapper mockMapper;

    @InjectMocks
    private QueryResolver queryResolver;

    @Nested
    class GetLegacyPrescribedRx {
        @Test
        void testPrescribedRXWithZeroArgs() {

            var error = assertThrows(
                    UnexpectedSocratesException.class,
                    () -> queryResolver.prescribedRX(0, 0)
            );

            assertEquals("Provide a valid id OR testRoomNumber", error.getMessage());
        }

        @Test
        void testPrescribedRXWithBothValidArgs() {

            var error = assertThrows(
                    UnexpectedSocratesException.class,
                    () -> queryResolver.prescribedRX(VALID_PRESCRIBED_RX_ID, VALID_TR_NUMBER_ID)
            );

            assertEquals("Cannot search by id and testRoomNumber at same time", error.getMessage());
        }

        @Test
        void testPrescribedRXWithBothNotFoundData() {

            assertThrows(
                    NotFoundException.class,
                    () -> queryResolver.prescribedRX(NOT_FOUND_ID, 0)
            );

            assertThrows(
                    NotFoundException.class,
                    () -> queryResolver.prescribedRX(0, NOT_FOUND_ID)
            );
        }

        @Test
        void testPrescribedRXWithValidTrNumber() {

            var rx = new RX();
            var prescribedRX = new PrescribedRX();
            prescribedRX.setRx(rx);
            var dto = mock(PrescribedRxDto.class);

            when(prescribedRxRepository.findByTestRoomNumber(VALID_TR_NUMBER_ID))
                    .thenReturn(Optional.of(prescribedRX));
            when(mockMapper.fromEntity(any())).thenReturn(dto);

            var result = queryResolver.prescribedRX(0, VALID_TR_NUMBER_ID);

            verify(prescribedRxRepository).findByTestRoomNumber(VALID_TR_NUMBER_ID);
            verify(mockMapper).fromEntity(same(prescribedRX));
            assertEquals(dto, result);
        }

        @Test
        void testPrescribedRXWithValidPrescribedRxId() {

            var rx = new RX();
            var prescribedRX = new PrescribedRX();
            prescribedRX.setRx(rx);
            var dto = mock(PrescribedRxDto.class);

            when(prescribedRxRepository.findById(VALID_PRESCRIBED_RX_ID))
                    .thenReturn(Optional.of(prescribedRX));
            when(mockMapper.fromEntity(any())).thenReturn(dto);

            var result = queryResolver.prescribedRX(VALID_PRESCRIBED_RX_ID, 0);

            verify(prescribedRxRepository).findById(VALID_PRESCRIBED_RX_ID);
            verify(mockMapper).fromEntity(same(prescribedRX));
            assertEquals(dto, result);
        }
    }

    @Nested
    class GetSightTests {
        @Test
        void testGetSightTestsWithValidCustomerId() {
            var sightTest = new SightTest();
            var list = new ArrayList<SightTest>();
            sightTest.setId(UUID.randomUUID());
            list.add(sightTest);

            when(sightTestRepository.findByCustomerIdOrderByCreationDateDesc(VALID_CUSTOMER_ID)).thenReturn(list);

            var result = queryResolver.sightTests(VALID_CUSTOMER_ID);

            verify(sightTestMapper).map(sightTest);
            assertEquals(sightTest.getId(), result.get(0).getId());
        }

        @Test
        void testGetSightTestsWithInvalidCustomerId() {
            var error = assertThrows(
                    UnexpectedSocratesException.class,
                    () -> queryResolver.sightTests(0)
            );

            assertEquals("Provide a valid customerId", error.getMessage());
        }
    }

    @Nested
    class GetSightTest {
        @Test
        void returnsSightTestWhenSightTestExists() {
            // given
            var id = UUID.randomUUID();
            var mockEntity = mock(SightTest.class);
            when(sightTestRepository.findById(any())).thenReturn(Optional.of(mockEntity));
            var mockDto = mock(SightTestDto.class);
            when(sightTestMapper.map(any(SightTest.class))).thenReturn(mockDto);

            // when
            var actual = queryResolver.sightTest(id);

            // then
            verify(sightTestRepository).findById(id);
            verify(sightTestMapper).map(same(mockEntity));
            assertSame(mockDto, actual);
        }

        @Test
        void throwsNotFoundExceptionWhenNoSightTestFound() {
            // given
            var id = UUID.randomUUID();
            when(sightTestRepository.findById(any())).thenReturn(Optional.empty());

            // when
            var actual = assertThrows(NotFoundException.class, () -> queryResolver.sightTest(id));

            // then
            assertNotNull(actual);
        }
    }
}
