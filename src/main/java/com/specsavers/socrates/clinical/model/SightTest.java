package com.specsavers.socrates.clinical.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.specsavers.socrates.clinical.types.PrescribedRX;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "tr_number")
    private Record record;

    @Column(name = "dispense_notes")
    private String dispenseNotes;

    @Column(name = "tr_number", insertable = false, updatable = false)
    private Integer trNumber;
}
