package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.*;
import com.specsavers.socrates.clinical.model.entity.*;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ContactLensMapper {
    ContactLensAssessmentDto fromEntity(ContactLensAssessment entity);
}
