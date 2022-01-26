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

import static com.specsavers.socrates.clinical.util.CommonStaticValues.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactLensAssessmentServiceTest {
    private static UUID INVALID_CONTACT_LENS_ID = UUID.randomUUID();
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
    private ContactLensAssessment assignment;
    private ContactLensAssessmentDto assessmentDto;


    @BeforeEach
    void setUp() {
        assignment = CommonStaticValues.CONTACT_LENS_ASSESSMENT.build();
        assessmentDto = CommonStaticValues.CONTACT_LENS_ASSESSMENT_DTO
                .creationDate(assignment.getCreationDate())
                .build();
        lenient().when(contactLensRepository.saveAndFlush(any())).thenReturn(assignment);
        lenient().when(contactLensRepository.findById(VALID_CONTACT_LENS_ID)).thenReturn(Optional.of(assignment));
        lenient().when(contactLensMapper.fromEntity(assignment)).thenReturn(assessmentDto);
        lenient().doNothing().when(contactLensMapper).update(tearAssessmentDto, tearAssessmentEntity);
    }

    @Test
    void testPersistingContactLensAssessment() {
        final var savedClAssignment = contactLensAssessmentService.save(assignment);
        verify(contactLensRepository).saveAndFlush(any());
        assertNotNull(savedClAssignment);
        assertEquals(savedClAssignment.getId(), assignment.getId());
        assertEquals(savedClAssignment.getCreationDate(), assignment.getCreationDate());
        assertEquals(savedClAssignment.getVersion(), assignment.getVersion());
    }

    @Test
    void testGetContactLensAssessmentGivenId() {
        final var retrievedClAssignmentDto
                = contactLensAssessmentService.getContactLensAssessment(VALID_CONTACT_LENS_ID);
        assertEquals(assessmentDto, retrievedClAssignmentDto);
        verify(contactLensRepository).findById(VALID_CONTACT_LENS_ID);
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
    void testPersistTestAssessmentGivenValidId() {
        ContactLensAssessmentDto updatedAssessment = contactLensAssessmentService
                .update(VALID_CONTACT_LENS_ID, VALID_VERSION, TEAR_ASSESSMENT_DTO.build());
        assertEquals(VALID_VERSION, updatedAssessment.getVersion());
        verify(contactLensRepository).findById(VALID_CONTACT_LENS_ID);
    }

    @Test
    void testPersistTestAssessmentGivenInvalidId() {
        doThrow(NotFoundException.class).when(contactLensRepository).findById(any());
        assertThrows(
                NotFoundException.class,
                () -> contactLensAssessmentService.
                        update(INVALID_CONTACT_LENS_ID, VALID_VERSION, TEAR_ASSESSMENT_DTO.build())
        );
    }
}