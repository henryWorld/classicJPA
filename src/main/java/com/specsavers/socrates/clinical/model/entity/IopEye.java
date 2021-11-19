package com.specsavers.socrates.clinical.model.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import lombok.Data;

@Data
@Entity
public class IopEye {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    UUID id;

    String sphere;
    Float cylinder;
    Float axis;
    String visualAcuity;
    Integer iop1;
    Integer iop2;
    Integer iop3;
    Integer iop4;
}
