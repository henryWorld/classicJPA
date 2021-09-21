package com.specsavers.socrates.clinical.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PrismDto {
    private String horizontal;
    private String vertical;
}
