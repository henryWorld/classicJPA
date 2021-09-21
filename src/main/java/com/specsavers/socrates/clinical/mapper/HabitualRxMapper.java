package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import com.specsavers.socrates.clinical.model.entity.Rx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.type.HabitualRxDto;
import com.specsavers.socrates.clinical.model.type.PrescribedEyeRxDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class HabitualRxMapper {

    public static final HabitualRxMapper INSTANCE = Mappers.getMapper(HabitualRxMapper.class);

    @Mapping(target = "rx", source = "input")
    @Mapping(target = "sightTestId", source = "sightTestId")
    @Mapping(target = "pairNumber", source = "pairNumber")
    public abstract HabitualRx toEntity(UUID sightTestId, int pairNumber, HabitualRxDto input);

    @Mapping(target = "rx", source = "input")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pairNumber", ignore = true)
    @Mapping(target = "sightTestId", ignore = true)
    public abstract void updateEntity(HabitualRxDto input, @MappingTarget HabitualRx entity);

    @Mapping(target = "notes", source = "rx.notes")
    @Mapping(target = "leftEye", source = "rx.leftEye")
    @Mapping(target = "rightEye", source = "rx.rightEye")
    public abstract HabitualRxDto fromEntity(HabitualRx entity);

    @Mapping(target = "prismHorizontal", source = "prism.horizontal")
    @Mapping(target = "prismVertical", source = "prism.vertical")
    abstract void mapEye(PrescribedEyeRxDto input, @MappingTarget RxEye entity);

    @Mapping(target = "id", ignore = true)
    abstract void updateRx(HabitualRxDto input, @MappingTarget Rx entity);

    @InheritConfiguration
    abstract RxEye toRxEye(PrescribedEyeRxDto input);

    @Mapping(target = "prism.horizontal", source = "prismHorizontal")
    @Mapping(target = "prism.vertical", source = "prismVertical")
    abstract PrescribedEyeRxDto mapEye(RxEye entity);
}
