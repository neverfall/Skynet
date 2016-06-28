package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.FollowUp;
import com.pernix_central.repository.FollowUpRepository;

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
 * Test class for the FollowUpResource REST controller.
 *
 * @see FollowUpResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class FollowUpResourceIntTest {


    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final Integer DEFAULT_COMPLIANCE = 1;
    private static final Integer UPDATED_COMPLIANCE = 2;

    @Inject
    private FollowUpRepository followUpRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFollowUpMockMvc;

    private FollowUp followUp;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FollowUpResource followUpResource = new FollowUpResource();
        ReflectionTestUtils.setField(followUpResource, "followUpRepository", followUpRepository);
        this.restFollowUpMockMvc = MockMvcBuilders.standaloneSetup(followUpResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        followUp = new FollowUp();
        followUp.setValue(DEFAULT_VALUE);
        followUp.setCompliance(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void createFollowUp() throws Exception {
        int databaseSizeBeforeCreate = followUpRepository.findAll().size();

        // Create the FollowUp

        restFollowUpMockMvc.perform(post("/api/follow-ups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(followUp)))
                .andExpect(status().isCreated());

        // Validate the FollowUp in the database
        List<FollowUp> followUps = followUpRepository.findAll();
        assertThat(followUps).hasSize(databaseSizeBeforeCreate + 1);
        FollowUp testFollowUp = followUps.get(followUps.size() - 1);
        assertThat(testFollowUp.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testFollowUp.getCompliance()).isEqualTo(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void getAllFollowUps() throws Exception {
        // Initialize the database
        followUpRepository.saveAndFlush(followUp);

        // Get all the followUps
        restFollowUpMockMvc.perform(get("/api/follow-ups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(followUp.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].compliance").value(hasItem(DEFAULT_COMPLIANCE)));
    }

    @Test
    @Transactional
    public void getFollowUp() throws Exception {
        // Initialize the database
        followUpRepository.saveAndFlush(followUp);

        // Get the followUp
        restFollowUpMockMvc.perform(get("/api/follow-ups/{id}", followUp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(followUp.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.compliance").value(DEFAULT_COMPLIANCE));
    }

    @Test
    @Transactional
    public void getNonExistingFollowUp() throws Exception {
        // Get the followUp
        restFollowUpMockMvc.perform(get("/api/follow-ups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFollowUp() throws Exception {
        // Initialize the database
        followUpRepository.saveAndFlush(followUp);
        int databaseSizeBeforeUpdate = followUpRepository.findAll().size();

        // Update the followUp
        FollowUp updatedFollowUp = new FollowUp();
        updatedFollowUp.setId(followUp.getId());
        updatedFollowUp.setValue(UPDATED_VALUE);
        updatedFollowUp.setCompliance(UPDATED_COMPLIANCE);

        restFollowUpMockMvc.perform(put("/api/follow-ups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFollowUp)))
                .andExpect(status().isOk());

        // Validate the FollowUp in the database
        List<FollowUp> followUps = followUpRepository.findAll();
        assertThat(followUps).hasSize(databaseSizeBeforeUpdate);
        FollowUp testFollowUp = followUps.get(followUps.size() - 1);
        assertThat(testFollowUp.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testFollowUp.getCompliance()).isEqualTo(UPDATED_COMPLIANCE);
    }

    @Test
    @Transactional
    public void deleteFollowUp() throws Exception {
        // Initialize the database
        followUpRepository.saveAndFlush(followUp);
        int databaseSizeBeforeDelete = followUpRepository.findAll().size();

        // Get the followUp
        restFollowUpMockMvc.perform(delete("/api/follow-ups/{id}", followUp.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FollowUp> followUps = followUpRepository.findAll();
        assertThat(followUps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
