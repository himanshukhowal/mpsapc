package com.mps.web.rest;

import com.mps.domain.Journal;
import com.mps.repository.JournalRepository;
import com.mps.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mps.domain.Journal}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JournalResource {

    private final Logger log = LoggerFactory.getLogger(JournalResource.class);

    private static final String ENTITY_NAME = "journal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JournalRepository journalRepository;

    public JournalResource(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    /**
     * {@code POST  /journals} : Create a new journal.
     *
     * @param journal the journal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new journal, or with status {@code 400 (Bad Request)} if the journal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/journals")
    public ResponseEntity<Journal> createJournal(@Valid @RequestBody Journal journal) throws URISyntaxException {
        log.debug("REST request to save Journal : {}", journal);
        if (journal.getId() != null) {
            throw new BadRequestAlertException("A new journal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Journal result = journalRepository.save(journal);
        return ResponseEntity.created(new URI("/api/journals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /journals} : Updates an existing journal.
     *
     * @param journal the journal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated journal,
     * or with status {@code 400 (Bad Request)} if the journal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the journal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/journals")
    public ResponseEntity<Journal> updateJournal(@Valid @RequestBody Journal journal) throws URISyntaxException {
        log.debug("REST request to update Journal : {}", journal);
        if (journal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Journal result = journalRepository.save(journal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, journal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /journals} : get all the journals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of journals in body.
     */
    @GetMapping("/journals")
    public ResponseEntity<List<Journal>> getAllJournals(Pageable pageable) {
        log.debug("REST request to get a page of Journals");
        Page<Journal> page = journalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /journals/:id} : get the "id" journal.
     *
     * @param id the id of the journal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the journal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/journals/{id}")
    public ResponseEntity<Journal> getJournal(@PathVariable Long id) {
        log.debug("REST request to get Journal : {}", id);
        Optional<Journal> journal = journalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(journal);
    }

    /**
     * {@code DELETE  /journals/:id} : delete the "id" journal.
     *
     * @param id the id of the journal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/journals/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable Long id) {
        log.debug("REST request to delete Journal : {}", id);
        journalRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
