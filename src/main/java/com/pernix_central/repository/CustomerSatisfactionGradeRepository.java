package com.pernix_central.repository;

import com.pernix_central.domain.CustomerSatisfactionGrade;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerSatisfactionGrade entity.
 */
@SuppressWarnings("unused")
public interface CustomerSatisfactionGradeRepository extends JpaRepository<CustomerSatisfactionGrade,Long> {

}
