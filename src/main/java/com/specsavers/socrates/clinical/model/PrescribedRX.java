package com.specsavers.socrates.clinical.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.specsavers.socrates.clinical.model.rx.RX;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "prescribed_rx")
public class PrescribedRX {
    @Id
    @Column(name = "prescribed_rx_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "rx_id")
    private RX rx;

    @OneToOne(mappedBy = "prescribedRX")
    private SightTest sightTest;

    @Column(name = "recall_period")
    private Integer recallPeriod;
}
