package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.service.ContactLensAssessmentService;
import com.specsavers.socrates.clinical.util.CommonStaticValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactLensResolverTest {
    private static final int TR_NUMBER = 24;
    @Mock
    ContactLensAssessmentService contactLensAssessmentService;
    @InjectMocks
    ContactLensResolver contactLensResolver;
    private final Clock clock = Clock.fixed(Instant.parse("2022-01-01T00:00:00.00Z"), ZoneId.systemDefault());
    private ContactLensAssessmentDto contactLensAssessmentDto;

    @BeforeEach
    void setUp() {
        contactLensAssessmentDto = CommonStaticValues.CONTACT_LENS_ASSESSMENT_DTO
                .trNumber(TR_NUMBER)
                .build();

        lenient().when(contactLensAssessmentService.save(any())).thenReturn(contactLensAssessmentDto);
        lenient().when(contactLensAssessmentService.getContactLensAssessment(any())).thenReturn(contactLensAssessmentDto);
        contactLensResolver = new ContactLensResolver(contactLensAssessmentService, clock);
    }

    @Test
    void testPersistingContactLensAssessment() {
        var createdContactLensAssessmentDto = contactLensResolver.createContactLensAssessment(TR_NUMBER);
        assertEquals(createdContactLensAssessmentDto, contactLensAssessmentDto);
    }

    @Test
    void testGetContactLensAssessment() {
        var createdContactLensAssessmentDto = contactLensResolver.createContactLensAssessment(TR_NUMBER);
        var retrievedContactLensAssessmentDto = contactLensResolver.retrieveContactLensAssessment(createdContactLensAssessmentDto.getId());
        assertEquals(retrievedContactLensAssessmentDto, createdContactLensAssessmentDto);
    }
}