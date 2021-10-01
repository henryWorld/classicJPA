package com.specsavers.socrates.clinical.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import com.specsavers.socrates.clinical.model.entity.SightTestType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class SightTestDto {
    private UUID id;
    private Integer trNumber;
    private SightTestType type;
    private LocalDate creationDate;
    private PrescribedRxDto prescribedRx;
    private RefractedRxDto refractedRx;
}
