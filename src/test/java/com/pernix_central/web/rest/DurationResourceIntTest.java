package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Duration;
import com.pernix_central.repository.DurationRepository;

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
 * Test class for the DurationResource REST controller.
 *
 * @see DurationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class DurationResourceIntTest {


    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final Integer DEFAULT_COMPLIANCE = 1;
    private static final Integer UPDATED_COMPLIANCE = 2;

    @Inject
    private DurationRepository durationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDurationMockMvc;

    private Duration duration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DurationResource durationResource = new DurationResource();
        ReflectionTestUtils.setField(durationResource, "durationRepository", durationRepository);
        this.restDurationMockMvc = MockMvcBuilders.standaloneSetup(durationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        duration = new Duration();
        duration.setValue(DEFAULT_VALUE);
        duration.setCompliance(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void createDuration() throws Exception {
        int databaseSizeBeforeCreate = durationRepository.findAll().size();

        // Create the Duration

        restDurationMockMvc.perform(post("/api/durations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(duration)))
                .andExpect(status().isCreated());

        // Validate the Duration in the database
        List<Duration> durations = durationRepository.findAll();
        assertThat(durations).hasSize(databaseSizeBeforeCreate + 1);
        Duration testDuration = durations.get(durations.size() - 1);
        assertThat(testDuration.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testDuration.getCompliance()).isEqualTo(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void getAllDurations() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        // Get all the durations
        restDurationMockMvc.perform(get("/api/durations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(duration.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].compliance").value(hasItem(DEFAULT_COMPLIANCE)));
    }

    @Test
    @Transactional
    public void getDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        // Get the duration
        restDurationMockMvc.perform(get("/api/durations/{id}", duration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(duration.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.compliance").value(DEFAULT_COMPLIANCE));
    }

    @Test
    @Transactional
    public void getNonExistingDuration() throws Exception {
        // Get the duration
        restDurationMockMvc.perform(get("/api/durations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();

        // Update the duration
        Duration updatedDuration = new Duration();
        updatedDuration.setId(duration.getId());
        updatedDuration.setValue(UPDATED_VALUE);
        updatedDuration.setCompliance(UPDATED_COMPLIANCE);

        restDurationMockMvc.perform(put("/api/durations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDuration)))
                .andExpect(status().isOk());

        // Validate the Duration in the database
        List<Duration> durations = durationRepository.findAll();
        assertThat(durations).hasSize(databaseSizeBeforeUpdate);
        Duration testDuration = durations.get(durations.size() - 1);
        assertThat(testDuration.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDuration.getCompliance()).isEqualTo(UPDATED_COMPLIANCE);
    }

    @Test
    @Transactional
    public void deleteDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);
        int databaseSizeBeforeDelete = durationRepository.findAll().size();

        // Get the duration
        restDurationMockMvc.perform(delete("/api/durations/{id}", duration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Duration> durations = durationRepository.findAll();
        assertThat(durations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
