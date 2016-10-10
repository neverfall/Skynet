package com.pernix_central.repository;

import com.pernix_central.domain.Empleado;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Empleado entity.
 */
@SuppressWarnings("unused")
public interface EmpleadoRepository extends JpaRepository<Empleado,Long> {

}
