package com.mps.web.rest;

import com.mps.MpsapcApp;
import com.mps.domain.Mail;
import com.mps.domain.Journal;
import com.mps.domain.MailTemplates;
import com.mps.repository.MailRepository;

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
import com.mps.domain.enumeration.ActiveStatus;
/**
 * Integration tests for the {@link MailResource} REST controller.
 */
@SpringBootTest(classes = MpsapcApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MailResourceIT {

    private static final String DEFAULT_MAIL_CONFIGURATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MAIL_CONFIGURATION_NAME = "BBBBBBBBBB";

    private static final APCStatus DEFAULT_STAGE_NAME = APCStatus.Initiated;
    private static final APCStatus UPDATED_STAGE_NAME = APCStatus.Pending;

    private static final ActiveStatus DEFAULT_ACTIVE_STATUS = ActiveStatus.Active;
    private static final ActiveStatus UPDATED_ACTIVE_STATUS = ActiveStatus.Inactive;

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMailMockMvc;

    private Mail mail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mail createEntity(EntityManager em) {
        Mail mail = new Mail()
            .mailConfigurationName(DEFAULT_MAIL_CONFIGURATION_NAME)
            .stageName(DEFAULT_STAGE_NAME)
            .activeStatus(DEFAULT_ACTIVE_STATUS)
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
        mail.setAssociatedJournal(journal);
        // Add required entity
        MailTemplates mailTemplates;
        if (TestUtil.findAll(em, MailTemplates.class).isEmpty()) {
            mailTemplates = MailTemplatesResourceIT.createEntity(em);
            em.persist(mailTemplates);
            em.flush();
        } else {
            mailTemplates = TestUtil.findAll(em, MailTemplates.class).get(0);
        }
        mail.setAssociatedMailTemplate(mailTemplates);
        return mail;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mail createUpdatedEntity(EntityManager em) {
        Mail mail = new Mail()
            .mailConfigurationName(UPDATED_MAIL_CONFIGURATION_NAME)
            .stageName(UPDATED_STAGE_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS)
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
        mail.setAssociatedJournal(journal);
        // Add required entity
        MailTemplates mailTemplates;
        if (TestUtil.findAll(em, MailTemplates.class).isEmpty()) {
            mailTemplates = MailTemplatesResourceIT.createUpdatedEntity(em);
            em.persist(mailTemplates);
            em.flush();
        } else {
            mailTemplates = TestUtil.findAll(em, MailTemplates.class).get(0);
        }
        mail.setAssociatedMailTemplate(mailTemplates);
        return mail;
    }

    @BeforeEach
    public void initTest() {
        mail = createEntity(em);
    }

    @Test
    @Transactional
    public void createMail() throws Exception {
        int databaseSizeBeforeCreate = mailRepository.findAll().size();

        // Create the Mail
        restMailMockMvc.perform(post("/api/mail")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mail)))
            .andExpect(status().isCreated());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeCreate + 1);
        Mail testMail = mailList.get(mailList.size() - 1);
        assertThat(testMail.getMailConfigurationName()).isEqualTo(DEFAULT_MAIL_CONFIGURATION_NAME);
        assertThat(testMail.getStageName()).isEqualTo(DEFAULT_STAGE_NAME);
        assertThat(testMail.getActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);
        assertThat(testMail.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testMail.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createMailWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mailRepository.findAll().size();

        // Create the Mail with an existing ID
        mail.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMailMockMvc.perform(post("/api/mail")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mail)))
            .andExpect(status().isBadRequest());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMailConfigurationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailRepository.findAll().size();
        // set the field null
        mail.setMailConfigurationName(null);

        // Create the Mail, which fails.

        restMailMockMvc.perform(post("/api/mail")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mail)))
            .andExpect(status().isBadRequest());

        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStageNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailRepository.findAll().size();
        // set the field null
        mail.setStageName(null);

        // Create the Mail, which fails.

        restMailMockMvc.perform(post("/api/mail")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mail)))
            .andExpect(status().isBadRequest());

        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailRepository.findAll().size();
        // set the field null
        mail.setActiveStatus(null);

        // Create the Mail, which fails.

        restMailMockMvc.perform(post("/api/mail")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mail)))
            .andExpect(status().isBadRequest());

        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get all the mailList
        restMailMockMvc.perform(get("/api/mail?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mail.getId().intValue())))
            .andExpect(jsonPath("$.[*].mailConfigurationName").value(hasItem(DEFAULT_MAIL_CONFIGURATION_NAME)))
            .andExpect(jsonPath("$.[*].stageName").value(hasItem(DEFAULT_STAGE_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        // Get the mail
        restMailMockMvc.perform(get("/api/mail/{id}", mail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mail.getId().intValue()))
            .andExpect(jsonPath("$.mailConfigurationName").value(DEFAULT_MAIL_CONFIGURATION_NAME))
            .andExpect(jsonPath("$.stageName").value(DEFAULT_STAGE_NAME.toString()))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMail() throws Exception {
        // Get the mail
        restMailMockMvc.perform(get("/api/mail/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        int databaseSizeBeforeUpdate = mailRepository.findAll().size();

        // Update the mail
        Mail updatedMail = mailRepository.findById(mail.getId()).get();
        // Disconnect from session so that the updates on updatedMail are not directly saved in db
        em.detach(updatedMail);
        updatedMail
            .mailConfigurationName(UPDATED_MAIL_CONFIGURATION_NAME)
            .stageName(UPDATED_STAGE_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restMailMockMvc.perform(put("/api/mail")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMail)))
            .andExpect(status().isOk());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeUpdate);
        Mail testMail = mailList.get(mailList.size() - 1);
        assertThat(testMail.getMailConfigurationName()).isEqualTo(UPDATED_MAIL_CONFIGURATION_NAME);
        assertThat(testMail.getStageName()).isEqualTo(UPDATED_STAGE_NAME);
        assertThat(testMail.getActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);
        assertThat(testMail.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testMail.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingMail() throws Exception {
        int databaseSizeBeforeUpdate = mailRepository.findAll().size();

        // Create the Mail

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMailMockMvc.perform(put("/api/mail")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mail)))
            .andExpect(status().isBadRequest());

        // Validate the Mail in the database
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMail() throws Exception {
        // Initialize the database
        mailRepository.saveAndFlush(mail);

        int databaseSizeBeforeDelete = mailRepository.findAll().size();

        // Delete the mail
        restMailMockMvc.perform(delete("/api/mail/{id}", mail.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mail> mailList = mailRepository.findAll();
        assertThat(mailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
