package com.specsavers.socrates.clinical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class SightTestDto {
    private UUID id;
    private Integer trNumber;
    private SightTestTypeDto type;
    private LocalDate creationDate;
    private Long version;
    private List<HabitualRxDto> habitualRx;
    private PrescribedRxDto prescribedRx;
    private RefractedRxDto refractedRx;
    private HistoryAndSymptomsDto historyAndSymptoms;
    private ObjectiveAndIopDto objectiveAndIop;
    private OptionRecommendationsDto optionRecommendations;
    private String dispenseNotes;
}
