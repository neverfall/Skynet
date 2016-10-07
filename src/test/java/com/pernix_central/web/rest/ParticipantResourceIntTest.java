package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Participant;
import com.pernix_central.repository.ParticipantRepository;

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
 * Test class for the ParticipantResource REST controller.
 *
 * @see ParticipantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class ParticipantResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_ROLE = "AAAAA";
    private static final String UPDATED_ROLE = "BBBBB";

    @Inject
    private ParticipantRepository participantRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restParticipantMockMvc;

    private Participant participant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParticipantResource participantResource = new ParticipantResource();
        ReflectionTestUtils.setField(participantResource, "participantRepository", participantRepository);
        this.restParticipantMockMvc = MockMvcBuilders.standaloneSetup(participantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        participant = new Participant();
        participant.setName(DEFAULT_NAME);
        participant.setEmail(DEFAULT_EMAIL);
        participant.setRole(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createParticipant() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant

        restParticipantMockMvc.perform(post("/api/participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(participant)))
                .andExpect(status().isCreated());

        // Validate the Participant in the database
        List<Participant> participants = participantRepository.findAll();
        assertThat(participants).hasSize(databaseSizeBeforeCreate + 1);
        Participant testParticipant = participants.get(participants.size() - 1);
        assertThat(testParticipant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParticipant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParticipant.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void getAllParticipants() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participants
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void getParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", participant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(participant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParticipant() throws Exception {
        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);
        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Update the participant
        Participant updatedParticipant = new Participant();
        updatedParticipant.setId(participant.getId());
        updatedParticipant.setName(UPDATED_NAME);
        updatedParticipant.setEmail(UPDATED_EMAIL);
        updatedParticipant.setRole(UPDATED_ROLE);

        restParticipantMockMvc.perform(put("/api/participants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedParticipant)))
                .andExpect(status().isOk());

        // Validate the Participant in the database
        List<Participant> participants = participantRepository.findAll();
        assertThat(participants).hasSize(databaseSizeBeforeUpdate);
        Participant testParticipant = participants.get(participants.size() - 1);
        assertThat(testParticipant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParticipant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParticipant.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void deleteParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);
        int databaseSizeBeforeDelete = participantRepository.findAll().size();

        // Get the participant
        restParticipantMockMvc.perform(delete("/api/participants/{id}", participant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Participant> participants = participantRepository.findAll();
        assertThat(participants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
