package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.CurrentSpecsVaDto;
import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeRxDto;
import com.specsavers.socrates.clinical.model.HistoryAndSymptomsDto;
import com.specsavers.socrates.clinical.model.ObjectiveAndIopDto;
import com.specsavers.socrates.clinical.model.OptionRecommendationsDto;
import com.specsavers.socrates.clinical.model.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.SightTestDto;
import com.specsavers.socrates.clinical.model.SightTestTypeDto;
import com.specsavers.socrates.clinical.model.SpecificAdditionDto;
import com.specsavers.socrates.clinical.model.UnaidedVisualAcuityDto;
import com.specsavers.socrates.clinical.model.entity.CurrentSpecsVA;
import com.specsavers.socrates.clinical.model.entity.DrugInfo;
import com.specsavers.socrates.clinical.model.entity.ObjectiveAndIop;
import com.specsavers.socrates.clinical.model.entity.OptionRecommendations;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SightTestType;
import com.specsavers.socrates.clinical.model.entity.SpecificAddition;
import org.mapstruct.BeforeMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SightTestMapper {

    @Mapping(target = "historyAndSymptoms", source = "entity")
    SightTestDto map(SightTest entity);

    @Mapping(target = ".", source = "historyAndSymptoms.lifestyle")
    void update(@MappingTarget SightTest entity, HistoryAndSymptomsDto historyAndSymptoms);

    @Mapping(target = "distanceBinVisualAcuity", source = "rx.distanceBinVA")
    @Mapping(target = "bvd", source = "rx.bvd")
    @Mapping(target = "unaidedVisualAcuity.binocular", source = "rx.unaidedBinVA")
    @Mapping(target = "unaidedVisualAcuity.leftEye", source = "rx.leftEye.unaidedVA")
    @Mapping(target = "unaidedVisualAcuity.rightEye", source = "rx.rightEye.unaidedVA")
    @Mapping(target = "rightEye", source = "rx.rightEye")
    @Mapping(target = "leftEye", source = "rx.leftEye") 
    RefractedRxDto map(RefractedRx value);
    
    @InheritInverseConfiguration
    @Mapping(target = "notes", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(RefractedRxDto source, @MappingTarget RefractedRx target);
    
    @BeforeMapping
    default void beforeUpdate(RefractedRxDto source){
        // Workaround to avoid deleting RX when UnaidedVisualAcuity is null
        if (source.getUnaidedVisualAcuity() == null) {
            source.setUnaidedVisualAcuity(new UnaidedVisualAcuityDto(null, null, null));
        }  
    }

    @Mapping(target = "distanceBinVisualAcuity", source = "rx.distanceBinVA")
    @Mapping(target = "bvd", source = "rx.bvd")
    @Mapping(target = "unaidedVisualAcuity.binocular", source = "rx.unaidedBinVA")
    @Mapping(target = "unaidedVisualAcuity.leftEye", source = "rx.leftEye.unaidedVA")
    @Mapping(target = "unaidedVisualAcuity.rightEye", source = "rx.rightEye.unaidedVA")
    @Mapping(target = "rightEye", source = "rx.rightEye")
    @Mapping(target = "leftEye", source = "rx.leftEye")
    PrescribedRxDto map(PrescribedRx value);

    @InheritInverseConfiguration
    @Mapping(target = "notes", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(PrescribedRxDto source, @MappingTarget PrescribedRx target);

    @Mapping(target = "distanceVisualAcuity", source = "distanceVA")
    @Mapping(target = "nearVisualAcuity", source = "nearVA")
    EyeRxDto map(RxEye value);
    
    @InheritInverseConfiguration
    void update(EyeRxDto source, @MappingTarget RxEye target);

    @Mapping(target = "lifestyle", source = "entity")
    HistoryAndSymptomsDto mapHistoryAndSymptoms(SightTest entity);

    @InheritInverseConfiguration
    @Mapping(target = "drugInfo", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(ObjectiveAndIopDto source, @MappingTarget ObjectiveAndIop target);

    @InheritInverseConfiguration
    DrugInfo map(DrugInfoDto source);

    SpecificAdditionDto map(SpecificAddition value);

    CurrentSpecsVaDto map(CurrentSpecsVA value);

    SightTestType map(SightTestTypeDto dto);

    SightTestTypeDto map(SightTestType entity);

    OptionRecommendations map(OptionRecommendationsDto dto);

    OptionRecommendationsDto map(OptionRecommendations entity);

}
