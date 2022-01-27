package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.mapper.HabitualRxMapper;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy1Dto;
import com.specsavers.socrates.clinical.model.EyeHealthDrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy2Dto;
import com.specsavers.socrates.clinical.model.HabitualRxDto;
import com.specsavers.socrates.clinical.model.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.ObjectiveAndIopDto;
import com.specsavers.socrates.clinical.model.OptionRecommendationsDto;
import com.specsavers.socrates.clinical.model.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.SightTestDto;
import com.specsavers.socrates.clinical.model.SightTestTypeDto;
import com.specsavers.socrates.clinical.model.entity.EyeHealthAndOphthalmoscopy1;
import com.specsavers.socrates.clinical.model.entity.EyeHealthAndOphthalmoscopy2;
import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import com.specsavers.socrates.clinical.model.entity.ObjectiveAndIop;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.RxNotes;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import com.specsavers.socrates.common.exception.NotFoundException;
import com.specsavers.socrates.common.validation.FieldChecks;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static com.specsavers.socrates.common.entity.Versioned.withVersioned;

@Slf4j
@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class MutationResolver implements GraphQLMutationResolver {

    private final SightTestRepository sightTestRepository;
    private final HabitualRxMapper habitualRxMapper;
    private final SightTestMapper sightTestMapper;
    private final Clock clock;

    @Autowired
    public MutationResolver(SightTestRepository sightTestRepository, HabitualRxMapper habitualRxMapper, SightTestMapper sightTestMapper) {
        this(sightTestRepository, habitualRxMapper, sightTestMapper, Clock.systemUTC());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SightTestDto createSightTest(Integer trNumber, SightTestTypeDto type) {
        log.info("Called createSightTest mutation with the following parameters: trNumber={}, type={}", trNumber, type);

        SightTest sightTest = new SightTest();
        sightTest.setTrNumber(trNumber);
        sightTest.setType(sightTestMapper.map(type));
        sightTest.setCreationDate(LocalDate.now());

        sightTest = save(sightTest);

        return sightTestMapper.map(sightTest);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SightTestDto updateHistoryAndSymptoms(UUID sightTestId, long version, @Valid HistoryAndSymptomsDto input) {
        log.info("Called updateHistoryAndSymptoms: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> sightTestMapper.update(sightTest, input));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SightTestDto updateHabitualRx(UUID sightTestId, long version, int pairNumber, @Valid HabitualRxDto input) {
        return mutation(sightTestId, version, sightTest -> {
            var habituals = sightTest.getHabitualRx();

            var entity = habituals.stream()
                    .filter(rx -> Objects.equals(pairNumber, rx.getPairNumber()))
                    .findFirst()
                    .orElseGet(() -> {
                        var rx = new HabitualRx();
                        rx.setSightTest(sightTest);
                        rx.setPairNumber(pairNumber);
                        habituals.add(rx);
                        return rx;
                    });

            habitualRxMapper.updateEntity(input, entity);
        });
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SightTestDto updateRefractedRx(UUID sightTestId, long version, @Valid RefractedRxDto input) {
        log.info("Called updateRefractedRx: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            if (sightTest.getRefractedRx() == null) {
                sightTest.setRefractedRx(new RefractedRx());
            }

            sightTestMapper.update(input, sightTest.getRefractedRx());
        });
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SightTestDto updateRefractedRxNote(UUID sightTestId, long version, String text) {
        log.info("Called updateRefractedRxNote: sightTestId={}", sightTestId);
        checkNotes(text);

        return mutation(sightTestId, version, sightTest -> {
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
        });
    }

    //region PrescribedRx
    public SightTestDto updatePrescribedRx(UUID sightTestId, long version, @Valid PrescribedRxDto input) {
        log.info("Called updatePrescribedRx: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            if (sightTest.getPrescribedRx() == null) {
                sightTest.setPrescribedRx(new PrescribedRx());
            }

            sightTestMapper.update(input, sightTest.getPrescribedRx());
        });
    }

    public SightTestDto updatePrescribedRxNote(UUID sightTestId, long version, String text) {
        log.info("Called updatePrescribedRxNote: sightTestId={}", sightTestId);
        checkNotes(text);

        return mutation(sightTestId, version, sightTest -> {
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
        });
    }
    //endregion

    //region ObjectiveAndIOP
    public SightTestDto updateObjectiveAndIop(UUID sightTestId, long version, @Valid ObjectiveAndIopDto input) {
        log.info("Called updateObjectiveAndIOP: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            if (sightTest.getObjectiveAndIop() == null) {
                sightTest.setObjectiveAndIop(new ObjectiveAndIop());
            }

            sightTestMapper.update(input, sightTest.getObjectiveAndIop());
        });
    }

    public SightTestDto updateObjectiveAndIopDrugInfo(UUID sightTestId, long version, @Valid DrugInfoDto input) {
        log.info("Called updateObjectiveAndIopDrugInfo: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            var objectiveAndIop = sightTest.getObjectiveAndIop();
            if (objectiveAndIop == null) {
                objectiveAndIop = new ObjectiveAndIop();
                sightTest.setObjectiveAndIop(objectiveAndIop);
            }

            objectiveAndIop.setDrugInfo(sightTestMapper.map(input));
        });
    }
    //endregion

    //region OptionRecommendations
    public SightTestDto updateOptionRecommendations(UUID sightTestId, long version, @Valid OptionRecommendationsDto input) {
        log.info("Called updateOptionRecommendations: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            var recommendations = sightTestMapper.map(input);
            sightTest.setOptionRecommendations(recommendations);
        });
    }

    public SightTestDto updateDispenseNote(UUID sightTestId, long version, String text) {
        log.info("Called updateDispenseNote: sightTestId={}", sightTestId);
        checkNotes(text);

        return mutation(sightTestId, version, sightTest -> sightTest.setDispenseNotes(text));
    }
    //endregion

    //region EyeHealthAndOphthalmoscopy
    public SightTestDto updateEyeHealthAndOphthalmoscopy1(UUID sightTestId, long version, @Valid EyeHealthAndOphthalmoscopy1Dto input) {
        log.info("Called updateEyeHealthAndOphthalmoscopy1: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            var eyeHealth = sightTest.getEyeHealthAndOphthalmoscopy1();
            if (eyeHealth == null) {
                eyeHealth = new EyeHealthAndOphthalmoscopy1();
            }

            sightTestMapper.update(input, eyeHealth);

            sightTest.setEyeHealthAndOphthalmoscopy1(eyeHealth);
        });
    }

    public SightTestDto updateEyeHealthAndOphthalmoscopy1DrugInfo(UUID sightTestId, long version, @Valid EyeHealthDrugInfoDto input) {
        log.info("Called updateEyeHealthAndOphthalmoscopy1DrugInfo: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            var eyeHealth = sightTest.getEyeHealthAndOphthalmoscopy1();
            if (eyeHealth == null) {
                eyeHealth = new EyeHealthAndOphthalmoscopy1();
                sightTest.setEyeHealthAndOphthalmoscopy1(eyeHealth);
            }

            eyeHealth.setDrugInfoEyeHealth(sightTestMapper.map(input));
        });
    }
    //endregion

    //region EyeHealthAndOphthalmoscopy
    public SightTestDto updateEyeHealthAndOphthalmoscopy2(UUID sightTestId, long version, @Valid EyeHealthAndOphthalmoscopy2Dto input) {
        log.info("Called updateEyeHealthAndOphthalmoscopy2: sightTestId={}", sightTestId);

        return mutation(sightTestId, version, sightTest -> {
            var eyeHealth = sightTest.getEyeHealthAndOphthalmoscopy2();
            if (eyeHealth == null) {
                eyeHealth = new EyeHealthAndOphthalmoscopy2();
            }

            sightTestMapper.update(input, eyeHealth);

            sightTest.setEyeHealthAndOphthalmoscopy2(eyeHealth);
        });
    }
    //endregion

    //region Helpers
    private SightTestDto mutation(UUID id, long version, Consumer<SightTest> mutation) {
        var sightTest = sightTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return withVersioned(sightTest, version, () -> {
            mutation.accept(sightTest);
            var saved = save(sightTest);
            return sightTestMapper.map(saved);
        });
    }

    private void checkNotes(String text){
        var textCheck = new FieldChecks("Text", text);
        textCheck.notBlank();
        textCheck.maxLength(220);
    }

    private SightTest save(SightTest sightTest) {
        sightTest.setUpdated(OffsetDateTime.now(clock));
        return sightTestRepository.saveAndFlush(sightTest);
    }
    //endregion
}
