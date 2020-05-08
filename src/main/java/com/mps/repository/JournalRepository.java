package com.mps.repository;

import com.mps.domain.Journal;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Journal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
}
