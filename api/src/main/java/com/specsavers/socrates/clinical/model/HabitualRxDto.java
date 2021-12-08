package com.specsavers.socrates.clinical.model;

import com.specsavers.socrates.clinical.model.validator.HabitualRxValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@HabitualRxValidation
public final class HabitualRxDto {
    private UUID id;
    private UUID sightTestId;
    private Integer pairNumber;
    private Float age;
    private String clinicianName;
    private EyeRxDto leftEye;
    private EyeRxDto rightEye;
    private String notes;
}
