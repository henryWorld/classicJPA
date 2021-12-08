package com.specsavers.socrates.clinical.legacy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
public class Name {
    @Id
    @Column(name = "name_id")
    private Integer id;

    @NonNull
    private String firstName;
    
    @NonNull
    private String lastName;
}
