package com.specsavers.socrates.clinical.model.entity;

import java.time.LocalDate;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class RxNotes {
    String text;
    String optomName;
    LocalDate date;    
}
