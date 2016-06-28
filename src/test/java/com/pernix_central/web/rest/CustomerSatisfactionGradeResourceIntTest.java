package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.CustomerSatisfactionGrade;
import com.pernix_central.repository.CustomerSatisfactionGradeRepository;

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
 * Test class for the CustomerSatisfactionGradeResource REST controller.
 *
 * @see CustomerSatisfactionGradeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerSatisfactionGradeResourceIntTest {


    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final Integer DEFAULT_COMPLIANCE = 1;
    private static final Integer UPDATED_COMPLIANCE = 2;

    @Inject
    private CustomerSatisfactionGradeRepository customerSatisfactionGradeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerSatisfactionGradeMockMvc;

    private CustomerSatisfactionGrade customerSatisfactionGrade;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerSatisfactionGradeResource customerSatisfactionGradeResource = new CustomerSatisfactionGradeResource();
        ReflectionTestUtils.setField(customerSatisfactionGradeResource, "customerSatisfactionGradeRepository", customerSatisfactionGradeRepository);
        this.restCustomerSatisfactionGradeMockMvc = MockMvcBuilders.standaloneSetup(customerSatisfactionGradeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerSatisfactionGrade = new CustomerSatisfactionGrade();
        customerSatisfactionGrade.setValue(DEFAULT_VALUE);
        customerSatisfactionGrade.setCompliance(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void createCustomerSatisfactionGrade() throws Exception {
        int databaseSizeBeforeCreate = customerSatisfactionGradeRepository.findAll().size();

        // Create the CustomerSatisfactionGrade

        restCustomerSatisfactionGradeMockMvc.perform(post("/api/customer-satisfaction-grades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerSatisfactionGrade)))
                .andExpect(status().isCreated());

        // Validate the CustomerSatisfactionGrade in the database
        List<CustomerSatisfactionGrade> customerSatisfactionGrades = customerSatisfactionGradeRepository.findAll();
        assertThat(customerSatisfactionGrades).hasSize(databaseSizeBeforeCreate + 1);
        CustomerSatisfactionGrade testCustomerSatisfactionGrade = customerSatisfactionGrades.get(customerSatisfactionGrades.size() - 1);
        assertThat(testCustomerSatisfactionGrade.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCustomerSatisfactionGrade.getCompliance()).isEqualTo(DEFAULT_COMPLIANCE);
    }

    @Test
    @Transactional
    public void getAllCustomerSatisfactionGrades() throws Exception {
        // Initialize the database
        customerSatisfactionGradeRepository.saveAndFlush(customerSatisfactionGrade);

        // Get all the customerSatisfactionGrades
        restCustomerSatisfactionGradeMockMvc.perform(get("/api/customer-satisfaction-grades?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerSatisfactionGrade.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].compliance").value(hasItem(DEFAULT_COMPLIANCE)));
    }

    @Test
    @Transactional
    public void getCustomerSatisfactionGrade() throws Exception {
        // Initialize the database
        customerSatisfactionGradeRepository.saveAndFlush(customerSatisfactionGrade);

        // Get the customerSatisfactionGrade
        restCustomerSatisfactionGradeMockMvc.perform(get("/api/customer-satisfaction-grades/{id}", customerSatisfactionGrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerSatisfactionGrade.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.compliance").value(DEFAULT_COMPLIANCE));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerSatisfactionGrade() throws Exception {
        // Get the customerSatisfactionGrade
        restCustomerSatisfactionGradeMockMvc.perform(get("/api/customer-satisfaction-grades/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerSatisfactionGrade() throws Exception {
        // Initialize the database
        customerSatisfactionGradeRepository.saveAndFlush(customerSatisfactionGrade);
        int databaseSizeBeforeUpdate = customerSatisfactionGradeRepository.findAll().size();

        // Update the customerSatisfactionGrade
        CustomerSatisfactionGrade updatedCustomerSatisfactionGrade = new CustomerSatisfactionGrade();
        updatedCustomerSatisfactionGrade.setId(customerSatisfactionGrade.getId());
        updatedCustomerSatisfactionGrade.setValue(UPDATED_VALUE);
        updatedCustomerSatisfactionGrade.setCompliance(UPDATED_COMPLIANCE);

        restCustomerSatisfactionGradeMockMvc.perform(put("/api/customer-satisfaction-grades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCustomerSatisfactionGrade)))
                .andExpect(status().isOk());

        // Validate the CustomerSatisfactionGrade in the database
        List<CustomerSatisfactionGrade> customerSatisfactionGrades = customerSatisfactionGradeRepository.findAll();
        assertThat(customerSatisfactionGrades).hasSize(databaseSizeBeforeUpdate);
        CustomerSatisfactionGrade testCustomerSatisfactionGrade = customerSatisfactionGrades.get(customerSatisfactionGrades.size() - 1);
        assertThat(testCustomerSatisfactionGrade.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCustomerSatisfactionGrade.getCompliance()).isEqualTo(UPDATED_COMPLIANCE);
    }

    @Test
    @Transactional
    public void deleteCustomerSatisfactionGrade() throws Exception {
        // Initialize the database
        customerSatisfactionGradeRepository.saveAndFlush(customerSatisfactionGrade);
        int databaseSizeBeforeDelete = customerSatisfactionGradeRepository.findAll().size();

        // Get the customerSatisfactionGrade
        restCustomerSatisfactionGradeMockMvc.perform(delete("/api/customer-satisfaction-grades/{id}", customerSatisfactionGrade.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerSatisfactionGrade> customerSatisfactionGrades = customerSatisfactionGradeRepository.findAll();
        assertThat(customerSatisfactionGrades).hasSize(databaseSizeBeforeDelete - 1);
    }
}
