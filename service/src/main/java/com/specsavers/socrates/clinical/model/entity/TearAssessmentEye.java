package com.specsavers.socrates.clinical.model.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tear_assessment_eye")
public class TearAssessmentEye {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private String tbut;
    private String prism;
    private String schirmer;
    private String scope;
}
