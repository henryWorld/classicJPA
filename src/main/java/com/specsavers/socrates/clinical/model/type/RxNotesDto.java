package com.specsavers.socrates.clinical.model.type;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RxNotesDto {
    String text;
    String optomName;
    LocalDate date;
}
