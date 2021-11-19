package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.model.entity.ObjectiveAndIop;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.RxNotes;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.model.type.DrugInfoDto;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.type.ObjectiveAndIopDto;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.type.RefractedRxDto;
import com.specsavers.socrates.clinical.model.type.RxNotesDto;
import com.specsavers.socrates.clinical.model.type.SightTestDto;
import com.specsavers.socrates.clinical.repository.HabitualRxRepository;
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import com.specsavers.socrates.common.exception.NotFoundException;
import com.specsavers.socrates.common.validation.FieldChecks;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(propagation = Propagation.MANDATORY)
    public SightTestDto createSightTest(Integer trNumber, SightTestType type) {
        log.info("Called createSightTest mutation with the following parameters: trNumber={}, type={}", trNumber, type);

        SightTest sightTest = new SightTest();
        sightTest.setTrNumber(trNumber);
        sightTest.setType(type);
        sightTest.setCreationDate(LocalDate.now());

        sightTest = sightTestRepository.save(sightTest);

        return sightTestMapper.map(sightTest);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public HistoryAndSymptomsDto updateHistoryAndSymptoms(UUID sightTestId, @Valid HistoryAndSymptomsDto input) {
        log.info("Called updateHistoryAndSymptoms: sightTestId={}", sightTestId);

        SightTest sightTest = sightTestRepository.findById(sightTestId)
                .orElseThrow(() -> new NotFoundException(sightTestId));
        sightTestMapper.update(sightTest, input);
        var saved = sightTestRepository.save(sightTest);
        return sightTestMapper
                .map(saved)
                .getHistoryAndSymptoms();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public HabitualRxDto createHabitualRx(UUID sightTestId, Integer pairNumber, @Valid HabitualRxDto input) {
        if (!sightTestRepository.existsById(sightTestId)) {
            throw new NotFoundException(sightTestId);
        }

        var entity = habitualRxMapper.toEntity(sightTestId, pairNumber, input);
        entity = habitualRxRepository.save(entity);

        return habitualRxMapper.fromEntity(entity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public HabitualRxDto updateHabitualRx(UUID id, @Valid HabitualRxDto input) {
        var entity = habitualRxRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        habitualRxMapper.updateEntity(input, entity);
        habitualRxRepository.save(entity);

        return habitualRxMapper.fromEntity(entity);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public RefractedRxDto updateRefractedRx(UUID sightTestId, @Valid RefractedRxDto input) {
        log.info("Called updateRefractedRx: sightTestId={}", sightTestId);
        var sightTest = sightTestRepository.findById(sightTestId)
                .orElseThrow(() -> new NotFoundException(sightTestId));

        if (sightTest.getRefractedRx() == null) {
            sightTest.setRefractedRx(new RefractedRx());
        }

        sightTestMapper.update(input, sightTest.getRefractedRx());

        return sightTestMapper
                .map(sightTestRepository.save(sightTest))
                .getRefractedRx();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public RxNotesDto updateRefractedRxNote(UUID sightTestId, String text) {
        log.info("Called updateRefractedRxNote: sightTestId={}", sightTestId);
        var textCheck = new FieldChecks("Text", text);
        textCheck.notBlank();
        textCheck.maxLength(220);

        var sightTest = sightTestRepository.findById(sightTestId)
                .orElseThrow(() -> new NotFoundException(sightTestId));

        var refractedRx = sightTest.getRefractedRx();
        if (refractedRx == null) {
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

    //region PrescribedRx
    public PrescribedRxDto updatePrescribedRx(UUID sightTestId, @Valid PrescribedRxDto input) {
        log.info("Called updatePrescribedRx: sightTestId={}", sightTestId);
        var sightTest = sightTestRepository.findById(sightTestId)
                .orElseThrow(() -> new NotFoundException(sightTestId));

        if (sightTest.getPrescribedRx() == null) {
            sightTest.setPrescribedRx(new PrescribedRx());
        }

        sightTestMapper.update(input, sightTest.getPrescribedRx());

        return sightTestMapper
                .map(sightTestRepository.save(sightTest))
                .getPrescribedRx();
    }

    public RxNotesDto updatePrescribedRxNote(UUID sightTestId, String text) {
        log.info("Called updatePrescribedRxNote: sightTestId={}", sightTestId);
        var textCheck = new FieldChecks("Text", text);
        textCheck.notBlank();
        textCheck.maxLength(220);

        var sightTest = sightTestRepository.findById(sightTestId)
                .orElseThrow(() -> new NotFoundException(sightTestId));

        var prescribedRx = sightTest.getPrescribedRx();
        if (prescribedRx == null) {
            prescribedRx = new PrescribedRx();
            sightTest.setPrescribedRx(prescribedRx);
        }

        if (text == null) {
            prescribedRx.setNotes(null);
        } else {
            //OptomName is hardcoded while user service is not integrated
            prescribedRx.setNotes(new RxNotes(text, "Will Smith", LocalDate.now()));
        }

        var sightTestDto = sightTestMapper.map(sightTestRepository.save(sightTest));
        return sightTestDto.getPrescribedRx().getNotes();
    }
    //endregion

    //region ObjectiveAndIOP
    public ObjectiveAndIopDto updateObjectiveAndIop(UUID sightTestId, @Valid ObjectiveAndIopDto input) {
        log.info("Called updateObjectiveAndIOP: sightTestId={}", sightTestId);
        var sightTest = sightTestRepository.findById(sightTestId)
            .orElseThrow(() -> new NotFoundException(sightTestId));

        if (sightTest.getObjectiveAndIop() == null){
            sightTest.setObjectiveAndIop(new ObjectiveAndIop());
        }

        sightTestMapper.update(input, sightTest.getObjectiveAndIop());

        return sightTestMapper
            .map(sightTestRepository.save(sightTest))
            .getObjectiveAndIop();
    }

    public DrugInfoDto updateObjectiveAndIopDrugInfo(UUID sightTestId, @Valid DrugInfoDto input) {
        log.info("Called updateObjectiveAndIopDrugInfo: sightTestId={}", sightTestId);
        var sightTest = sightTestRepository.findById(sightTestId)
            .orElseThrow(() -> new NotFoundException(sightTestId));

        var objectiveAndIop = sightTest.getObjectiveAndIop();
        if (objectiveAndIop == null) {
            objectiveAndIop = new ObjectiveAndIop();
            sightTest.setObjectiveAndIop(objectiveAndIop);
        }

        objectiveAndIop.setDrugInfo(sightTestMapper.map(input));

        return sightTestMapper
            .map(sightTestRepository.save(sightTest))
            .getObjectiveAndIop()
            .getDrugInfo();
    }
    //endregion
}
