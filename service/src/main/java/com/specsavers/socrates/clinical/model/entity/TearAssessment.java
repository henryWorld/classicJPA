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
    @Column(name ="ta_observations")
    private String observations;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ta_left_eye_id")
    private TearAssessmentEye leftEye;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ta_right_eye_id")
    private TearAssessmentEye rightEye;
}
