package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentEyeDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessmentEye;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ContactLensMapperTest {
    private final ContactLensMapper contactLensMapper = new ContactLensMapperImpl();

    @Test
    void testTransformFromEntityToDto() {
        var contactLensAssessment = buildContactLensAssessmentEntity();
        var contactLensAssessmentDto = contactLensMapper.fromEntity(contactLensAssessment);
        assertThat(contactLensAssessmentDto.getId()).isEqualTo(contactLensAssessment.getId());
        assertThat(contactLensAssessmentDto.getVersion()).isEqualTo(contactLensAssessment.getVersion());
        assertThat(contactLensAssessmentDto.getCreationDate()).isEqualTo(contactLensAssessment.getCreationDate());
    }

    @Test
    void testUpdatingTearAssessmentEntity() {
        var tearAssessmentDto = buildTearAssessmentInputDto().build();
        assertThat(tearAssessmentDto.getLeftEye().getPrism())
                .isEqualTo("yyyy");
        var tearAssessmentEntity = buildTearAssessmentEntity().build();
        assertThat(tearAssessmentEntity.getLeftEye().getPrism())
                .isEqualTo("DDDD");
        contactLensMapper.update(tearAssessmentDto, tearAssessmentEntity);

        assertThat(tearAssessmentEntity.getLeftEye().getPrism())
                .isEqualTo("yyyy");
    }

    private ContactLensAssessment buildContactLensAssessmentEntity() {
        return ContactLensAssessment.builder()
                .trNumber(23)
                .creationDate(OffsetDateTime.now())
                .version(20L)
                .build();
    }

    private TearAssessmentDto.TearAssessmentDtoBuilder buildTearAssessmentInputDto() {
        return TearAssessmentDto.builder()
                .leftEye(buildTAEyeInputDto().build());
    }

    private TearAssessmentEyeDto.TearAssessmentEyeDtoBuilder buildTAEyeInputDto() {
        return TearAssessmentEyeDto.builder().prism("yyyy");
    }

    private TearAssessment.TearAssessmentBuilder buildTearAssessmentEntity() {
        return TearAssessment.builder()
                .leftEye(buildTAEyeEntity().build());
    }

    private TearAssessmentEye.TearAssessmentEyeBuilder buildTAEyeEntity() {
        return TearAssessmentEye.builder().prism("DDDD");
    }

}