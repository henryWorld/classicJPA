package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.TearAssessmentEyeInputDto;
import com.specsavers.socrates.clinical.model.TearAssessmentInputDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessmentEye;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
                .creationDate(LocalDateTime.now())
                .version(20L)
                .build();
    }

    private TearAssessmentInputDto.TearAssessmentInputDtoBuilder buildTearAssessmentInputDto() {
        return TearAssessmentInputDto.builder()
                .leftEye(buildTAEyeInputDto().build());
    }

    private TearAssessmentEyeInputDto.TearAssessmentEyeInputDtoBuilder buildTAEyeInputDto() {
        return TearAssessmentEyeInputDto.builder().prism("yyyy");
    }

    private TearAssessment.TearAssessmentBuilder buildTearAssessmentEntity() {
        return TearAssessment.builder()
                .leftEye(buildTAEyeEntity().build());
    }

    private TearAssessmentEye.TearAssessmentEyeBuilder buildTAEyeEntity() {
        return TearAssessmentEye.builder().prism("DDDD");
    }

}