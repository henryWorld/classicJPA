package com.specsavers.socrates.clinical.resolvers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.*;

import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.model.PrescribedRX;
import com.specsavers.socrates.clinical.model.rx.RX;
import com.specsavers.socrates.clinical.repository.PrescribedRxRepository;

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
        var prescribedRX =new PrescribedRX();
        prescribedRX.setRx(rx);

        when(prescribedRxRepository.findByTestRoomNumber(VALID_TR_NUMBER_ID))
            .thenReturn(Optional.of(prescribedRX));

        var result = queryResolver.prescribedRX(0, VALID_TR_NUMBER_ID);

        assertEquals(rx, result);
    }

    @Test
    void testPrescribedRXWithValidPrescribedRxId(){

        var rx = new RX();
        var prescribedRX =new PrescribedRX();
        prescribedRX.setRx(rx);

        when(prescribedRxRepository.findById(VALID_PRESCRIBEDRX_ID))
            .thenReturn(Optional.of(prescribedRX));

        var result = queryResolver.prescribedRX(VALID_PRESCRIBEDRX_ID, 0);

        assertEquals(rx, result);
    }
}
