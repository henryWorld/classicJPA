package com.specsavers.socrates.clinical.service;

import com.specsavers.socrates.clinical.mapper.ContactLensMapper;
import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.repository.ContactLensRepository;
import com.specsavers.socrates.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ContactLensAssessmentService {
    ContactLensRepository contactLensRepository;
    ContactLensMapper contactLensMapper;

    public ContactLensAssessmentDto save(ContactLensAssessment clAssessment) {
        return contactLensMapper.fromEntity(contactLensRepository.saveAndFlush(clAssessment));
    }

    public ContactLensAssessmentDto getContactLensAssessment(UUID id) {
        return contactLensRepository.findById(id)
                .map(contactLensMapper::fromEntity)
                .orElseThrow(() -> new NotFoundException(id));
    }
}
