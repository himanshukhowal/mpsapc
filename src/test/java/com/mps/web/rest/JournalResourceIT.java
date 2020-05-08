package com.mps.web.rest;

import com.mps.MpsapcApp;
import com.mps.domain.Journal;
import com.mps.domain.Manuscript;
import com.mps.repository.JournalRepository;

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

import com.mps.domain.enumeration.ActiveStatus;
/**
 * Integration tests for the {@link JournalResource} REST controller.
 */
@SpringBootTest(classes = MpsapcApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class JournalResourceIT {

    private static final String DEFAULT_JOURNAL_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_JOURNAL_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_JOURNAL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOURNAL_TITLE = "BBBBBBBBBB";

    private static final ActiveStatus DEFAULT_ACTIVE_STATUS = ActiveStatus.Active;
    private static final ActiveStatus UPDATED_ACTIVE_STATUS = ActiveStatus.Inactive;

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJournalMockMvc;

    private Journal journal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journal createEntity(EntityManager em) {
        Journal journal = new Journal()
            .journalAcronym(DEFAULT_JOURNAL_ACRONYM)
            .journalTitle(DEFAULT_JOURNAL_TITLE)
            .activeStatus(DEFAULT_ACTIVE_STATUS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        // Add required entity
        Manuscript manuscript;
        if (TestUtil.findAll(em, Manuscript.class).isEmpty()) {
            manuscript = ManuscriptResourceIT.createEntity(em);
            em.persist(manuscript);
            em.flush();
        } else {
            manuscript = TestUtil.findAll(em, Manuscript.class).get(0);
        }
        journal.getJournalAcronyms().add(manuscript);
        return journal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journal createUpdatedEntity(EntityManager em) {
        Journal journal = new Journal()
            .journalAcronym(UPDATED_JOURNAL_ACRONYM)
            .journalTitle(UPDATED_JOURNAL_TITLE)
            .activeStatus(UPDATED_ACTIVE_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);
        // Add required entity
        Manuscript manuscript;
        if (TestUtil.findAll(em, Manuscript.class).isEmpty()) {
            manuscript = ManuscriptResourceIT.createUpdatedEntity(em);
            em.persist(manuscript);
            em.flush();
        } else {
            manuscript = TestUtil.findAll(em, Manuscript.class).get(0);
        }
        journal.getJournalAcronyms().add(manuscript);
        return journal;
    }

    @BeforeEach
    public void initTest() {
        journal = createEntity(em);
    }

    @Test
    @Transactional
    public void createJournal() throws Exception {
        int databaseSizeBeforeCreate = journalRepository.findAll().size();

        // Create the Journal
        restJournalMockMvc.perform(post("/api/journals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(journal)))
            .andExpect(status().isCreated());

        // Validate the Journal in the database
        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeCreate + 1);
        Journal testJournal = journalList.get(journalList.size() - 1);
        assertThat(testJournal.getJournalAcronym()).isEqualTo(DEFAULT_JOURNAL_ACRONYM);
        assertThat(testJournal.getJournalTitle()).isEqualTo(DEFAULT_JOURNAL_TITLE);
        assertThat(testJournal.getActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);
        assertThat(testJournal.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testJournal.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createJournalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = journalRepository.findAll().size();

        // Create the Journal with an existing ID
        journal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalMockMvc.perform(post("/api/journals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(journal)))
            .andExpect(status().isBadRequest());

        // Validate the Journal in the database
        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkJournalAcronymIsRequired() throws Exception {
        int databaseSizeBeforeTest = journalRepository.findAll().size();
        // set the field null
        journal.setJournalAcronym(null);

        // Create the Journal, which fails.

        restJournalMockMvc.perform(post("/api/journals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(journal)))
            .andExpect(status().isBadRequest());

        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJournalTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = journalRepository.findAll().size();
        // set the field null
        journal.setJournalTitle(null);

        // Create the Journal, which fails.

        restJournalMockMvc.perform(post("/api/journals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(journal)))
            .andExpect(status().isBadRequest());

        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = journalRepository.findAll().size();
        // set the field null
        journal.setActiveStatus(null);

        // Create the Journal, which fails.

        restJournalMockMvc.perform(post("/api/journals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(journal)))
            .andExpect(status().isBadRequest());

        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJournals() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);

        // Get all the journalList
        restJournalMockMvc.perform(get("/api/journals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journal.getId().intValue())))
            .andExpect(jsonPath("$.[*].journalAcronym").value(hasItem(DEFAULT_JOURNAL_ACRONYM)))
            .andExpect(jsonPath("$.[*].journalTitle").value(hasItem(DEFAULT_JOURNAL_TITLE)))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getJournal() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);

        // Get the journal
        restJournalMockMvc.perform(get("/api/journals/{id}", journal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(journal.getId().intValue()))
            .andExpect(jsonPath("$.journalAcronym").value(DEFAULT_JOURNAL_ACRONYM))
            .andExpect(jsonPath("$.journalTitle").value(DEFAULT_JOURNAL_TITLE))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJournal() throws Exception {
        // Get the journal
        restJournalMockMvc.perform(get("/api/journals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJournal() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);

        int databaseSizeBeforeUpdate = journalRepository.findAll().size();

        // Update the journal
        Journal updatedJournal = journalRepository.findById(journal.getId()).get();
        // Disconnect from session so that the updates on updatedJournal are not directly saved in db
        em.detach(updatedJournal);
        updatedJournal
            .journalAcronym(UPDATED_JOURNAL_ACRONYM)
            .journalTitle(UPDATED_JOURNAL_TITLE)
            .activeStatus(UPDATED_ACTIVE_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restJournalMockMvc.perform(put("/api/journals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJournal)))
            .andExpect(status().isOk());

        // Validate the Journal in the database
        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeUpdate);
        Journal testJournal = journalList.get(journalList.size() - 1);
        assertThat(testJournal.getJournalAcronym()).isEqualTo(UPDATED_JOURNAL_ACRONYM);
        assertThat(testJournal.getJournalTitle()).isEqualTo(UPDATED_JOURNAL_TITLE);
        assertThat(testJournal.getActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);
        assertThat(testJournal.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testJournal.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingJournal() throws Exception {
        int databaseSizeBeforeUpdate = journalRepository.findAll().size();

        // Create the Journal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalMockMvc.perform(put("/api/journals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(journal)))
            .andExpect(status().isBadRequest());

        // Validate the Journal in the database
        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJournal() throws Exception {
        // Initialize the database
        journalRepository.saveAndFlush(journal);

        int databaseSizeBeforeDelete = journalRepository.findAll().size();

        // Delete the journal
        restJournalMockMvc.perform(delete("/api/journals/{id}", journal.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Journal> journalList = journalRepository.findAll();
        assertThat(journalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
