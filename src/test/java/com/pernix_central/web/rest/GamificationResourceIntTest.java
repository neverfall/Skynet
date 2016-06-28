package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Gamification;
import com.pernix_central.repository.GamificationRepository;

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
 * Test class for the GamificationResource REST controller.
 *
 * @see GamificationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class GamificationResourceIntTest {


    private static final Integer DEFAULT_YEARLY_SCORE = 1;
    private static final Integer UPDATED_YEARLY_SCORE = 2;

    private static final Integer DEFAULT_PERIOD_SCORE = 1;
    private static final Integer UPDATED_PERIOD_SCORE = 2;

    @Inject
    private GamificationRepository gamificationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGamificationMockMvc;

    private Gamification gamification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GamificationResource gamificationResource = new GamificationResource();
        ReflectionTestUtils.setField(gamificationResource, "gamificationRepository", gamificationRepository);
        this.restGamificationMockMvc = MockMvcBuilders.standaloneSetup(gamificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        gamification = new Gamification();
        gamification.setYearlyScore(DEFAULT_YEARLY_SCORE);
        gamification.setPeriodScore(DEFAULT_PERIOD_SCORE);
    }

    @Test
    @Transactional
    public void createGamification() throws Exception {
        int databaseSizeBeforeCreate = gamificationRepository.findAll().size();

        // Create the Gamification

        restGamificationMockMvc.perform(post("/api/gamifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gamification)))
                .andExpect(status().isCreated());

        // Validate the Gamification in the database
        List<Gamification> gamifications = gamificationRepository.findAll();
        assertThat(gamifications).hasSize(databaseSizeBeforeCreate + 1);
        Gamification testGamification = gamifications.get(gamifications.size() - 1);
        assertThat(testGamification.getYearlyScore()).isEqualTo(DEFAULT_YEARLY_SCORE);
        assertThat(testGamification.getPeriodScore()).isEqualTo(DEFAULT_PERIOD_SCORE);
    }

    @Test
    @Transactional
    public void getAllGamifications() throws Exception {
        // Initialize the database
        gamificationRepository.saveAndFlush(gamification);

        // Get all the gamifications
        restGamificationMockMvc.perform(get("/api/gamifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gamification.getId().intValue())))
                .andExpect(jsonPath("$.[*].yearlyScore").value(hasItem(DEFAULT_YEARLY_SCORE)))
                .andExpect(jsonPath("$.[*].periodScore").value(hasItem(DEFAULT_PERIOD_SCORE)));
    }

    @Test
    @Transactional
    public void getGamification() throws Exception {
        // Initialize the database
        gamificationRepository.saveAndFlush(gamification);

        // Get the gamification
        restGamificationMockMvc.perform(get("/api/gamifications/{id}", gamification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gamification.getId().intValue()))
            .andExpect(jsonPath("$.yearlyScore").value(DEFAULT_YEARLY_SCORE))
            .andExpect(jsonPath("$.periodScore").value(DEFAULT_PERIOD_SCORE));
    }

    @Test
    @Transactional
    public void getNonExistingGamification() throws Exception {
        // Get the gamification
        restGamificationMockMvc.perform(get("/api/gamifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGamification() throws Exception {
        // Initialize the database
        gamificationRepository.saveAndFlush(gamification);
        int databaseSizeBeforeUpdate = gamificationRepository.findAll().size();

        // Update the gamification
        Gamification updatedGamification = new Gamification();
        updatedGamification.setId(gamification.getId());
        updatedGamification.setYearlyScore(UPDATED_YEARLY_SCORE);
        updatedGamification.setPeriodScore(UPDATED_PERIOD_SCORE);

        restGamificationMockMvc.perform(put("/api/gamifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGamification)))
                .andExpect(status().isOk());

        // Validate the Gamification in the database
        List<Gamification> gamifications = gamificationRepository.findAll();
        assertThat(gamifications).hasSize(databaseSizeBeforeUpdate);
        Gamification testGamification = gamifications.get(gamifications.size() - 1);
        assertThat(testGamification.getYearlyScore()).isEqualTo(UPDATED_YEARLY_SCORE);
        assertThat(testGamification.getPeriodScore()).isEqualTo(UPDATED_PERIOD_SCORE);
    }

    @Test
    @Transactional
    public void deleteGamification() throws Exception {
        // Initialize the database
        gamificationRepository.saveAndFlush(gamification);
        int databaseSizeBeforeDelete = gamificationRepository.findAll().size();

        // Get the gamification
        restGamificationMockMvc.perform(delete("/api/gamifications/{id}", gamification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Gamification> gamifications = gamificationRepository.findAll();
        assertThat(gamifications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
