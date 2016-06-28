package com.pernix_central.repository;

import com.pernix_central.domain.KPI;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the KPI entity.
 */
@SuppressWarnings("unused")
public interface KPIRepository extends JpaRepository<KPI,Long> {

}
