package com.specsavers.socrates.clinical.service;

import com.specsavers.socrates.clinical.mapper.ContactLensMapper;
import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessment;
import com.specsavers.socrates.clinical.repository.ContactLensRepository;
import com.specsavers.socrates.clinical.util.CommonStaticValues;
import com.specsavers.socrates.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.TEAR_ASSESSMENT_DTO;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_CONTACT_LENS_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactLensAssessmentServiceTest {
    @Mock
    private ContactLensRepository contactLensRepository;

    @Mock
    private TearAssessment tearAssessmentEntity;

    @Mock
    private TearAssessmentDto tearAssessmentDto;

    @Mock
    private ContactLensMapper contactLensMapper;

    @InjectMocks
    private ContactLensAssessmentService contactLensAssessmentService;
    private ContactLensAssessment clAssignment;
   private ContactLensAssessmentDto clAssessmentDto;

    @BeforeEach
    void setUp() {
        clAssignment = CommonStaticValues.CONTACT_LENS_ASSESSMENT.build();
        clAssessmentDto = CommonStaticValues.CONTACT_LENS_ASSESSMENT_DTO
                .creationDate(clAssignment.getCreationDate())
                .build();
        lenient().when(contactLensRepository.saveAndFlush(any())).thenReturn(clAssignment);
        lenient().when(contactLensRepository.findById(VALID_CONTACT_LENS_ID)).thenReturn(Optional.of(clAssignment));
        lenient().when(contactLensMapper.fromEntity(clAssignment)).thenReturn(clAssessmentDto);
        lenient().doNothing().when(contactLensMapper).update(tearAssessmentDto, tearAssessmentEntity);
    }

    @Test
    void testPersistingContactLensAssessment() {
        final var savedClAssignment = contactLensAssessmentService.save(clAssignment);
        verify(contactLensRepository).saveAndFlush(any());
        assertNotNull(savedClAssignment);
        assertEquals(savedClAssignment.getId(),  clAssignment.getId());
        assertEquals(savedClAssignment.getCreationDate(),  clAssignment.getCreationDate());
        assertEquals(savedClAssignment.getVersion(),  clAssignment.getVersion());
    }

    @Test
    void testGetContactLensAssessmentGivenId() {
        final var retrievedClAssignmentDto
                = contactLensAssessmentService.getContactLensAssessment(VALID_CONTACT_LENS_ID);
        assertEquals(clAssessmentDto, retrievedClAssignmentDto);
    }

    @Test
    void testGetContactLensAssessmentWithInvalidId() {
        doThrow(NotFoundException.class).when(contactLensRepository).findById(any());
        final var uuid = UUID.randomUUID();
        assertThrows(
                NotFoundException.class,
                () -> contactLensAssessmentService.getContactLensAssessment(uuid)
        );
    }


    @Test
    void testPersistTestAssessment() {
        ContactLensAssessmentDto updatedClAssessment = contactLensAssessmentService
                .update(VALID_CONTACT_LENS_ID, 20L, TEAR_ASSESSMENT_DTO.build());
        assertEquals(20L, updatedClAssessment.getVersion());
    }
}