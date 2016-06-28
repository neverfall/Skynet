package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Collaborator;
import com.pernix_central.repository.CollaboratorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CollaboratorResource REST controller.
 *
 * @see CollaboratorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class CollaboratorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final Boolean DEFAULT_APPRENTICE = false;
    private static final Boolean UPDATED_APPRENTICE = true;

    @Inject
    private CollaboratorRepository collaboratorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCollaboratorMockMvc;

    private Collaborator collaborator;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollaboratorResource collaboratorResource = new CollaboratorResource();
        ReflectionTestUtils.setField(collaboratorResource, "collaboratorRepository", collaboratorRepository);
        this.restCollaboratorMockMvc = MockMvcBuilders.standaloneSetup(collaboratorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        collaborator = new Collaborator();
        collaborator.setName(DEFAULT_NAME);
        collaborator.setLastName(DEFAULT_LAST_NAME);
        collaborator.setEmail(DEFAULT_EMAIL);
        collaborator.setApprentice(DEFAULT_APPRENTICE);
    }

    @Test
    @Transactional
    public void createCollaborator() throws Exception {
        int databaseSizeBeforeCreate = collaboratorRepository.findAll().size();

        // Create the Collaborator

        restCollaboratorMockMvc.perform(post("/api/collaborators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborator)))
                .andExpect(status().isCreated());

        // Validate the Collaborator in the database
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        assertThat(collaborators).hasSize(databaseSizeBeforeCreate + 1);
        Collaborator testCollaborator = collaborators.get(collaborators.size() - 1);
        assertThat(testCollaborator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollaborator.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCollaborator.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCollaborator.isApprentice()).isEqualTo(DEFAULT_APPRENTICE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaboratorRepository.findAll().size();
        // set the field null
        collaborator.setName(null);

        // Create the Collaborator, which fails.

        restCollaboratorMockMvc.perform(post("/api/collaborators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborator)))
                .andExpect(status().isBadRequest());

        List<Collaborator> collaborators = collaboratorRepository.findAll();
        assertThat(collaborators).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaboratorRepository.findAll().size();
        // set the field null
        collaborator.setEmail(null);

        // Create the Collaborator, which fails.

        restCollaboratorMockMvc.perform(post("/api/collaborators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborator)))
                .andExpect(status().isBadRequest());

        List<Collaborator> collaborators = collaboratorRepository.findAll();
        assertThat(collaborators).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApprenticeIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaboratorRepository.findAll().size();
        // set the field null
        collaborator.setApprentice(null);

        // Create the Collaborator, which fails.

        restCollaboratorMockMvc.perform(post("/api/collaborators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborator)))
                .andExpect(status().isBadRequest());

        List<Collaborator> collaborators = collaboratorRepository.findAll();
        assertThat(collaborators).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCollaborators() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaborators
        restCollaboratorMockMvc.perform(get("/api/collaborators?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(collaborator.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].apprentice").value(hasItem(DEFAULT_APPRENTICE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get the collaborator
        restCollaboratorMockMvc.perform(get("/api/collaborators/{id}", collaborator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(collaborator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.apprentice").value(DEFAULT_APPRENTICE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCollaborator() throws Exception {
        // Get the collaborator
        restCollaboratorMockMvc.perform(get("/api/collaborators/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();

        // Update the collaborator
        Collaborator updatedCollaborator = new Collaborator();
        updatedCollaborator.setId(collaborator.getId());
        updatedCollaborator.setName(UPDATED_NAME);
        updatedCollaborator.setLastName(UPDATED_LAST_NAME);
        updatedCollaborator.setEmail(UPDATED_EMAIL);
        updatedCollaborator.setApprentice(UPDATED_APPRENTICE);

        restCollaboratorMockMvc.perform(put("/api/collaborators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCollaborator)))
                .andExpect(status().isOk());

        // Validate the Collaborator in the database
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        assertThat(collaborators).hasSize(databaseSizeBeforeUpdate);
        Collaborator testCollaborator = collaborators.get(collaborators.size() - 1);
        assertThat(testCollaborator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollaborator.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCollaborator.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCollaborator.isApprentice()).isEqualTo(UPDATED_APPRENTICE);
    }

    @Test
    @Transactional
    public void deleteCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);
        int databaseSizeBeforeDelete = collaboratorRepository.findAll().size();

        // Get the collaborator
        restCollaboratorMockMvc.perform(delete("/api/collaborators/{id}", collaborator.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        assertThat(collaborators).hasSize(databaseSizeBeforeDelete - 1);
    }
}
