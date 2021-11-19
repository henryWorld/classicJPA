package com.specsavers.socrates.clinical.model.type;

import lombok.Data;

@Data
public class EyeIopDto {
    String sphere;
    Float cylinder;
    Float axis;
    String visualAcuity;
    Integer iop1;
    Integer iop2;
    Integer iop3;
    Integer iop4;
}
