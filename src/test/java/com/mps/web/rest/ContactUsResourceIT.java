package com.mps.web.rest;

import com.mps.MpsapcApp;
import com.mps.domain.ContactUs;
import com.mps.repository.ContactUsRepository;

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
 * Integration tests for the {@link ContactUsResource} REST controller.
 */
@SpringBootTest(classes = MpsapcApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ContactUsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTACT = 1;
    private static final Integer UPDATED_CONTACT = 2;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ContactUsRepository contactUsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactUsMockMvc;

    private ContactUs contactUs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUs createEntity(EntityManager em) {
        ContactUs contactUs = new ContactUs()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .contact(DEFAULT_CONTACT)
            .message(DEFAULT_MESSAGE)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return contactUs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUs createUpdatedEntity(EntityManager em) {
        ContactUs contactUs = new ContactUs()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .contact(UPDATED_CONTACT)
            .message(UPDATED_MESSAGE)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return contactUs;
    }

    @BeforeEach
    public void initTest() {
        contactUs = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactUs() throws Exception {
        int databaseSizeBeforeCreate = contactUsRepository.findAll().size();

        // Create the ContactUs
        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isCreated());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeCreate + 1);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactUs.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactUs.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testContactUs.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testContactUs.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testContactUs.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createContactUsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactUsRepository.findAll().size();

        // Create the ContactUs with an existing ID
        contactUs.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactUsRepository.findAll().size();
        // set the field null
        contactUs.setName(null);

        // Create the ContactUs, which fails.

        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isBadRequest());

        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactUsRepository.findAll().size();
        // set the field null
        contactUs.setEmail(null);

        // Create the ContactUs, which fails.

        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isBadRequest());

        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactUsRepository.findAll().size();
        // set the field null
        contactUs.setContact(null);

        // Create the ContactUs, which fails.

        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isBadRequest());

        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactUsRepository.findAll().size();
        // set the field null
        contactUs.setMessage(null);

        // Create the ContactUs, which fails.

        restContactUsMockMvc.perform(post("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isBadRequest());

        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactuses() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        // Get all the contactUsList
        restContactUsMockMvc.perform(get("/api/contactuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactUs.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        // Get the contactUs
        restContactUsMockMvc.perform(get("/api/contactuses/{id}", contactUs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactUs.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactUs() throws Exception {
        // Get the contactUs
        restContactUsMockMvc.perform(get("/api/contactuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();

        // Update the contactUs
        ContactUs updatedContactUs = contactUsRepository.findById(contactUs.getId()).get();
        // Disconnect from session so that the updates on updatedContactUs are not directly saved in db
        em.detach(updatedContactUs);
        updatedContactUs
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .contact(UPDATED_CONTACT)
            .message(UPDATED_MESSAGE)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restContactUsMockMvc.perform(put("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactUs)))
            .andExpect(status().isOk());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactUs.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactUs.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testContactUs.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testContactUs.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testContactUs.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();

        // Create the ContactUs

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUsMockMvc.perform(put("/api/contactuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeDelete = contactUsRepository.findAll().size();

        // Delete the contactUs
        restContactUsMockMvc.perform(delete("/api/contactuses/{id}", contactUs.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
