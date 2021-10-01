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
    
    @Transient
    private String unaidedVA;
    private String sphere;
    private String cylinder;
    private Float axis;
    private Float pupillaryDistance;

    //HabitualRX
    private String visualAcuity;
    private Float addition;
    @Embedded
    @AttributeOverride(name = "horizontal", column = @Column(name = "prism_horizontal"))
    @AttributeOverride(name = "vertical", column = @Column(name = "prism_vertical"))
    private Prism prism;

    //Distance
    @Transient
    private Prism distancePrism;
    @Transient
    private String distanceVA;

    //Near
    @Transient
    private Prism nearPrism;
    @Transient
    private String nearVA;
    @Transient
    private Float nearAddition;

    //Intermediate 
    @Transient
    private Float interAddition;
}
