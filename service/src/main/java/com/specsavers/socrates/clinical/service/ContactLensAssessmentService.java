package com.specsavers.socrates.clinical.service;

import com.specsavers.socrates.clinical.mapper.ContactLensMapper;
import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentInputDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessment;
import com.specsavers.socrates.clinical.repository.ContactLensRepository;
import com.specsavers.socrates.common.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Consumer;

import static com.specsavers.socrates.common.entity.Versioned.withVersioned;

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


    public ContactLensAssessmentDto update(UUID contactLensId, long version, TearAssessmentInputDto input) {

        return mutation(contactLensId, version, contactLensAssessment -> {
            if (contactLensAssessment.getTearAssessment() == null) {
                contactLensAssessment.setTearAssessment(TearAssessment.builder().build());
            }
            contactLensMapper.update(input, contactLensAssessment.getTearAssessment());
        });

    }

    private ContactLensAssessmentDto mutation(UUID id, long version, Consumer<ContactLensAssessment> mutation) {
        var contactLensAssessment = contactLensRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return withVersioned(contactLensAssessment, version, () -> {
            mutation.accept(contactLensAssessment);
            return contactLensMapper.fromEntity(contactLensRepository
                    .saveAndFlush(contactLensAssessment));
        });
    }
}
