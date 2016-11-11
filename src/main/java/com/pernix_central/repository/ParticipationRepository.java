package com.pernix_central.repository;

import com.pernix_central.domain.Participation;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
 * Spring Data JPA repository for the Participation entity.
 */
@SuppressWarnings("unused")
public interface ParticipationRepository extends JpaRepository<Participation,Long> {

    @Query("select participation from Participation participation where participation.user.login = ?#{principal.username}")
    List<Participation> findByUserIsCurrentUser();

}
