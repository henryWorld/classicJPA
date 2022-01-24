package com.specsavers.socrates.clinical.model.entity;

import com.specsavers.socrates.common.entity.Versioned;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ContactLensAssessment implements Versioned {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private Integer trNumber;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime creationDate;

    @Column(nullable = false)
    @LastModifiedDate
    private OffsetDateTime updatedDate;

    @Version
    private Long version;

    @Embedded
    private TearAssessment tearAssessment;
}
