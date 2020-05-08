package com.mps.repository;

import com.mps.domain.Manuscript;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Manuscript entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManuscriptRepository extends JpaRepository<Manuscript, Long> {
}
