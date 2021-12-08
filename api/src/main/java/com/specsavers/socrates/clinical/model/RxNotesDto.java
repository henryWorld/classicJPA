package com.specsavers.socrates.clinical.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RxNotesDto {
    String text;
    String optomName;
    LocalDate date;
}
