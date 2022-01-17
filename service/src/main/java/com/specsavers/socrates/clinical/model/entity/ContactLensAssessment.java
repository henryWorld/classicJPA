package com.specsavers.socrates.clinical.model.entity;

import com.specsavers.socrates.common.entity.Versioned;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactLensAssessment implements Versioned {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    private Integer trNumber;
    private Instant creationDate;
    private Instant updatedDate;
    @Version
    private Long version;
}
