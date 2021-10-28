package com.specsavers.socrates.clinical.model.type;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RxNotesDto {
    String text;
    String optomName;
    LocalDate date;
}
