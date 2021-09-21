package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rx_eye")
public class RxEye {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private String sphere;
    private String cylinder;
    private Float axis;
    private String visualAcuity;
    private Float pupillaryDistance;
    private Float addition;
    private String prismHorizontal;
    private String prismVertical;
}
