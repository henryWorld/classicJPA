package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import com.specsavers.socrates.clinical.model.entity.Rx;
import com.specsavers.socrates.clinical.model.entity.RxEye;
import com.specsavers.socrates.clinical.model.HabitualRxDto;
import com.specsavers.socrates.clinical.model.EyeRxDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class HabitualRxMapper {

    public static final HabitualRxMapper INSTANCE = Mappers.getMapper(HabitualRxMapper.class);

    @Mapping(target = "rx", source = "input")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pairNumber", ignore = true)
    @Mapping(target = "sightTest", ignore = true)
    public abstract void updateEntity(HabitualRxDto input, @MappingTarget HabitualRx entity);

    @Mapping(target = "notes", source = "rx.notes")
    @Mapping(target = "leftEye", source = "rx.leftEye")
    @Mapping(target = "rightEye", source = "rx.rightEye")
    public abstract HabitualRxDto fromEntity(HabitualRx entity);

    abstract void mapEye(EyeRxDto input, @MappingTarget RxEye entity);

    @Mapping(target = "id", ignore = true)
    abstract void updateRx(HabitualRxDto input, @MappingTarget Rx entity);

    abstract EyeRxDto mapEye(RxEye entity);
}
