package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.repository.SightTestRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.specsavers.socrates.clinical.model.SightTest;
import com.specsavers.socrates.clinical.model.SightTestType;

@ExtendWith(MockitoExtension.class)
class MutationResolverTest {
    @Mock
    private SightTestRepository sightTestRepository;

    @InjectMocks
    private MutationResolver mutationResolver;

    //No need to test invalid values since GraphQL does the validation
    @Test
    void testCreateSightTestWithValidValues(){
        var type = SightTestType.MY_SIGHT_TEST;
        var expectedSightTest = new SightTest();
        
        expectedSightTest.setTrNumber(VALID_TR_NUMBER_ID);
        expectedSightTest.setType(type);

        var result = mutationResolver.createSightTest(VALID_TR_NUMBER_ID, type);

        assertEquals(expectedSightTest, result);
        verify(sightTestRepository).save(expectedSightTest);
    }
    
}
