package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.entity.CurrentSpecsVA;
import com.specsavers.socrates.clinical.model.entity.PrescribedRx;
import com.specsavers.socrates.clinical.model.entity.RefractedRx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.entity.SightTest;
import com.specsavers.socrates.clinical.model.entity.SpecificAddition;
import com.specsavers.socrates.clinical.model.type.CurrentSpecsVaDto;
import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.type.RefractedRxDto;
import com.specsavers.socrates.clinical.model.type.SightTestDto;
import com.specsavers.socrates.clinical.model.type.SpecificAdditionDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SightTestMapper {

    SightTestDto map(SightTest entity);

    @Mapping(target = "distanceBinVisualAcuity", source = "rx.distanceBinVA")
    @Mapping(target = "bvd", source = "rx.bvd")
    @Mapping(target = "unaidedVisualAcuity.binocular", source = "rx.unaidedBinVA")
    @Mapping(target = "unaidedVisualAcuity.leftEye", source = "rx.leftEye.unaidedVA")
    @Mapping(target = "unaidedVisualAcuity.rightEye", source = "rx.rightEye.unaidedVA")
    @Mapping(target = "rightEye", source = "rx.rightEye")
    @Mapping(target = "leftEye", source = "rx.leftEye")
    RefractedRxDto map(RefractedRx value);
    
    SpecificAdditionDto map(SpecificAddition value);

    CurrentSpecsVaDto map(CurrentSpecsVA value);

    @Mapping(target = "distanceBinVisualAcuity", source = "rx.distanceBinVA")
    @Mapping(target = "bvd", source = "rx.bvd")
    @Mapping(target = "unaidedVisualAcuity.binocular", source = "rx.unaidedBinVA")
    @Mapping(target = "unaidedVisualAcuity.leftEye", source = "rx.leftEye.unaidedVA")
    @Mapping(target = "unaidedVisualAcuity.rightEye", source = "rx.rightEye.unaidedVA")
    @Mapping(target = "rightEye", source = "rx.rightEye")
    @Mapping(target = "leftEye", source = "rx.leftEye")
    PrescribedRxDto map(PrescribedRx value);

    @Mapping(target = "distanceVisualAcuity", source = "distanceVA")
    @Mapping(target = "nearVisualAcuity", source = "nearVA")
    EyeRxDto map(RxEye value);
}
