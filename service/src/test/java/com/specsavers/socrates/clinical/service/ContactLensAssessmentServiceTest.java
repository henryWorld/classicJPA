package com.specsavers.socrates.clinical.service;

import com.specsavers.socrates.clinical.mapper.ContactLensMapper;
import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactLensAssessmentServiceTest {
    @Mock
    private ContactLensRepository contactLensRepository;
    @Mock
    private ContactLensAssessment contactLensAssessment;
    @Mock
    private ContactLensAssessmentDto contactLensAssessmentDto;

    @Mock
    private ContactLensMapper contactLensMapper;

    @InjectMocks
    private ContactLensAssessmentService contactLensAssessmentService;


    @BeforeEach
    void setUp() {
        var clAssignment = CommonStaticValues.CONTACT_LENS_ASSESSMENT.build();
        lenient().when(contactLensRepository.save(any())).thenReturn(clAssignment);
        lenient().when(contactLensRepository.findById(any())).thenReturn(Optional.of(clAssignment));
        lenient().when(contactLensMapper.fromEntity(clAssignment)).thenReturn(contactLensAssessmentDto);
    }

    @Test
    void testPersistingContactLensAssessment() {
        var savedClAssignment = contactLensAssessmentService.save(contactLensAssessment);
        verify(contactLensRepository).save(any());
        assertNotNull(savedClAssignment);
        assertEquals(savedClAssignment.getId(), contactLensAssessment.getId());
        assertEquals(savedClAssignment.getCreationDate(), contactLensAssessment.getCreationDate());
        assertEquals(savedClAssignment.getVersion(), contactLensAssessment.getVersion());
    }

    @Test
    void testGetContactLensAssessmentGivenId() {
        var savedClAssignmentDto = contactLensAssessmentService.save(contactLensAssessment);
        var id = savedClAssignmentDto.getId();
        var retrievedClAssignmentDto
                = contactLensAssessmentService.getContactLensAssessment(id);
        assertEquals(savedClAssignmentDto, retrievedClAssignmentDto);
    }

    @Test
    void testGetContactLensAssessmentWithInvalidId() {
        doThrow(NotFoundException.class).when(contactLensRepository).findById(any());
        var uuid = UUID.randomUUID();
        assertThrows(
                NotFoundException.class,
                () -> contactLensAssessmentService.getContactLensAssessment(uuid)
        );
    }
}