package com.mps.web.rest;

import com.mps.MpsapcApp;
import com.mps.domain.MailTemplates;
import com.mps.domain.Mail;
import com.mps.repository.MailTemplatesRepository;

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

/**
 * Integration tests for the {@link MailTemplatesResource} REST controller.
 */
@SpringBootTest(classes = MpsapcApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MailTemplatesResourceIT {

    private static final String DEFAULT_TEMPLATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL_CC = "AAAAAAAAAA";
    private static final String UPDATED_MAIL_CC = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL_BCC = "AAAAAAAAAA";
    private static final String UPDATED_MAIL_BCC = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_MAIL_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL_BODY = "AAAAAAAAAA";
    private static final String UPDATED_MAIL_BODY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MailTemplatesRepository mailTemplatesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMailTemplatesMockMvc;

    private MailTemplates mailTemplates;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MailTemplates createEntity(EntityManager em) {
        MailTemplates mailTemplates = new MailTemplates()
            .templateName(DEFAULT_TEMPLATE_NAME)
            .mailCC(DEFAULT_MAIL_CC)
            .mailBCC(DEFAULT_MAIL_BCC)
            .mailSubject(DEFAULT_MAIL_SUBJECT)
            .mailBody(DEFAULT_MAIL_BODY)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        // Add required entity
        Mail mail;
        if (TestUtil.findAll(em, Mail.class).isEmpty()) {
            mail = MailResourceIT.createEntity(em);
            em.persist(mail);
            em.flush();
        } else {
            mail = TestUtil.findAll(em, Mail.class).get(0);
        }
        mailTemplates.getTemplateNames().add(mail);
        return mailTemplates;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MailTemplates createUpdatedEntity(EntityManager em) {
        MailTemplates mailTemplates = new MailTemplates()
            .templateName(UPDATED_TEMPLATE_NAME)
            .mailCC(UPDATED_MAIL_CC)
            .mailBCC(UPDATED_MAIL_BCC)
            .mailSubject(UPDATED_MAIL_SUBJECT)
            .mailBody(UPDATED_MAIL_BODY)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);
        // Add required entity
        Mail mail;
        if (TestUtil.findAll(em, Mail.class).isEmpty()) {
            mail = MailResourceIT.createUpdatedEntity(em);
            em.persist(mail);
            em.flush();
        } else {
            mail = TestUtil.findAll(em, Mail.class).get(0);
        }
        mailTemplates.getTemplateNames().add(mail);
        return mailTemplates;
    }

    @BeforeEach
    public void initTest() {
        mailTemplates = createEntity(em);
    }

    @Test
    @Transactional
    public void createMailTemplates() throws Exception {
        int databaseSizeBeforeCreate = mailTemplatesRepository.findAll().size();

        // Create the MailTemplates
        restMailTemplatesMockMvc.perform(post("/api/mail-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mailTemplates)))
            .andExpect(status().isCreated());

        // Validate the MailTemplates in the database
        List<MailTemplates> mailTemplatesList = mailTemplatesRepository.findAll();
        assertThat(mailTemplatesList).hasSize(databaseSizeBeforeCreate + 1);
        MailTemplates testMailTemplates = mailTemplatesList.get(mailTemplatesList.size() - 1);
        assertThat(testMailTemplates.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);
        assertThat(testMailTemplates.getMailCC()).isEqualTo(DEFAULT_MAIL_CC);
        assertThat(testMailTemplates.getMailBCC()).isEqualTo(DEFAULT_MAIL_BCC);
        assertThat(testMailTemplates.getMailSubject()).isEqualTo(DEFAULT_MAIL_SUBJECT);
        assertThat(testMailTemplates.getMailBody()).isEqualTo(DEFAULT_MAIL_BODY);
        assertThat(testMailTemplates.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testMailTemplates.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createMailTemplatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mailTemplatesRepository.findAll().size();

        // Create the MailTemplates with an existing ID
        mailTemplates.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMailTemplatesMockMvc.perform(post("/api/mail-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mailTemplates)))
            .andExpect(status().isBadRequest());

        // Validate the MailTemplates in the database
        List<MailTemplates> mailTemplatesList = mailTemplatesRepository.findAll();
        assertThat(mailTemplatesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTemplateNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailTemplatesRepository.findAll().size();
        // set the field null
        mailTemplates.setTemplateName(null);

        // Create the MailTemplates, which fails.

        restMailTemplatesMockMvc.perform(post("/api/mail-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mailTemplates)))
            .andExpect(status().isBadRequest());

        List<MailTemplates> mailTemplatesList = mailTemplatesRepository.findAll();
        assertThat(mailTemplatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMailTemplates() throws Exception {
        // Initialize the database
        mailTemplatesRepository.saveAndFlush(mailTemplates);

        // Get all the mailTemplatesList
        restMailTemplatesMockMvc.perform(get("/api/mail-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailTemplates.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)))
            .andExpect(jsonPath("$.[*].mailCC").value(hasItem(DEFAULT_MAIL_CC)))
            .andExpect(jsonPath("$.[*].mailBCC").value(hasItem(DEFAULT_MAIL_BCC)))
            .andExpect(jsonPath("$.[*].mailSubject").value(hasItem(DEFAULT_MAIL_SUBJECT)))
            .andExpect(jsonPath("$.[*].mailBody").value(hasItem(DEFAULT_MAIL_BODY)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getMailTemplates() throws Exception {
        // Initialize the database
        mailTemplatesRepository.saveAndFlush(mailTemplates);

        // Get the mailTemplates
        restMailTemplatesMockMvc.perform(get("/api/mail-templates/{id}", mailTemplates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mailTemplates.getId().intValue()))
            .andExpect(jsonPath("$.templateName").value(DEFAULT_TEMPLATE_NAME))
            .andExpect(jsonPath("$.mailCC").value(DEFAULT_MAIL_CC))
            .andExpect(jsonPath("$.mailBCC").value(DEFAULT_MAIL_BCC))
            .andExpect(jsonPath("$.mailSubject").value(DEFAULT_MAIL_SUBJECT))
            .andExpect(jsonPath("$.mailBody").value(DEFAULT_MAIL_BODY))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMailTemplates() throws Exception {
        // Get the mailTemplates
        restMailTemplatesMockMvc.perform(get("/api/mail-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMailTemplates() throws Exception {
        // Initialize the database
        mailTemplatesRepository.saveAndFlush(mailTemplates);

        int databaseSizeBeforeUpdate = mailTemplatesRepository.findAll().size();

        // Update the mailTemplates
        MailTemplates updatedMailTemplates = mailTemplatesRepository.findById(mailTemplates.getId()).get();
        // Disconnect from session so that the updates on updatedMailTemplates are not directly saved in db
        em.detach(updatedMailTemplates);
        updatedMailTemplates
            .templateName(UPDATED_TEMPLATE_NAME)
            .mailCC(UPDATED_MAIL_CC)
            .mailBCC(UPDATED_MAIL_BCC)
            .mailSubject(UPDATED_MAIL_SUBJECT)
            .mailBody(UPDATED_MAIL_BODY)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restMailTemplatesMockMvc.perform(put("/api/mail-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMailTemplates)))
            .andExpect(status().isOk());

        // Validate the MailTemplates in the database
        List<MailTemplates> mailTemplatesList = mailTemplatesRepository.findAll();
        assertThat(mailTemplatesList).hasSize(databaseSizeBeforeUpdate);
        MailTemplates testMailTemplates = mailTemplatesList.get(mailTemplatesList.size() - 1);
        assertThat(testMailTemplates.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);
        assertThat(testMailTemplates.getMailCC()).isEqualTo(UPDATED_MAIL_CC);
        assertThat(testMailTemplates.getMailBCC()).isEqualTo(UPDATED_MAIL_BCC);
        assertThat(testMailTemplates.getMailSubject()).isEqualTo(UPDATED_MAIL_SUBJECT);
        assertThat(testMailTemplates.getMailBody()).isEqualTo(UPDATED_MAIL_BODY);
        assertThat(testMailTemplates.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testMailTemplates.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingMailTemplates() throws Exception {
        int databaseSizeBeforeUpdate = mailTemplatesRepository.findAll().size();

        // Create the MailTemplates

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMailTemplatesMockMvc.perform(put("/api/mail-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mailTemplates)))
            .andExpect(status().isBadRequest());

        // Validate the MailTemplates in the database
        List<MailTemplates> mailTemplatesList = mailTemplatesRepository.findAll();
        assertThat(mailTemplatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMailTemplates() throws Exception {
        // Initialize the database
        mailTemplatesRepository.saveAndFlush(mailTemplates);

        int databaseSizeBeforeDelete = mailTemplatesRepository.findAll().size();

        // Delete the mailTemplates
        restMailTemplatesMockMvc.perform(delete("/api/mail-templates/{id}", mailTemplates.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MailTemplates> mailTemplatesList = mailTemplatesRepository.findAll();
        assertThat(mailTemplatesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
