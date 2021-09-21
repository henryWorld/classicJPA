package com.specsavers.socrates.clinical.model.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@Data
public class SightTest {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private SightTestType type;

    private Integer trNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sight_test_id")
    private List<HabitualRx> habitualRx;
}
