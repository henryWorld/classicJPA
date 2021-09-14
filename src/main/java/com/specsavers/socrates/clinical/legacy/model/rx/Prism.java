package com.specsavers.socrates.clinical.legacy.model.rx;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Prism {
    private String horizontal;
    private String vertical;
}
