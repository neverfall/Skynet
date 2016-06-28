package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.CustomerSatisfactionNPS;
import com.pernix_central.repository.CustomerSatisfactionNPSRepository;

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
 * Test class for the CustomerSatisfactionNPSResource REST controller.
 *
 * @see CustomerSatisfactionNPSResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerSatisfactionNPSResourceIntTest {


    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final Integer DEFAULT_COMPLIANCE = 1;
    private static final Integer UPDATED_COMPLIANCE = 2;

    @Inject
    private CustomerSatisfactionNPSRepository customerSatisfactionNPSRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerSatisfactionNPSMockMvc;

    private CustomerSatisfactionNPS customerSatisfactionNPS;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerSatisfactionNPSResource customerSatisfactionNPSResource = new CustomerSatisfactionNPSResource();
        ReflectionTestUtils.setField(customerSatisfactionNPSResource, "customerSatisfactionNPSRepository", customerSatisfactionNPSRepository);
        this.restCustomerSatisfactionNPSMockMvc = MockMvcBuilders.standaloneSetup(customerSatisfactionNPSResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerSatisfactionNPS = new CustomerSatisfactionNPS();
        customerSatisfactionNPS.setValue(DEFAULT_VALUE);
        customerSatisfactionNPS.setCompliance(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void createCustomerSatisfactionNPS() throws Exception {
        int databaseSizeBeforeCreate = customerSatisfactionNPSRepository.findAll().size();

        // Create the CustomerSatisfactionNPS

        restCustomerSatisfactionNPSMockMvc.perform(post("/api/customer-satisfaction-nps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerSatisfactionNPS)))
                .andExpect(status().isCreated());

        // Validate the CustomerSatisfactionNPS in the database
        List<CustomerSatisfactionNPS> customerSatisfactionNPS = customerSatisfactionNPSRepository.findAll();
        assertThat(customerSatisfactionNPS).hasSize(databaseSizeBeforeCreate + 1);
        CustomerSatisfactionNPS testCustomerSatisfactionNPS = customerSatisfactionNPS.get(customerSatisfactionNPS.size() - 1);
        assertThat(testCustomerSatisfactionNPS.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCustomerSatisfactionNPS.getCompliance()).isEqualTo(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerSatisfactionNPS() throws Exception {
        // Initialize the database
        customerSatisfactionNPSRepository.saveAndFlush(customerSatisfactionNPS);

        // Get all the customerSatisfactionNPS
        restCustomerSatisfactionNPSMockMvc.perform(get("/api/customer-satisfaction-nps?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerSatisfactionNPS.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].compliance").value(hasItem(DEFAULT_COMPLIANCE)));
    }

    @Test
    @Transactional
    public void getCustomerSatisfactionNPS() throws Exception {
        // Initialize the database
        customerSatisfactionNPSRepository.saveAndFlush(customerSatisfactionNPS);

        // Get the customerSatisfactionNPS
        restCustomerSatisfactionNPSMockMvc.perform(get("/api/customer-satisfaction-nps/{id}", customerSatisfactionNPS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerSatisfactionNPS.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.compliance").value(DEFAULT_COMPLIANCE));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerSatisfactionNPS() throws Exception {
        // Get the customerSatisfactionNPS
        restCustomerSatisfactionNPSMockMvc.perform(get("/api/customer-satisfaction-nps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerSatisfactionNPS() throws Exception {
        // Initialize the database
        customerSatisfactionNPSRepository.saveAndFlush(customerSatisfactionNPS);
        int databaseSizeBeforeUpdate = customerSatisfactionNPSRepository.findAll().size();

        // Update the customerSatisfactionNPS
        CustomerSatisfactionNPS updatedCustomerSatisfactionNPS = new CustomerSatisfactionNPS();
        updatedCustomerSatisfactionNPS.setId(customerSatisfactionNPS.getId());
        updatedCustomerSatisfactionNPS.setValue(UPDATED_VALUE);
        updatedCustomerSatisfactionNPS.setCompliance(UPDATED_COMPLIANCE);

        restCustomerSatisfactionNPSMockMvc.perform(put("/api/customer-satisfaction-nps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCustomerSatisfactionNPS)))
                .andExpect(status().isOk());

        // Validate the CustomerSatisfactionNPS in the database
        List<CustomerSatisfactionNPS> customerSatisfactionNPS = customerSatisfactionNPSRepository.findAll();
        assertThat(customerSatisfactionNPS).hasSize(databaseSizeBeforeUpdate);
        CustomerSatisfactionNPS testCustomerSatisfactionNPS = customerSatisfactionNPS.get(customerSatisfactionNPS.size() - 1);
        assertThat(testCustomerSatisfactionNPS.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCustomerSatisfactionNPS.getCompliance()).isEqualTo(UPDATED_COMPLIANCE);
    }

    @Test
    @Transactional
    public void deleteCustomerSatisfactionNPS() throws Exception {
        // Initialize the database
        customerSatisfactionNPSRepository.saveAndFlush(customerSatisfactionNPS);
        int databaseSizeBeforeDelete = customerSatisfactionNPSRepository.findAll().size();

        // Get the customerSatisfactionNPS
        restCustomerSatisfactionNPSMockMvc.perform(delete("/api/customer-satisfaction-nps/{id}", customerSatisfactionNPS.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerSatisfactionNPS> customerSatisfactionNPS = customerSatisfactionNPSRepository.findAll();
        assertThat(customerSatisfactionNPS).hasSize(databaseSizeBeforeDelete - 1);
    }
}
