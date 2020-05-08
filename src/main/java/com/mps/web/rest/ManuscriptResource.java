package com.mps.web.rest;

import com.mps.domain.Manuscript;
import com.mps.repository.ManuscriptRepository;
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
 * REST controller for managing {@link com.mps.domain.Manuscript}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ManuscriptResource {

    private final Logger log = LoggerFactory.getLogger(ManuscriptResource.class);

    private static final String ENTITY_NAME = "manuscript";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManuscriptRepository manuscriptRepository;

    public ManuscriptResource(ManuscriptRepository manuscriptRepository) {
        this.manuscriptRepository = manuscriptRepository;
    }

    /**
     * {@code POST  /manuscripts} : Create a new manuscript.
     *
     * @param manuscript the manuscript to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manuscript, or with status {@code 400 (Bad Request)} if the manuscript has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manuscripts")
    public ResponseEntity<Manuscript> createManuscript(@Valid @RequestBody Manuscript manuscript) throws URISyntaxException {
        log.debug("REST request to save Manuscript : {}", manuscript);
        if (manuscript.getId() != null) {
            throw new BadRequestAlertException("A new manuscript cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Manuscript result = manuscriptRepository.save(manuscript);
        return ResponseEntity.created(new URI("/api/manuscripts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manuscripts} : Updates an existing manuscript.
     *
     * @param manuscript the manuscript to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manuscript,
     * or with status {@code 400 (Bad Request)} if the manuscript is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manuscript couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manuscripts")
    public ResponseEntity<Manuscript> updateManuscript(@Valid @RequestBody Manuscript manuscript) throws URISyntaxException {
        log.debug("REST request to update Manuscript : {}", manuscript);
        if (manuscript.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Manuscript result = manuscriptRepository.save(manuscript);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manuscript.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /manuscripts} : get all the manuscripts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manuscripts in body.
     */
    @GetMapping("/manuscripts")
    public ResponseEntity<List<Manuscript>> getAllManuscripts(Pageable pageable) {
        log.debug("REST request to get a page of Manuscripts");
        Page<Manuscript> page = manuscriptRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manuscripts/:id} : get the "id" manuscript.
     *
     * @param id the id of the manuscript to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manuscript, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manuscripts/{id}")
    public ResponseEntity<Manuscript> getManuscript(@PathVariable Long id) {
        log.debug("REST request to get Manuscript : {}", id);
        Optional<Manuscript> manuscript = manuscriptRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(manuscript);
    }

    /**
     * {@code DELETE  /manuscripts/:id} : delete the "id" manuscript.
     *
     * @param id the id of the manuscript to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manuscripts/{id}")
    public ResponseEntity<Void> deleteManuscript(@PathVariable Long id) {
        log.debug("REST request to delete Manuscript : {}", id);
        manuscriptRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
