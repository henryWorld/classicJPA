package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Table(name = "habitual_rx")
public class HabitualRx {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "sight_test_id")
    private UUID sightTestId;

    @Column(name = "pair_number")
    private Integer pairNumber;

    private Float age;

    @Column(name = "clinician_name")
    private String clinicianName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rx_id")
    private Rx rx;
}
