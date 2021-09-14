package com.specsavers.socrates.clinical.resolvers;


import com.specsavers.socrates.clinical.model.SightTest;
import com.specsavers.socrates.clinical.model.SightTestType;
import com.specsavers.socrates.clinical.repository.SightTestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MutationResolver implements GraphQLMutationResolver {

    @Autowired
    private SightTestRepository sightTestRepository;

    public SightTest createSightTest(Integer trNumber, SightTestType type) {
		log.info("Called createSightTest mutation with the following parameters: trNumber={}, type={}", trNumber, type);
        
        var sightTest = new SightTest();
        sightTest.setTrNumber(trNumber);
        sightTest.setType(type);
        
        sightTestRepository.save(sightTest);

		return sightTest;
	}
}
