package com.specsavers.socrates.clinical.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ContactLensAssessmentDto {
    private UUID id;
    private OffsetDateTime creationDate;
    private Long version;
    private TearAssessmentDto tearAssessment;
}
