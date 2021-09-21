package com.specsavers.socrates.clinical.repository;

import com.specsavers.socrates.clinical.model.entity.HabitualRx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HabitualRxRepository extends JpaRepository<HabitualRx, UUID> { }
