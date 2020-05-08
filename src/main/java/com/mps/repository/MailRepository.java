package com.mps.repository;

import com.mps.domain.Mail;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Mail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
}
