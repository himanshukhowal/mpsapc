package com.mps.repository;

import com.mps.domain.MailTemplates;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MailTemplates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MailTemplatesRepository extends JpaRepository<MailTemplates, Long> {
}
