package com.specsavers.socrates.clinical.resolvers;


import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.type.SightTestDto;
import com.specsavers.socrates.clinical.repository.HabitualRxRepository;
import com.specsavers.socrates.clinical.repository.SightTestRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
@Validated
public class MutationResolver implements GraphQLMutationResolver {

    private final SightTestRepository sightTestRepository;
    private final HabitualRxRepository habitualRxRepository;
    private final HabitualRxMapper habitualRxMapper;
    private final SightTestMapper sightTestMapper;

    public SightTestDto createSightTest(Integer trNumber, SightTestType type) {
		log.info("Called createSightTest mutation with the following parameters: trNumber={}, type={}", trNumber, type);
        
        var sightTest = new SightTest();
        sightTest.setTrNumber(trNumber);
        sightTest.setType(type);
        sightTest.setCreationDate(LocalDate.now());
        
        sightTestRepository.save(sightTest);

		return sightTestMapper.map(sightTest);
	}

    public HistoryAndSymptomsDto updateHistoryAndSymptoms(UUID sightTestId, @Valid HistoryAndSymptomsDto input) {
        log.info("Called updateHistoryAndSymptoms: sightTestId={}", sightTestId);

        return sightTestRepository.findById(sightTestId)
                .map(sightTest -> {
                    sightTestMapper.update(sightTest, input);
                    var saved = sightTestRepository.save(sightTest);
                    return sightTestMapper.map(saved);
                })
                .map(SightTestDto::getHistoryAndSymptoms)
                .orElseThrow(NotFoundException::new);
    }

    public HabitualRxDto createHabitualRx(UUID sightTestId, Integer pairNumber, HabitualRxDto input) {
        if (!sightTestRepository.existsById(sightTestId)) {
            throw new NotFoundException();
        }

        var entity = habitualRxMapper.toEntity(sightTestId, pairNumber, input);
        entity = habitualRxRepository.save(entity);

        return habitualRxMapper.fromEntity(entity);
    }

    public HabitualRxDto updateHabitualRx(UUID id, HabitualRxDto input) {
        var entity = habitualRxRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        habitualRxMapper.updateEntity(input, entity);
        habitualRxRepository.save(entity);

        return habitualRxMapper.fromEntity(entity);
    }
}
