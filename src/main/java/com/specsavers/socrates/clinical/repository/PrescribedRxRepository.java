package com.specsavers.socrates.clinical.repository;

import com.specsavers.socrates.clinical.types.PrescribedRX;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescribedRxRepository extends CrudRepository<PrescribedRX, String> {
    @Query(value = "SELECT p FROM PrescribedRX p WHERE p.sightTest.trNumber = ?1")
    public PrescribedRX findByTestRoomNumber(Integer testRoomNumber); 
}
