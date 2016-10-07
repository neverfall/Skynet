package com.pernix_central.repository;

import com.pernix_central.domain.Participation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Participation entity.
 */
@SuppressWarnings("unused")
public interface ParticipationRepository extends JpaRepository<Participation,Long> {

}
