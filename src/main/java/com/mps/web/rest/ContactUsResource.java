package com.mps.web.rest;

import com.mps.domain.ContactUs;
import com.mps.repository.ContactUsRepository;
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
 * REST controller for managing {@link com.mps.domain.ContactUs}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContactUsResource {

    private final Logger log = LoggerFactory.getLogger(ContactUsResource.class);

    private static final String ENTITY_NAME = "contactUs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactUsRepository contactUsRepository;

    public ContactUsResource(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    /**
     * {@code POST  /contactuses} : Create a new contactUs.
     *
     * @param contactUs the contactUs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactUs, or with status {@code 400 (Bad Request)} if the contactUs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contactuses")
    public ResponseEntity<ContactUs> createContactUs(@Valid @RequestBody ContactUs contactUs) throws URISyntaxException {
        log.debug("REST request to save ContactUs : {}", contactUs);
        if (contactUs.getId() != null) {
            throw new BadRequestAlertException("A new contactUs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactUs result = contactUsRepository.save(contactUs);
        return ResponseEntity.created(new URI("/api/contactuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contactuses} : Updates an existing contactUs.
     *
     * @param contactUs the contactUs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUs,
     * or with status {@code 400 (Bad Request)} if the contactUs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactUs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contactuses")
    public ResponseEntity<ContactUs> updateContactUs(@Valid @RequestBody ContactUs contactUs) throws URISyntaxException {
        log.debug("REST request to update ContactUs : {}", contactUs);
        if (contactUs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactUs result = contactUsRepository.save(contactUs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUs.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contactuses} : get all the contactuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactuses in body.
     */
    @GetMapping("/contactuses")
    public List<ContactUs> getAllContactuses() {
        log.debug("REST request to get all Contactuses");
        return contactUsRepository.findAll();
    }

    /**
     * {@code GET  /contactuses/:id} : get the "id" contactUs.
     *
     * @param id the id of the contactUs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactUs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contactuses/{id}")
    public ResponseEntity<ContactUs> getContactUs(@PathVariable Long id) {
        log.debug("REST request to get ContactUs : {}", id);
        Optional<ContactUs> contactUs = contactUsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contactUs);
    }

    /**
     * {@code DELETE  /contactuses/:id} : delete the "id" contactUs.
     *
     * @param id the id of the contactUs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contactuses/{id}")
    public ResponseEntity<Void> deleteContactUs(@PathVariable Long id) {
        log.debug("REST request to delete ContactUs : {}", id);
        contactUsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
