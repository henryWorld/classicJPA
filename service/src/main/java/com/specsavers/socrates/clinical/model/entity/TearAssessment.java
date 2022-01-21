package com.specsavers.socrates.clinical.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Builder
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TearAssessment {
    private String observations;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "left_ta_eye_id")
    private TearAssessmentEye leftEye;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "right_ta_eye_id")
    private TearAssessmentEye rightEye;
}
