package com.specsavers.socrates.clinical.model.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class SightTest {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private SightTestType type;

    private LocalDate creationDate;
    private Integer customerId;
    private Integer trNumber;

    @Embedded 
    private PrescribedRx prescribedRx;
    
    @Embedded    
    private RefractedRx refractedRx; 
    
    @Embedded    
    private ObjectiveAndIop objectiveAndIop; 
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sight_test_id")
    private List<HabitualRx> habitualRx;

    @Column(name = "reason_for_visit")
    private String reasonForVisit;

    @Column(name = "general_health")
    private String generalHealth;

    @Column(name = "medication")
    private String medication;

    @Column(name = "ocular_history")
    private String ocularHistory;

    @Column(name = "family_history")
    private String familyHistory;

    @Column(name = "drive_heavy_goods")
    private Boolean driveHeavyGoods;

    @Column(name = "drive_private")
    private Boolean drivePrivate;

    @Column(name = "drive_public")
    private Boolean drivePublic;

    @Column(name = "drive_motorcycle")
    private Boolean driveMotorcycle;

    @Column(name = "vdu")
    private Boolean vdu;

    @Column(name = "vdu_hours_per_day")
    private Integer vduHoursPerDay;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "hobbies")
    private String hobbies;
}
