package com.specsavers.socrates.clinical.repository;

import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactLensRepository extends JpaRepository<ContactLensAssessment, UUID> {

}
