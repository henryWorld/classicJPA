package com.specsavers.socrates.clinical.legacy.repository;

import com.specsavers.socrates.clinical.legacy.model.PrescribedRX;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescribedRxRepository extends CrudRepository<PrescribedRX, Integer> {
    @Query(value = "SELECT p FROM PrescribedRX p WHERE p.sightTest.trNumber = ?1")
    Optional<PrescribedRX> findByTestRoomNumber(int testRoomNumber);
}
