package com.specsavers.socrates.clinical.model.type;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LifestyleDto {
    private Boolean driveHeavyGoods;
    private Boolean drivePrivate;
    private Boolean drivePublic;
    private Boolean driveMotorcycle;
    private Boolean vdu;
    private Integer vduHoursPerDay;
    private String occupation;
    private String hobbies;
}
