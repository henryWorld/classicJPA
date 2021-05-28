package com.specsavers.socrates.clinical.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Prism {
    private String horizontal;
    private String vertical;
}
