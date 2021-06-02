package com.specsavers.socrates.clinical.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Staff {
    @Id
    @Column(name = "staff_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "name_id")
    private Name name;
}
