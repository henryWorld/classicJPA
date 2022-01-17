package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ContactLensMapperTest {
    private final ContactLensMapper contactLensMapper = new ContactLensMapperImpl();

    @Test
    void testTransformFromEntityToDto() {
        var contactLensAssessment = buildContactLensAssessmentEntity();
        var contactLensAssessmentDto = contactLensMapper.fromEntity(contactLensAssessment);
        assertThat(contactLensAssessmentDto.getId()).isEqualTo(contactLensAssessment.getId());
        assertThat(contactLensAssessmentDto.getTrNumber()).isEqualTo(contactLensAssessment.getTrNumber());
        assertThat(contactLensAssessmentDto.getVersion()).isEqualTo(contactLensAssessment.getVersion());
        assertThat(contactLensAssessmentDto.getCreationDate()).isEqualTo(contactLensAssessment.getCreationDate());
    }

    private ContactLensAssessment buildContactLensAssessmentEntity() {
        return ContactLensAssessment.builder()
                .trNumber(23)
                .creationDate(Instant.now())
                .version(20L)
                .build();
    }
}