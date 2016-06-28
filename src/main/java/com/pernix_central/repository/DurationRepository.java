package com.pernix_central.repository;

import com.pernix_central.domain.Duration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Duration entity.
 */
@SuppressWarnings("unused")
public interface DurationRepository extends JpaRepository<Duration,Long> {

}
