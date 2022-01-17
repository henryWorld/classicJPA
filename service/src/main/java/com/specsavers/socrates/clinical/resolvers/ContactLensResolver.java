package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.service.ContactLensAssessmentService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Validated
public class ContactLensResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

    ContactLensAssessmentService contactLensAssessmentService;
    Clock clock;

    @Autowired
    public ContactLensResolver(ContactLensAssessmentService contactLensAssessmentService) {
        this(contactLensAssessmentService, Clock.systemUTC());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public ContactLensAssessmentDto createContactLensAssessment(int trNumber) {
        log.info("Called createContactLensAssessment mutation with the trNumber={}", trNumber);
        var clAssessment = ContactLensAssessment.builder()
                .trNumber(trNumber)
                .creationDate(Instant.now(clock))
                .build();
        return contactLensAssessmentService.save(clAssessment);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public ContactLensAssessmentDto retrieveContactLensAssessment(UUID id) {
        log.info("Called contactLensAssessment query with the Id={}", id);
        return contactLensAssessmentService.getContactLensAssessment(id);
    }

}
