package com.specsavers.socrates.clinical.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Record {
    @Id
    @Column(name = "record_id")
    private Integer id;
    private LocalDateTime customerArrivalTime;

}
