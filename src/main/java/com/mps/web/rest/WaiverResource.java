package com.mps.web.rest;

import com.mps.domain.Waiver;
import com.mps.repository.WaiverRepository;
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
 * REST controller for managing {@link com.mps.domain.Waiver}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WaiverResource {

    private final Logger log = LoggerFactory.getLogger(WaiverResource.class);

    private static final String ENTITY_NAME = "waiver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaiverRepository waiverRepository;

    public WaiverResource(WaiverRepository waiverRepository) {
        this.waiverRepository = waiverRepository;
    }

    /**
     * {@code POST  /waivers} : Create a new waiver.
     *
     * @param waiver the waiver to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waiver, or with status {@code 400 (Bad Request)} if the waiver has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/waivers")
    public ResponseEntity<Waiver> createWaiver(@Valid @RequestBody Waiver waiver) throws URISyntaxException {
        log.debug("REST request to save Waiver : {}", waiver);
        if (waiver.getId() != null) {
            throw new BadRequestAlertException("A new waiver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Waiver result = waiverRepository.save(waiver);
        return ResponseEntity.created(new URI("/api/waivers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /waivers} : Updates an existing waiver.
     *
     * @param waiver the waiver to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waiver,
     * or with status {@code 400 (Bad Request)} if the waiver is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waiver couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/waivers")
    public ResponseEntity<Waiver> updateWaiver(@Valid @RequestBody Waiver waiver) throws URISyntaxException {
        log.debug("REST request to update Waiver : {}", waiver);
        if (waiver.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Waiver result = waiverRepository.save(waiver);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waiver.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /waivers} : get all the waivers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waivers in body.
     */
    @GetMapping("/waivers")
    public ResponseEntity<List<Waiver>> getAllWaivers(Pageable pageable) {
        log.debug("REST request to get a page of Waivers");
        Page<Waiver> page = waiverRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /waivers/:id} : get the "id" waiver.
     *
     * @param id the id of the waiver to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waiver, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/waivers/{id}")
    public ResponseEntity<Waiver> getWaiver(@PathVariable Long id) {
        log.debug("REST request to get Waiver : {}", id);
        Optional<Waiver> waiver = waiverRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(waiver);
    }

    /**
     * {@code DELETE  /waivers/:id} : delete the "id" waiver.
     *
     * @param id the id of the waiver to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/waivers/{id}")
    public ResponseEntity<Void> deleteWaiver(@PathVariable Long id) {
        log.debug("REST request to delete Waiver : {}", id);
        waiverRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
