package com.pernix_central.repository;

import com.pernix_central.domain.CustomerSatisfactionNPS;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerSatisfactionNPS entity.
 */
@SuppressWarnings("unused")
public interface CustomerSatisfactionNPSRepository extends JpaRepository<CustomerSatisfactionNPS,Long> {

}
