package com.specsavers.socrates.clinical.model.entity;

import com.specsavers.socrates.common.entity.Versioned;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Version;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class SightTest implements Versioned {
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

    @Embedded
    private OptionRecommendations optionRecommendations;

    @Column(name = "dispense_notes")
    private String dispenseNotes;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sightTest")
    @OrderBy("pairNumber")
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

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "updated")
    private OffsetDateTime updated;
}
