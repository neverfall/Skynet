package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Participacion;
import com.pernix_central.repository.ParticipacionRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ParticipacionResource REST controller.
 *
 * @see ParticipacionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class ParticipacionResourceIntTest {


    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ParticipacionRepository participacionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restParticipacionMockMvc;

    private Participacion participacion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParticipacionResource participacionResource = new ParticipacionResource();
        ReflectionTestUtils.setField(participacionResource, "participacionRepository", participacionRepository);
        this.restParticipacionMockMvc = MockMvcBuilders.standaloneSetup(participacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        participacion = new Participacion();
        participacion.setFecha(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void createParticipacion() throws Exception {
        int databaseSizeBeforeCreate = participacionRepository.findAll().size();

        // Create the Participacion

        restParticipacionMockMvc.perform(post("/api/participacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(participacion)))
                .andExpect(status().isCreated());

        // Validate the Participacion in the database
        List<Participacion> participacions = participacionRepository.findAll();
        assertThat(participacions).hasSize(databaseSizeBeforeCreate + 1);
        Participacion testParticipacion = participacions.get(participacions.size() - 1);
        assertThat(testParticipacion.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    public void getAllParticipacions() throws Exception {
        // Initialize the database
        participacionRepository.saveAndFlush(participacion);

        // Get all the participacions
        restParticipacionMockMvc.perform(get("/api/participacions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(participacion.getId().intValue())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    public void getParticipacion() throws Exception {
        // Initialize the database
        participacionRepository.saveAndFlush(participacion);

        // Get the participacion
        restParticipacionMockMvc.perform(get("/api/participacions/{id}", participacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(participacion.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParticipacion() throws Exception {
        // Get the participacion
        restParticipacionMockMvc.perform(get("/api/participacions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipacion() throws Exception {
        // Initialize the database
        participacionRepository.saveAndFlush(participacion);
        int databaseSizeBeforeUpdate = participacionRepository.findAll().size();

        // Update the participacion
        Participacion updatedParticipacion = new Participacion();
        updatedParticipacion.setId(participacion.getId());
        updatedParticipacion.setFecha(UPDATED_FECHA);

        restParticipacionMockMvc.perform(put("/api/participacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedParticipacion)))
                .andExpect(status().isOk());

        // Validate the Participacion in the database
        List<Participacion> participacions = participacionRepository.findAll();
        assertThat(participacions).hasSize(databaseSizeBeforeUpdate);
        Participacion testParticipacion = participacions.get(participacions.size() - 1);
        assertThat(testParticipacion.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    public void deleteParticipacion() throws Exception {
        // Initialize the database
        participacionRepository.saveAndFlush(participacion);
        int databaseSizeBeforeDelete = participacionRepository.findAll().size();

        // Get the participacion
        restParticipacionMockMvc.perform(delete("/api/participacions/{id}", participacion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Participacion> participacions = participacionRepository.findAll();
        assertThat(participacions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
