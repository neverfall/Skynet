package com.pernix_central.repository;

import com.pernix_central.domain.FollowUp;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FollowUp entity.
 */
@SuppressWarnings("unused")
public interface FollowUpRepository extends JpaRepository<FollowUp,Long> {

}
