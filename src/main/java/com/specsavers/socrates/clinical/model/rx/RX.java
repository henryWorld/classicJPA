package com.specsavers.socrates.clinical.model.rx;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.specsavers.socrates.clinical.model.PrescribedRX;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RX {
    @Id
    @Column(name = "rx_id")
    private int id;

    @OneToOne(mappedBy = "rx")
    private PrescribedRX prescribedRX;

    @Column(name = "dist_bin_va_as_string")
    private String distanceBinVisualAcuity;
    
    @Column(name = "bvd")
    private Float bvd;

    @Column(name = "notes")
    private String recommendations;

    @Embedded
    private UnaidedVA unaidedVisualAcuity;

    @Embedded
    @AttributeOverride(name = "sphere", column = @Column(name = "sph_right_as_string"))
    @AttributeOverride(name = "cylinder", column = @Column(name = "cyl_right_as_string"))
    @AttributeOverride(name = "axis", column = @Column(name = "axis_right"))
    @AttributeOverride(name = "distanceVisualAcuity", column = @Column(name = "dist_va_right_as_string"))
    @AttributeOverride(name = "nearVisualAcuity", column = @Column(name = "near_va_right_as_string"))
    @AttributeOverride(name = "pupillaryDistance", column = @Column(name = "pd_right"))
    @AttributeOverride(name = "nearAddition", column = @Column(name = "near_add_right"))
    @AttributeOverride(name = "interAddtion", column = @Column(name = "inter_add_right"))
    @AttributeOverride(name = "distancePrism.horizontal", column = @Column(name = "prism_distance_horizontal_right_as_string"))
    @AttributeOverride(name = "distancePrism.vertical", column = @Column(name = "prism_distance_vertical_right_as_string"))
    @AttributeOverride(name = "nearPrism.horizontal", column = @Column(name = "prism_near_horizontal_right_as_string"))
    @AttributeOverride(name = "nearPrism.vertical", column = @Column(name = "prism_near_vertical_right_as_string"))
    private EyeRX rightEye;

    @Embedded
    @AttributeOverride(name = "sphere", column = @Column(name = "sph_left_as_string"))
    @AttributeOverride(name = "cylinder", column = @Column(name = "cyl_left_as_string"))
    @AttributeOverride(name = "axis", column = @Column(name = "axis_left"))
    @AttributeOverride(name = "distanceVisualAcuity", column = @Column(name = "dist_va_left_as_string"))
    @AttributeOverride(name = "nearVisualAcuity", column = @Column(name = "near_va_left_as_string"))
    @AttributeOverride(name = "pupillaryDistance", column = @Column(name = "pd_left"))
    @AttributeOverride(name = "nearAddition", column = @Column(name = "near_add_left"))
    @AttributeOverride(name = "interAddtion", column = @Column(name = "inter_add_left"))
    @AttributeOverride(name = "distancePrism.horizontal", column = @Column(name = "prism_distance_horizontal_left_as_string"))
    @AttributeOverride(name = "distancePrism.vertical", column = @Column(name = "prism_distance_vertical_left_as_string"))
    @AttributeOverride(name = "nearPrism.horizontal", column = @Column(name = "prism_near_horizontal_left_as_string"))
    @AttributeOverride(name = "nearPrism.vertical", column = @Column(name = "prism_near_vertical_left_as_string"))
    private EyeRX leftEye;
}
