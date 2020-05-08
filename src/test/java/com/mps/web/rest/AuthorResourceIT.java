package com.mps.web.rest;

import com.mps.MpsapcApp;
import com.mps.domain.Author;
import com.mps.domain.Manuscript;
import com.mps.repository.AuthorRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mps.domain.enumeration.ActiveStatus;
/**
 * Integration tests for the {@link AuthorResource} REST controller.
 */
@SpringBootTest(classes = MpsapcApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class AuthorResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "\"K/R@z\\f.|k7OH";
    private static final String UPDATED_EMAIL = "?O@ML[}\"K.Q\\";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final ActiveStatus DEFAULT_ACTIVE_STATUS = ActiveStatus.Active;
    private static final ActiveStatus UPDATED_ACTIVE_STATUS = ActiveStatus.Inactive;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthorMockMvc;

    private Author author;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Author createEntity(EntityManager em) {
        Author author = new Author()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .institute(DEFAULT_INSTITUTE)
            .designation(DEFAULT_DESIGNATION)
            .activeStatus(DEFAULT_ACTIVE_STATUS);
        // Add required entity
        Manuscript manuscript;
        if (TestUtil.findAll(em, Manuscript.class).isEmpty()) {
            manuscript = ManuscriptResourceIT.createEntity(em);
            em.persist(manuscript);
            em.flush();
        } else {
            manuscript = TestUtil.findAll(em, Manuscript.class).get(0);
        }
        author.getFirstNames().add(manuscript);
        return author;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Author createUpdatedEntity(EntityManager em) {
        Author author = new Author()
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .institute(UPDATED_INSTITUTE)
            .designation(UPDATED_DESIGNATION)
            .activeStatus(UPDATED_ACTIVE_STATUS);
        // Add required entity
        Manuscript manuscript;
        if (TestUtil.findAll(em, Manuscript.class).isEmpty()) {
            manuscript = ManuscriptResourceIT.createUpdatedEntity(em);
            em.persist(manuscript);
            em.flush();
        } else {
            manuscript = TestUtil.findAll(em, Manuscript.class).get(0);
        }
        author.getFirstNames().add(manuscript);
        return author;
    }

    @BeforeEach
    public void initTest() {
        author = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthor() throws Exception {
        int databaseSizeBeforeCreate = authorRepository.findAll().size();

        // Create the Author
        restAuthorMockMvc.perform(post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(author)))
            .andExpect(status().isCreated());

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeCreate + 1);
        Author testAuthor = authorList.get(authorList.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAuthor.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAuthor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAuthor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAuthor.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAuthor.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAuthor.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testAuthor.getInstitute()).isEqualTo(DEFAULT_INSTITUTE);
        assertThat(testAuthor.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testAuthor.getActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);
    }

    @Test
    @Transactional
    public void createAuthorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorRepository.findAll().size();

        // Create the Author with an existing ID
        author.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorMockMvc.perform(post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(author)))
            .andExpect(status().isBadRequest());

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().size();
        // set the field null
        author.setFirstName(null);

        // Create the Author, which fails.

        restAuthorMockMvc.perform(post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(author)))
            .andExpect(status().isBadRequest());

        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().size();
        // set the field null
        author.setEmail(null);

        // Create the Author, which fails.

        restAuthorMockMvc.perform(post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(author)))
            .andExpect(status().isBadRequest());

        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().size();
        // set the field null
        author.setCountry(null);

        // Create the Author, which fails.

        restAuthorMockMvc.perform(post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(author)))
            .andExpect(status().isBadRequest());

        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().size();
        // set the field null
        author.setActiveStatus(null);

        // Create the Author, which fails.

        restAuthorMockMvc.perform(post("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(author)))
            .andExpect(status().isBadRequest());

        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthors() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Get all the authorList
        restAuthorMockMvc.perform(get("/api/authors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].institute").value(hasItem(DEFAULT_INSTITUTE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", author.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(author.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.institute").value(DEFAULT_INSTITUTE))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthor() throws Exception {
        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        // Update the author
        Author updatedAuthor = authorRepository.findById(author.getId()).get();
        // Disconnect from session so that the updates on updatedAuthor are not directly saved in db
        em.detach(updatedAuthor);
        updatedAuthor
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .institute(UPDATED_INSTITUTE)
            .designation(UPDATED_DESIGNATION)
            .activeStatus(UPDATED_ACTIVE_STATUS);

        restAuthorMockMvc.perform(put("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuthor)))
            .andExpect(status().isOk());

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
        Author testAuthor = authorList.get(authorList.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAuthor.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAuthor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAuthor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAuthor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAuthor.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAuthor.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAuthor.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testAuthor.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testAuthor.getActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthor() throws Exception {
        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        // Create the Author

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorMockMvc.perform(put("/api/authors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(author)))
            .andExpect(status().isBadRequest());

        // Validate the Author in the database
        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        int databaseSizeBeforeDelete = authorRepository.findAll().size();

        // Delete the author
        restAuthorMockMvc.perform(delete("/api/authors/{id}", author.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Author> authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
