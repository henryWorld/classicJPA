package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Embeddable
@NoArgsConstructor
public class RefractedRx {
    @Embedded
    private SpecificAddition specificAddition;

    @Embedded
    private CurrentSpecsVA currentSpecsVA;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ref_rx_id")
    private Rx rx;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "ref_rx_notes_text"))
    @AttributeOverride(name = "optomName", column = @Column(name = "ref_rx_notes_optom_name"))
    @AttributeOverride(name = "date", column = @Column(name = "ref_rx_notes_date"))
    private RxNotes notes;
}
