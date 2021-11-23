package com.specsavers.socrates.clinical.legacy.model;

import com.specsavers.socrates.clinical.legacy.model.rx.RX;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "refracted_rx")
public class RefractedRX {
    @Id
    @Column(name = "refracted_rx_id")
    private int id;

    @Embedded
    SpecificAdd specificAddition;

    @Column(name = "current_specs_va_right_as_string")
    private String currentSpecsVARight;

    @Column(name = "current_specs_va_left_as_string")
    private String currentSpecsVALeft;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rx_id")
    private RX rx;
}
