package com.specsavers.socrates.clinical.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import lombok.Data;

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
}
