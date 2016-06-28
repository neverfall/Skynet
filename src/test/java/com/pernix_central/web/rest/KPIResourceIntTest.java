package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.KPI;
import com.pernix_central.repository.KPIRepository;

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
 * Test class for the KPIResource REST controller.
 *
 * @see KPIResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class KPIResourceIntTest {


    @Inject
    private KPIRepository kPIRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restKPIMockMvc;

    private KPI kPI;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KPIResource kPIResource = new KPIResource();
        ReflectionTestUtils.setField(kPIResource, "kPIRepository", kPIRepository);
        this.restKPIMockMvc = MockMvcBuilders.standaloneSetup(kPIResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        kPI = new KPI();
    }

    @Test
    @Transactional
    public void createKPI() throws Exception {
        int databaseSizeBeforeCreate = kPIRepository.findAll().size();

        // Create the KPI

        restKPIMockMvc.perform(post("/api/k-pis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(kPI)))
                .andExpect(status().isCreated());

        // Validate the KPI in the database
        List<KPI> kPIS = kPIRepository.findAll();
        assertThat(kPIS).hasSize(databaseSizeBeforeCreate + 1);
        KPI testKPI = kPIS.get(kPIS.size() - 1);
    }

    @Test
    @Transactional
    public void getAllKPIS() throws Exception {
        // Initialize the database
        kPIRepository.saveAndFlush(kPI);

        // Get all the kPIS
        restKPIMockMvc.perform(get("/api/k-pis?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(kPI.getId().intValue())));
    }

    @Test
    @Transactional
    public void getKPI() throws Exception {
        // Initialize the database
        kPIRepository.saveAndFlush(kPI);

        // Get the kPI
        restKPIMockMvc.perform(get("/api/k-pis/{id}", kPI.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(kPI.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingKPI() throws Exception {
        // Get the kPI
        restKPIMockMvc.perform(get("/api/k-pis/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKPI() throws Exception {
        // Initialize the database
        kPIRepository.saveAndFlush(kPI);
        int databaseSizeBeforeUpdate = kPIRepository.findAll().size();

        // Update the kPI
        KPI updatedKPI = new KPI();
        updatedKPI.setId(kPI.getId());

        restKPIMockMvc.perform(put("/api/k-pis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedKPI)))
                .andExpect(status().isOk());

        // Validate the KPI in the database
        List<KPI> kPIS = kPIRepository.findAll();
        assertThat(kPIS).hasSize(databaseSizeBeforeUpdate);
        KPI testKPI = kPIS.get(kPIS.size() - 1);
    }

    @Test
    @Transactional
    public void deleteKPI() throws Exception {
        // Initialize the database
        kPIRepository.saveAndFlush(kPI);
        int databaseSizeBeforeDelete = kPIRepository.findAll().size();

        // Get the kPI
        restKPIMockMvc.perform(delete("/api/k-pis/{id}", kPI.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<KPI> kPIS = kPIRepository.findAll();
        assertThat(kPIS).hasSize(databaseSizeBeforeDelete - 1);
    }
}
