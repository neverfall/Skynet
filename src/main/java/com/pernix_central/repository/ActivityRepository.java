package com.pernix_central.repository;

import com.pernix_central.domain.Activity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@SuppressWarnings("unused")
public interface ActivityRepository extends JpaRepository<Activity,Long> {

    @Query("select activity from Activity activity where activity.coordinator.login = ?#{principal.username}")
    List<Activity> findByCoordinatorIsCurrentUser();

}
