package com.specsavers.socrates.clinical.resolvers;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.NOT_FOUND_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_PRESCRIBEDRX_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_TR_NUMBER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.legacy.model.PrescribedRX;
import com.specsavers.socrates.clinical.legacy.model.rx.RX;
import com.specsavers.socrates.clinical.legacy.repository.PrescribedRxRepository;

import com.specsavers.socrates.clinical.mapper.PrescribedRxMapper;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import graphql.GraphqlErrorException;

@ExtendWith(MockitoExtension.class)
class QueryTest {

    @Mock
    private PrescribedRxRepository prescribedRxRepository;

    @Mock
    private PrescribedRxMapper mockMapper;

    @InjectMocks
    private Query queryResolver;

    @Test
    void testPrescribedRXWithZeroArgs(){

       var error = assertThrows(
            GraphqlErrorException.class,
            () -> queryResolver.prescribedRX(0, 0)
        );

        assertEquals("Provide a valid id OR testRoomNumber", error.getMessage());
    }

    @Test
    void testPrescribedRXWithBothValidArgs(){

        var error = assertThrows(
            GraphqlErrorException.class,
            () -> queryResolver.prescribedRX(VALID_PRESCRIBEDRX_ID, VALID_TR_NUMBER_ID)
        );

        assertEquals("Cannot search by id and testRoomNumber at same time", error.getMessage());
    }

    @Test
    void testPrescribedRXWithBothNotFoundData(){

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
    void testPrescribedRXWithValidTrNumber(){

        var rx = new RX();
        var prescribedRX = new PrescribedRX();
        prescribedRX.setRx(rx);
        var dto = mock(PrescribedRxDto.class);

        when(prescribedRxRepository.findByTestRoomNumber(VALID_TR_NUMBER_ID))
            .thenReturn(Optional.of(prescribedRX));
        when(mockMapper.fromEntity(any())).thenReturn(dto);

        var result = queryResolver.prescribedRX(0, VALID_TR_NUMBER_ID);

        verify(prescribedRxRepository).findByTestRoomNumber(eq(VALID_TR_NUMBER_ID));
        verify(mockMapper).fromEntity(same(prescribedRX));
        assertEquals(dto, result);
    }

    @Test
    void testPrescribedRXWithValidPrescribedRxId(){

        var rx = new RX();
        var prescribedRX = new PrescribedRX();
        prescribedRX.setRx(rx);
        var dto = mock(PrescribedRxDto.class);

        when(prescribedRxRepository.findById(VALID_PRESCRIBEDRX_ID))
            .thenReturn(Optional.of(prescribedRX));
        when(mockMapper.fromEntity(any())).thenReturn(dto);

        var result = queryResolver.prescribedRX(VALID_PRESCRIBEDRX_ID, 0);

        verify(prescribedRxRepository).findById(eq(VALID_PRESCRIBEDRX_ID));
        verify(mockMapper).fromEntity(same(prescribedRX));
        assertEquals(dto, result);
    }
}
