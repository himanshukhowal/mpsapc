package com.mps.web.rest;

import com.mps.domain.MailTemplates;
import com.mps.repository.MailTemplatesRepository;
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
 * REST controller for managing {@link com.mps.domain.MailTemplates}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MailTemplatesResource {

    private final Logger log = LoggerFactory.getLogger(MailTemplatesResource.class);

    private static final String ENTITY_NAME = "mailTemplates";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MailTemplatesRepository mailTemplatesRepository;

    public MailTemplatesResource(MailTemplatesRepository mailTemplatesRepository) {
        this.mailTemplatesRepository = mailTemplatesRepository;
    }

    /**
     * {@code POST  /mail-templates} : Create a new mailTemplates.
     *
     * @param mailTemplates the mailTemplates to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mailTemplates, or with status {@code 400 (Bad Request)} if the mailTemplates has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mail-templates")
    public ResponseEntity<MailTemplates> createMailTemplates(@Valid @RequestBody MailTemplates mailTemplates) throws URISyntaxException {
        log.debug("REST request to save MailTemplates : {}", mailTemplates);
        if (mailTemplates.getId() != null) {
            throw new BadRequestAlertException("A new mailTemplates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MailTemplates result = mailTemplatesRepository.save(mailTemplates);
        return ResponseEntity.created(new URI("/api/mail-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mail-templates} : Updates an existing mailTemplates.
     *
     * @param mailTemplates the mailTemplates to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mailTemplates,
     * or with status {@code 400 (Bad Request)} if the mailTemplates is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mailTemplates couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mail-templates")
    public ResponseEntity<MailTemplates> updateMailTemplates(@Valid @RequestBody MailTemplates mailTemplates) throws URISyntaxException {
        log.debug("REST request to update MailTemplates : {}", mailTemplates);
        if (mailTemplates.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MailTemplates result = mailTemplatesRepository.save(mailTemplates);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mailTemplates.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mail-templates} : get all the mailTemplates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mailTemplates in body.
     */
    @GetMapping("/mail-templates")
    public List<MailTemplates> getAllMailTemplates() {
        log.debug("REST request to get all MailTemplates");
        return mailTemplatesRepository.findAll();
    }

    /**
     * {@code GET  /mail-templates/:id} : get the "id" mailTemplates.
     *
     * @param id the id of the mailTemplates to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mailTemplates, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mail-templates/{id}")
    public ResponseEntity<MailTemplates> getMailTemplates(@PathVariable Long id) {
        log.debug("REST request to get MailTemplates : {}", id);
        Optional<MailTemplates> mailTemplates = mailTemplatesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mailTemplates);
    }

    /**
     * {@code DELETE  /mail-templates/:id} : delete the "id" mailTemplates.
     *
     * @param id the id of the mailTemplates to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mail-templates/{id}")
    public ResponseEntity<Void> deleteMailTemplates(@PathVariable Long id) {
        log.debug("REST request to delete MailTemplates : {}", id);
        mailTemplatesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
