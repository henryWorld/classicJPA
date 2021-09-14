package com.specsavers.socrates.clinical.legacy.model.rx;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UnaidedVA {

    @Column(name = "vision_right_as_string")
    private String rightEye;
    @Column(name = "vision_left_as_string")
    private String leftEye;
    @Column(name = "bin_vision_as_string")
    private String binocular;
}
