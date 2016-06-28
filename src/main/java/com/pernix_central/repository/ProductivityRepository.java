package com.pernix_central.repository;

import com.pernix_central.domain.Productivity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Productivity entity.
 */
@SuppressWarnings("unused")
public interface ProductivityRepository extends JpaRepository<Productivity,Long> {

}
