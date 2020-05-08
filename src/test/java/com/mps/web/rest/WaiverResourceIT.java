package com.mps.web.rest;

import com.mps.MpsapcApp;
import com.mps.domain.Waiver;
import com.mps.repository.WaiverRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mps.domain.enumeration.WaiverType;
import com.mps.domain.enumeration.ActiveStatus;
/**
 * Integration tests for the {@link WaiverResource} REST controller.
 */
@SpringBootTest(classes = MpsapcApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class WaiverResourceIT {

    private static final WaiverType DEFAULT_WAIVER_TYPE = WaiverType.Country;
    private static final WaiverType UPDATED_WAIVER_TYPE = WaiverType.Institute;

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final ActiveStatus DEFAULT_ACTIVE_STATUS = ActiveStatus.Active;
    private static final ActiveStatus UPDATED_ACTIVE_STATUS = ActiveStatus.Inactive;

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private WaiverRepository waiverRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWaiverMockMvc;

    private Waiver waiver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Waiver createEntity(EntityManager em) {
        Waiver waiver = new Waiver()
            .waiverType(DEFAULT_WAIVER_TYPE)
            .entityName(DEFAULT_ENTITY_NAME)
            .activeStatus(DEFAULT_ACTIVE_STATUS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return waiver;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Waiver createUpdatedEntity(EntityManager em) {
        Waiver waiver = new Waiver()
            .waiverType(UPDATED_WAIVER_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return waiver;
    }

    @BeforeEach
    public void initTest() {
        waiver = createEntity(em);
    }

    @Test
    @Transactional
    public void createWaiver() throws Exception {
        int databaseSizeBeforeCreate = waiverRepository.findAll().size();

        // Create the Waiver
        restWaiverMockMvc.perform(post("/api/waivers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(waiver)))
            .andExpect(status().isCreated());

        // Validate the Waiver in the database
        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeCreate + 1);
        Waiver testWaiver = waiverList.get(waiverList.size() - 1);
        assertThat(testWaiver.getWaiverType()).isEqualTo(DEFAULT_WAIVER_TYPE);
        assertThat(testWaiver.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testWaiver.getActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);
        assertThat(testWaiver.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testWaiver.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createWaiverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = waiverRepository.findAll().size();

        // Create the Waiver with an existing ID
        waiver.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaiverMockMvc.perform(post("/api/waivers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(waiver)))
            .andExpect(status().isBadRequest());

        // Validate the Waiver in the database
        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkWaiverTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = waiverRepository.findAll().size();
        // set the field null
        waiver.setWaiverType(null);

        // Create the Waiver, which fails.

        restWaiverMockMvc.perform(post("/api/waivers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(waiver)))
            .andExpect(status().isBadRequest());

        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = waiverRepository.findAll().size();
        // set the field null
        waiver.setEntityName(null);

        // Create the Waiver, which fails.

        restWaiverMockMvc.perform(post("/api/waivers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(waiver)))
            .andExpect(status().isBadRequest());

        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = waiverRepository.findAll().size();
        // set the field null
        waiver.setActiveStatus(null);

        // Create the Waiver, which fails.

        restWaiverMockMvc.perform(post("/api/waivers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(waiver)))
            .andExpect(status().isBadRequest());

        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWaivers() throws Exception {
        // Initialize the database
        waiverRepository.saveAndFlush(waiver);

        // Get all the waiverList
        restWaiverMockMvc.perform(get("/api/waivers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waiver.getId().intValue())))
            .andExpect(jsonPath("$.[*].waiverType").value(hasItem(DEFAULT_WAIVER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getWaiver() throws Exception {
        // Initialize the database
        waiverRepository.saveAndFlush(waiver);

        // Get the waiver
        restWaiverMockMvc.perform(get("/api/waivers/{id}", waiver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(waiver.getId().intValue()))
            .andExpect(jsonPath("$.waiverType").value(DEFAULT_WAIVER_TYPE.toString()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWaiver() throws Exception {
        // Get the waiver
        restWaiverMockMvc.perform(get("/api/waivers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWaiver() throws Exception {
        // Initialize the database
        waiverRepository.saveAndFlush(waiver);

        int databaseSizeBeforeUpdate = waiverRepository.findAll().size();

        // Update the waiver
        Waiver updatedWaiver = waiverRepository.findById(waiver.getId()).get();
        // Disconnect from session so that the updates on updatedWaiver are not directly saved in db
        em.detach(updatedWaiver);
        updatedWaiver
            .waiverType(UPDATED_WAIVER_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restWaiverMockMvc.perform(put("/api/waivers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedWaiver)))
            .andExpect(status().isOk());

        // Validate the Waiver in the database
        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeUpdate);
        Waiver testWaiver = waiverList.get(waiverList.size() - 1);
        assertThat(testWaiver.getWaiverType()).isEqualTo(UPDATED_WAIVER_TYPE);
        assertThat(testWaiver.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testWaiver.getActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);
        assertThat(testWaiver.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testWaiver.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingWaiver() throws Exception {
        int databaseSizeBeforeUpdate = waiverRepository.findAll().size();

        // Create the Waiver

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWaiverMockMvc.perform(put("/api/waivers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(waiver)))
            .andExpect(status().isBadRequest());

        // Validate the Waiver in the database
        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWaiver() throws Exception {
        // Initialize the database
        waiverRepository.saveAndFlush(waiver);

        int databaseSizeBeforeDelete = waiverRepository.findAll().size();

        // Delete the waiver
        restWaiverMockMvc.perform(delete("/api/waivers/{id}", waiver.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Waiver> waiverList = waiverRepository.findAll();
        assertThat(waiverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
