package com.pernix_central.repository;

import com.pernix_central.domain.Participacion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Participacion entity.
 */
@SuppressWarnings("unused")
public interface ParticipacionRepository extends JpaRepository<Participacion,Long> {

}
