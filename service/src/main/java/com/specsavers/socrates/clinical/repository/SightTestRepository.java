package com.specsavers.socrates.clinical.repository;

import com.specsavers.socrates.clinical.model.entity.SightTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SightTestRepository extends JpaRepository<SightTest, UUID> { 
    List<SightTest> findByCustomerIdOrderByCreationDateDesc(Integer customerId);
}
