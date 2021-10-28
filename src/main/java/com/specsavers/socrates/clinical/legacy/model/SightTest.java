package com.specsavers.socrates.clinical.legacy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class SightTest {
    @Id
    @Column(name = "sight_test_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "prescribed_rx_id")
    private PrescribedRX prescribedRX;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "prescribed_option_recommendation",
            joinColumns = @JoinColumn(name = "sight_test_id"),
            inverseJoinColumns = @JoinColumn(name = "option_recommendation_id")
    )
    private Collection<OptionRecommendation> optionRecommendations;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @SuppressWarnings("java:S6213") // Rule S6213: Restricted Identifiers should not be used as Identifiers
    @OneToOne
    @JoinColumn(name = "tr_number")
    private Record record;

    @Column(name = "dispense_notes")
    private String dispenseNotes;

    @Column(name = "tr_number", insertable = false, updatable = false)
    private Integer trNumber;
}
