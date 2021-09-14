package com.specsavers.socrates.clinical.legacy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class OptionRecommendation {
    @Id
    @Column(name = "option_recommendation_id")
    private int id;

    private String text;
}