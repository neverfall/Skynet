package com.pernix_central.repository;

import com.pernix_central.domain.Gamification;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Gamification entity.
 */
@SuppressWarnings("unused")
public interface GamificationRepository extends JpaRepository<Gamification,Long> {

}
