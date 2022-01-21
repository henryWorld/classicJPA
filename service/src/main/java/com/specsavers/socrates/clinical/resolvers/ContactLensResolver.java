package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentInputDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.service.ContactLensAssessmentService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
public class ContactLensResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

    ContactLensAssessmentService contactLensAssessmentService;

    @Transactional(propagation = Propagation.MANDATORY)
    public ContactLensAssessmentDto createContactLensAssessment(int trNumber) {
        log.info("Called createContactLensAssessment mutation with the trNumber={}", trNumber);
        var clAssessment = ContactLensAssessment.builder()
                .trNumber(trNumber)
                .build();
        return contactLensAssessmentService.save(clAssessment);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public ContactLensAssessmentDto contactLensAssessment(UUID id) {
        log.info("Called contactLensAssessment query with the Id={}", id);
        return contactLensAssessmentService.getContactLensAssessment(id);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public Integer updateTearAssessment(UUID contactLensId, long version, TearAssessmentInputDto input) {
        log.info("Called updateTearAssessment: contactLensId={}", contactLensId);
        var updatedCLAssessment = contactLensAssessmentService.update(contactLensId, version, input);
        return Math.toIntExact(updatedCLAssessment.getVersion());
    }

}
