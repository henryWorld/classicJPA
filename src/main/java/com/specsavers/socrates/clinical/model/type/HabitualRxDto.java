package com.specsavers.socrates.clinical.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class HabitualRxDto {
    private UUID id;
    private UUID sightTestId;
    private Integer pairNumber;
    private Float age;
    private String clinicianName;
    private PrescribedEyeRxDto leftEye;
    private PrescribedEyeRxDto rightEye;
    private String notes;
}
