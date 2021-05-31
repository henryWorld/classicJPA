package com.specsavers.socrates.clinical.types;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rx")
@SecondaryTables({
    @SecondaryTable(name = "prescribed_rx")
})
public class PrescribedRX {
    @NonNull
    @Id
    @Column(name = "rx_id")
    private String id;
    @Transient
    private String clinicianName;
    @Transient
    private Integer testRoomNumber;

    @Transient
    private PrescribedEyeRX rightEye;
    @Transient
    private PrescribedEyeRX leftEye;
    @Transient
    private String distanceBinVisualAcuity;

    @Embedded
    private UnaidedVA unaidedVisualAcuity;

    private Float bvd;

    @Column(table = "prescribed_rx")
    private Integer recallPeriod;
    @Transient
    private OffsetDateTime testDate;
    @Transient
    private String dispenseNotes;
    @Transient
    private String recommendations;
}
