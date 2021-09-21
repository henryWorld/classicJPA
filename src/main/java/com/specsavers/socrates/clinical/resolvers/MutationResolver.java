package com.specsavers.socrates.clinical.resolvers;


import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.repository.HabitualRxRepository;
import com.specsavers.socrates.clinical.repository.SightTestRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class MutationResolver implements GraphQLMutationResolver {

    private final SightTestRepository sightTestRepository;
    private final HabitualRxRepository habitualRxRepository;
    private final HabitualRxMapper mapper;

    public SightTest createSightTest(Integer trNumber, SightTestType type) {
		log.info("Called createSightTest mutation with the following parameters: trNumber={}, type={}", trNumber, type);
        
        var sightTest = new SightTest();
        sightTest.setTrNumber(trNumber);
        sightTest.setType(type);
        
        sightTestRepository.save(sightTest);

		return sightTest;
	}

    public HabitualRxDto createHabitualRx(UUID sightTestId, Integer pairNumber, HabitualRxDto input) {
        if (!sightTestRepository.existsById(sightTestId)) {
            throw new NotFoundException();
        }

        var entity = mapper.toEntity(sightTestId, pairNumber, input);
        entity = habitualRxRepository.save(entity);

        return mapper.fromEntity(entity);
    }

    public HabitualRxDto updateHabitualRx(UUID id, HabitualRxDto input) {
        var entity = habitualRxRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        mapper.updateEntity(input, entity);
        habitualRxRepository.save(entity);

        return mapper.fromEntity(entity);
    }
}
