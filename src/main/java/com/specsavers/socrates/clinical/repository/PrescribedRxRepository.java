package com.specsavers.socrates.clinical.repository;

import java.util.Optional;

import com.specsavers.socrates.clinical.model.PrescribedRX;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescribedRxRepository extends CrudRepository<PrescribedRX, Integer> {
    @Query(value = "SELECT p FROM PrescribedRX p WHERE p.sightTest.trNumber = ?1")
    public Optional<PrescribedRX> findByTestRoomNumber(int testRoomNumber); 
}
