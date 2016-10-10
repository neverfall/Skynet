package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Actividad;
import com.pernix_central.repository.ActividadRepository;

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
 * Test class for the ActividadResource REST controller.
 *
 * @see ActividadResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class ActividadResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final Integer DEFAULT_PUNTOS = 1;
    private static final Integer UPDATED_PUNTOS = 2;
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private ActividadRepository actividadRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActividadMockMvc;

    private Actividad actividad;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActividadResource actividadResource = new ActividadResource();
        ReflectionTestUtils.setField(actividadResource, "actividadRepository", actividadRepository);
        this.restActividadMockMvc = MockMvcBuilders.standaloneSetup(actividadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        actividad = new Actividad();
        actividad.setNombre(DEFAULT_NOMBRE);
        actividad.setPuntos(DEFAULT_PUNTOS);
        actividad.setDescripcion(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createActividad() throws Exception {
        int databaseSizeBeforeCreate = actividadRepository.findAll().size();

        // Create the Actividad

        restActividadMockMvc.perform(post("/api/actividads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actividad)))
                .andExpect(status().isCreated());

        // Validate the Actividad in the database
        List<Actividad> actividads = actividadRepository.findAll();
        assertThat(actividads).hasSize(databaseSizeBeforeCreate + 1);
        Actividad testActividad = actividads.get(actividads.size() - 1);
        assertThat(testActividad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testActividad.getPuntos()).isEqualTo(DEFAULT_PUNTOS);
        assertThat(testActividad.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllActividads() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        // Get all the actividads
        restActividadMockMvc.perform(get("/api/actividads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(actividad.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].puntos").value(hasItem(DEFAULT_PUNTOS)))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);

        // Get the actividad
        restActividadMockMvc.perform(get("/api/actividads/{id}", actividad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(actividad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.puntos").value(DEFAULT_PUNTOS))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActividad() throws Exception {
        // Get the actividad
        restActividadMockMvc.perform(get("/api/actividads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);
        int databaseSizeBeforeUpdate = actividadRepository.findAll().size();

        // Update the actividad
        Actividad updatedActividad = new Actividad();
        updatedActividad.setId(actividad.getId());
        updatedActividad.setNombre(UPDATED_NOMBRE);
        updatedActividad.setPuntos(UPDATED_PUNTOS);
        updatedActividad.setDescripcion(UPDATED_DESCRIPCION);

        restActividadMockMvc.perform(put("/api/actividads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActividad)))
                .andExpect(status().isOk());

        // Validate the Actividad in the database
        List<Actividad> actividads = actividadRepository.findAll();
        assertThat(actividads).hasSize(databaseSizeBeforeUpdate);
        Actividad testActividad = actividads.get(actividads.size() - 1);
        assertThat(testActividad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testActividad.getPuntos()).isEqualTo(UPDATED_PUNTOS);
        assertThat(testActividad.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteActividad() throws Exception {
        // Initialize the database
        actividadRepository.saveAndFlush(actividad);
        int databaseSizeBeforeDelete = actividadRepository.findAll().size();

        // Get the actividad
        restActividadMockMvc.perform(delete("/api/actividads/{id}", actividad.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Actividad> actividads = actividadRepository.findAll();
        assertThat(actividads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
