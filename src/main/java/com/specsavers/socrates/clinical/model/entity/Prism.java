package com.specsavers.socrates.clinical.model.entity;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Prism {
    private String horizontal;
    private String vertical;
}

