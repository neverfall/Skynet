package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Productivity;
import com.pernix_central.repository.ProductivityRepository;

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
 * Test class for the ProductivityResource REST controller.
 *
 * @see ProductivityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProductivityResourceIntTest {


    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final Integer DEFAULT_COMPLIANCE = 1;
    private static final Integer UPDATED_COMPLIANCE = 2;

    @Inject
    private ProductivityRepository productivityRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductivityMockMvc;

    private Productivity productivity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductivityResource productivityResource = new ProductivityResource();
        ReflectionTestUtils.setField(productivityResource, "productivityRepository", productivityRepository);
        this.restProductivityMockMvc = MockMvcBuilders.standaloneSetup(productivityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productivity = new Productivity();
        productivity.setValue(DEFAULT_VALUE);
        productivity.setCompliance(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void createProductivity() throws Exception {
        int databaseSizeBeforeCreate = productivityRepository.findAll().size();

        // Create the Productivity

        restProductivityMockMvc.perform(post("/api/productivities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productivity)))
                .andExpect(status().isCreated());

        // Validate the Productivity in the database
        List<Productivity> productivities = productivityRepository.findAll();
        assertThat(productivities).hasSize(databaseSizeBeforeCreate + 1);
        Productivity testProductivity = productivities.get(productivities.size() - 1);
        assertThat(testProductivity.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testProductivity.getCompliance()).isEqualTo(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void getAllProductivities() throws Exception {
        // Initialize the database
        productivityRepository.saveAndFlush(productivity);

        // Get all the productivities
        restProductivityMockMvc.perform(get("/api/productivities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productivity.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].compliance").value(hasItem(DEFAULT_COMPLIANCE)));
    }

    @Test
    @Transactional
    public void getProductivity() throws Exception {
        // Initialize the database
        productivityRepository.saveAndFlush(productivity);

        // Get the productivity
        restProductivityMockMvc.perform(get("/api/productivities/{id}", productivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productivity.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.compliance").value(DEFAULT_COMPLIANCE));
    }

    @Test
    @Transactional
    public void getNonExistingProductivity() throws Exception {
        // Get the productivity
        restProductivityMockMvc.perform(get("/api/productivities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductivity() throws Exception {
        // Initialize the database
        productivityRepository.saveAndFlush(productivity);
        int databaseSizeBeforeUpdate = productivityRepository.findAll().size();

        // Update the productivity
        Productivity updatedProductivity = new Productivity();
        updatedProductivity.setId(productivity.getId());
        updatedProductivity.setValue(UPDATED_VALUE);
        updatedProductivity.setCompliance(UPDATED_COMPLIANCE);

        restProductivityMockMvc.perform(put("/api/productivities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProductivity)))
                .andExpect(status().isOk());

        // Validate the Productivity in the database
        List<Productivity> productivities = productivityRepository.findAll();
        assertThat(productivities).hasSize(databaseSizeBeforeUpdate);
        Productivity testProductivity = productivities.get(productivities.size() - 1);
        assertThat(testProductivity.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testProductivity.getCompliance()).isEqualTo(UPDATED_COMPLIANCE);
    }

    @Test
    @Transactional
    public void deleteProductivity() throws Exception {
        // Initialize the database
        productivityRepository.saveAndFlush(productivity);
        int databaseSizeBeforeDelete = productivityRepository.findAll().size();

        // Get the productivity
        restProductivityMockMvc.perform(delete("/api/productivities/{id}", productivity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Productivity> productivities = productivityRepository.findAll();
        assertThat(productivities).hasSize(databaseSizeBeforeDelete - 1);
    }
}
