package com.mps.web.rest;

import com.mps.MpsapcApp;
import com.mps.domain.Manuscript;
import com.mps.domain.Journal;
import com.mps.repository.ManuscriptRepository;

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

import com.mps.domain.enumeration.APCStatus;
/**
 * Integration tests for the {@link ManuscriptResource} REST controller.
 */
@SpringBootTest(classes = MpsapcApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ManuscriptResourceIT {

    private static final String DEFAULT_MANUSCRIPT_ID = "AAAAAAAAAA";
    private static final String UPDATED_MANUSCRIPT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MANUSCRIPT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MANUSCRIPT_TITLE = "BBBBBBBBBB";

    private static final APCStatus DEFAULT_APC_STATUS = APCStatus.Initiated;
    private static final APCStatus UPDATED_APC_STATUS = APCStatus.Pending;

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ManuscriptRepository manuscriptRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManuscriptMockMvc;

    private Manuscript manuscript;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manuscript createEntity(EntityManager em) {
        Manuscript manuscript = new Manuscript()
            .manuscriptId(DEFAULT_MANUSCRIPT_ID)
            .manuscriptTitle(DEFAULT_MANUSCRIPT_TITLE)
            .apcStatus(DEFAULT_APC_STATUS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        // Add required entity
        Journal journal;
        if (TestUtil.findAll(em, Journal.class).isEmpty()) {
            journal = JournalResourceIT.createEntity(em);
            em.persist(journal);
            em.flush();
        } else {
            journal = TestUtil.findAll(em, Journal.class).get(0);
        }
        manuscript.setManuscriptJournalAcronym(journal);
        return manuscript;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manuscript createUpdatedEntity(EntityManager em) {
        Manuscript manuscript = new Manuscript()
            .manuscriptId(UPDATED_MANUSCRIPT_ID)
            .manuscriptTitle(UPDATED_MANUSCRIPT_TITLE)
            .apcStatus(UPDATED_APC_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);
        // Add required entity
        Journal journal;
        if (TestUtil.findAll(em, Journal.class).isEmpty()) {
            journal = JournalResourceIT.createUpdatedEntity(em);
            em.persist(journal);
            em.flush();
        } else {
            journal = TestUtil.findAll(em, Journal.class).get(0);
        }
        manuscript.setManuscriptJournalAcronym(journal);
        return manuscript;
    }

    @BeforeEach
    public void initTest() {
        manuscript = createEntity(em);
    }

    @Test
    @Transactional
    public void createManuscript() throws Exception {
        int databaseSizeBeforeCreate = manuscriptRepository.findAll().size();

        // Create the Manuscript
        restManuscriptMockMvc.perform(post("/api/manuscripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manuscript)))
            .andExpect(status().isCreated());

        // Validate the Manuscript in the database
        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeCreate + 1);
        Manuscript testManuscript = manuscriptList.get(manuscriptList.size() - 1);
        assertThat(testManuscript.getManuscriptId()).isEqualTo(DEFAULT_MANUSCRIPT_ID);
        assertThat(testManuscript.getManuscriptTitle()).isEqualTo(DEFAULT_MANUSCRIPT_TITLE);
        assertThat(testManuscript.getApcStatus()).isEqualTo(DEFAULT_APC_STATUS);
        assertThat(testManuscript.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testManuscript.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createManuscriptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = manuscriptRepository.findAll().size();

        // Create the Manuscript with an existing ID
        manuscript.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManuscriptMockMvc.perform(post("/api/manuscripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manuscript)))
            .andExpect(status().isBadRequest());

        // Validate the Manuscript in the database
        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkManuscriptIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = manuscriptRepository.findAll().size();
        // set the field null
        manuscript.setManuscriptId(null);

        // Create the Manuscript, which fails.

        restManuscriptMockMvc.perform(post("/api/manuscripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manuscript)))
            .andExpect(status().isBadRequest());

        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkManuscriptTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = manuscriptRepository.findAll().size();
        // set the field null
        manuscript.setManuscriptTitle(null);

        // Create the Manuscript, which fails.

        restManuscriptMockMvc.perform(post("/api/manuscripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manuscript)))
            .andExpect(status().isBadRequest());

        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApcStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = manuscriptRepository.findAll().size();
        // set the field null
        manuscript.setApcStatus(null);

        // Create the Manuscript, which fails.

        restManuscriptMockMvc.perform(post("/api/manuscripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manuscript)))
            .andExpect(status().isBadRequest());

        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllManuscripts() throws Exception {
        // Initialize the database
        manuscriptRepository.saveAndFlush(manuscript);

        // Get all the manuscriptList
        restManuscriptMockMvc.perform(get("/api/manuscripts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manuscript.getId().intValue())))
            .andExpect(jsonPath("$.[*].manuscriptId").value(hasItem(DEFAULT_MANUSCRIPT_ID)))
            .andExpect(jsonPath("$.[*].manuscriptTitle").value(hasItem(DEFAULT_MANUSCRIPT_TITLE)))
            .andExpect(jsonPath("$.[*].apcStatus").value(hasItem(DEFAULT_APC_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getManuscript() throws Exception {
        // Initialize the database
        manuscriptRepository.saveAndFlush(manuscript);

        // Get the manuscript
        restManuscriptMockMvc.perform(get("/api/manuscripts/{id}", manuscript.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manuscript.getId().intValue()))
            .andExpect(jsonPath("$.manuscriptId").value(DEFAULT_MANUSCRIPT_ID))
            .andExpect(jsonPath("$.manuscriptTitle").value(DEFAULT_MANUSCRIPT_TITLE))
            .andExpect(jsonPath("$.apcStatus").value(DEFAULT_APC_STATUS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingManuscript() throws Exception {
        // Get the manuscript
        restManuscriptMockMvc.perform(get("/api/manuscripts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManuscript() throws Exception {
        // Initialize the database
        manuscriptRepository.saveAndFlush(manuscript);

        int databaseSizeBeforeUpdate = manuscriptRepository.findAll().size();

        // Update the manuscript
        Manuscript updatedManuscript = manuscriptRepository.findById(manuscript.getId()).get();
        // Disconnect from session so that the updates on updatedManuscript are not directly saved in db
        em.detach(updatedManuscript);
        updatedManuscript
            .manuscriptId(UPDATED_MANUSCRIPT_ID)
            .manuscriptTitle(UPDATED_MANUSCRIPT_TITLE)
            .apcStatus(UPDATED_APC_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restManuscriptMockMvc.perform(put("/api/manuscripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedManuscript)))
            .andExpect(status().isOk());

        // Validate the Manuscript in the database
        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeUpdate);
        Manuscript testManuscript = manuscriptList.get(manuscriptList.size() - 1);
        assertThat(testManuscript.getManuscriptId()).isEqualTo(UPDATED_MANUSCRIPT_ID);
        assertThat(testManuscript.getManuscriptTitle()).isEqualTo(UPDATED_MANUSCRIPT_TITLE);
        assertThat(testManuscript.getApcStatus()).isEqualTo(UPDATED_APC_STATUS);
        assertThat(testManuscript.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testManuscript.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingManuscript() throws Exception {
        int databaseSizeBeforeUpdate = manuscriptRepository.findAll().size();

        // Create the Manuscript

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManuscriptMockMvc.perform(put("/api/manuscripts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manuscript)))
            .andExpect(status().isBadRequest());

        // Validate the Manuscript in the database
        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteManuscript() throws Exception {
        // Initialize the database
        manuscriptRepository.saveAndFlush(manuscript);

        int databaseSizeBeforeDelete = manuscriptRepository.findAll().size();

        // Delete the manuscript
        restManuscriptMockMvc.perform(delete("/api/manuscripts/{id}", manuscript.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Manuscript> manuscriptList = manuscriptRepository.findAll();
        assertThat(manuscriptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
