package com.specsavers.socrates.clinical.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.AttributeOverride;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Column;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "rx_eye")
public class RxEye {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    
    @Column(name = "unaided_va")
    private String unaidedVA;
    private String sphere;
    private String cylinder;
    private Float axis;
    private Float pupillaryDistance;

    //region HabitualRX Fields
    private String visualAcuity;
    private Float addition;
    @Embedded
    @AttributeOverride(name = "horizontal", column = @Column(name = "prism_horizontal"))
    @AttributeOverride(name = "vertical", column = @Column(name = "prism_vertical"))
    private Prism prism;
    //endregion

    //region Distance Fields
    @Embedded
    @AttributeOverride(name = "horizontal", column = @Column(name = "dist_prism_horizontal"))
    @AttributeOverride(name = "vertical", column = @Column(name = "dist_prism_vertical"))
    private Prism distancePrism;
    
    @Column(name = "dist_va")
    private String distanceVA;
    //endregion

    //region Near Fields
    @Embedded
    @AttributeOverride(name = "horizontal", column = @Column(name = "near_prism_horizontal"))
    @AttributeOverride(name = "vertical", column = @Column(name = "near_prism_vertical"))
    private Prism nearPrism;

    @Column(name = "near_va")
    private String nearVA;
    private Float nearAddition;
    //endregion

    //region Intermediate Fields
    private Float interAddition;
    //endregion
}
