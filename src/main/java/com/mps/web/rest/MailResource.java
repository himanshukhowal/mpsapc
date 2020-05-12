package com.mps.web.rest;

import com.mps.domain.Mail;
import com.mps.repository.MailRepository;
import com.mps.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mps.domain.Mail}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MailResource {

    private final Logger log = LoggerFactory.getLogger(MailResource.class);

    private static final String ENTITY_NAME = "mail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MailRepository mailRepository;

    public MailResource(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    /**
     * {@code POST  /mail} : Create a new mail.
     *
     * @param mail the mail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mail, or with status {@code 400 (Bad Request)} if the mail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mail")
    public ResponseEntity<Mail> createMail(@Valid @RequestBody Mail mail) throws URISyntaxException {
        log.debug("REST request to save Mail : {}", mail);
        if (mail.getId() != null) {
            throw new BadRequestAlertException("A new mail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mail result = mailRepository.save(mail);
        return ResponseEntity.created(new URI("/api/mail/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mail} : Updates an existing mail.
     *
     * @param mail the mail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mail,
     * or with status {@code 400 (Bad Request)} if the mail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mail")
    public ResponseEntity<Mail> updateMail(@Valid @RequestBody Mail mail) throws URISyntaxException {
        log.debug("REST request to update Mail : {}", mail);
        if (mail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Mail result = mailRepository.save(mail);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mail.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mail} : get all the mail.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mail in body.
     */
    @GetMapping("/mail")
    public List<Mail> getAllMail() {
        log.debug("REST request to get all Mail");
        return mailRepository.findAll();
    }

    /**
     * {@code GET  /mail/:id} : get the "id" mail.
     *
     * @param id the id of the mail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mail/{id}")
    public ResponseEntity<Mail> getMail(@PathVariable Long id) {
        log.debug("REST request to get Mail : {}", id);
        Optional<Mail> mail = mailRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mail);
    }

    /**
     * {@code DELETE  /mail/:id} : delete the "id" mail.
     *
     * @param id the id of the mail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mail/{id}")
    public ResponseEntity<Void> deleteMail(@PathVariable Long id) {
        log.debug("REST request to delete Mail : {}", id);
        mailRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
