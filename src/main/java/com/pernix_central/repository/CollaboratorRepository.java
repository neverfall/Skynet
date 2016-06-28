package com.pernix_central.repository;

import com.pernix_central.domain.Collaborator;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Collaborator entity.
 */
@SuppressWarnings("unused")
public interface CollaboratorRepository extends JpaRepository<Collaborator,Long> {

}
