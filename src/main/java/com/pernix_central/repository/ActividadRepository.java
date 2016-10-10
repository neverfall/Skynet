package com.pernix_central.repository;

import com.pernix_central.domain.Actividad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Actividad entity.
 */
@SuppressWarnings("unused")
public interface ActividadRepository extends JpaRepository<Actividad,Long> {

}
