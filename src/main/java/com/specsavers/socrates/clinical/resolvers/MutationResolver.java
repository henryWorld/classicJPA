package com.specsavers.socrates.clinical.resolvers;


import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.RxNotes;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.type.RefractedRxDto;
import com.specsavers.socrates.clinical.model.type.RxNotesDto;
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

    public HabitualRxDto createHabitualRx(UUID sightTestId, Integer pairNumber, @Valid HabitualRxDto input) {
        if (!sightTestRepository.existsById(sightTestId)) {
            throw new NotFoundException();
        }

        var entity = habitualRxMapper.toEntity(sightTestId, pairNumber, input);
        entity = habitualRxRepository.save(entity);

        return habitualRxMapper.fromEntity(entity);
    }

    public HabitualRxDto updateHabitualRx(UUID id, @Valid HabitualRxDto input) {
        var entity = habitualRxRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        habitualRxMapper.updateEntity(input, entity);
        habitualRxRepository.save(entity);

        return habitualRxMapper.fromEntity(entity);
    }

    public RefractedRxDto updateRefractedRx(UUID sightTestId, @Valid RefractedRxDto input) {
        log.info("Called updateRefractedRx: sightTestId={}", sightTestId);
        var sightTest = sightTestRepository.findById(sightTestId)
            .orElseThrow(NotFoundException::new); 

        if (sightTest.getRefractedRx() == null){
            sightTest.setRefractedRx(new RefractedRx());
        }

        sightTestMapper.update(input, sightTest.getRefractedRx());

        return sightTestMapper
            .map(sightTestRepository.save(sightTest))
            .getRefractedRx();
    }

    public RxNotesDto updateRefractedRxNote(UUID sightTestId, String text) {
        log.info("Called updateRefractedRxNote: sightTestId={}", sightTestId);
        var sightTest = sightTestRepository.findById(sightTestId)
            .orElseThrow(NotFoundException::new); 

        var refractedRx = sightTest.getRefractedRx();
        if (refractedRx == null){
            refractedRx = new RefractedRx();
            sightTest.setRefractedRx(refractedRx);
        }

        if (text == null) {
            refractedRx.setNotes(null);
        } else {
            //OptomName is hardcoded while user service is not integrated
            refractedRx.setNotes(new RxNotes(text, "Will Smith", LocalDate.now()));
        }

        var sightTestDto = sightTestMapper.map(sightTestRepository.save(sightTest));
        return sightTestDto.getRefractedRx().getNotes();
    }
}
