package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Embeddable
public class PrescribedRx {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pre_rx_id")
    private Rx rx;

    @Column(name = "pre_rx_recall_period")
    private Integer recallPeriod;

    @Embedded
    private SpecificAddition specificAddition;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "pre_rx_notes_text"))
    @AttributeOverride(name = "optomName", column = @Column(name = "pre_rx_notes_optom_name"))
    @AttributeOverride(name = "date", column = @Column(name = "pre_rx_notes_date"))
    private RxNotes notes;
}
