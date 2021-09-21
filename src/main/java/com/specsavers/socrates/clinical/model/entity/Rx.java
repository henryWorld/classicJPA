package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "rx")
public class Rx {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "left_rx_eye_id")
    private RxEye leftEye;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "right_rx_eye_id")
    private RxEye rightEye;

    private String notes;
}
