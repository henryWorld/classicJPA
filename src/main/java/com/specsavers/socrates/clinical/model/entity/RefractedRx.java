package com.specsavers.socrates.clinical.model.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import lombok.Data;


@Data
@Entity
public class RefractedRx {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private SpecificAddition specificAddition;

    private CurrentSpecsVA currentSpecsVA;

    @OneToOne
    private Rx rx;
}
