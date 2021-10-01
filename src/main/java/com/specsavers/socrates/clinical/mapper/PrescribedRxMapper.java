package com.specsavers.socrates.clinical.mapper;

import com.specsavers.socrates.clinical.legacy.model.Name;
import com.specsavers.socrates.clinical.legacy.model.OptionRecommendation;
import com.specsavers.socrates.clinical.legacy.model.PrescribedRX;
import com.specsavers.socrates.clinical.legacy.model.rx.EyeRX;
import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PrescribedRxMapper {

    public static final PrescribedRxMapper INSTANCE = Mappers.getMapper(PrescribedRxMapper.class);

    @Mapping(target = "clinicianName", source = "sightTest.staff.name")
    @Mapping(target = "testRoomNumber", source = "sightTest.trNumber")
    @Mapping(target = "rightEye", source = "rx.rightEye")
    @Mapping(target = "leftEye", source = "rx.leftEye")
    @Mapping(target = "distanceBinVisualAcuity", source = "rx.distanceBinVisualAcuity")
    @Mapping(target = "unaidedVisualAcuity", source = "rx.unaidedVisualAcuity")
    @Mapping(target = "bvd", source = "rx.bvd")
    @Mapping(target = "testDate", source = "sightTest.record.customerArrivalTime")
    @Mapping(target = "dispenseNotes", source = "sightTest.dispenseNotes")
    @Mapping(target = "recommendations", source = "sightTest.optionRecommendations")
    public abstract PrescribedRxDto fromEntity(PrescribedRX entity);

    abstract EyeRxDto mapEye(EyeRX from);

    OffsetDateTime mapLocalDateTime(LocalDateTime dateTime) {
        if (null == dateTime) return null;

        return OffsetDateTime.of(dateTime, ZoneOffset.UTC);
    }

    String mapClinicianName(Name name) {
        if (null == name) return null;

        return String.join(" ", name.getFirstName(), name.getLastName());
    }

    String mapRecommendations(Collection<OptionRecommendation> recommendations) {
        if (null == recommendations) return null;

        return recommendations
                .stream()
                .map(OptionRecommendation::getText)
                .collect(Collectors.joining(", "));
    }
}
