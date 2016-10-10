package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Empleado;
import com.pernix_central.repository.EmpleadoRepository;

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
 * Test class for the EmpleadoResource REST controller.
 *
 * @see EmpleadoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmpleadoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_ROL = "AAAAA";
    private static final String UPDATED_ROL = "BBBBB";

    @Inject
    private EmpleadoRepository empleadoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmpleadoMockMvc;

    private Empleado empleado;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmpleadoResource empleadoResource = new EmpleadoResource();
        ReflectionTestUtils.setField(empleadoResource, "empleadoRepository", empleadoRepository);
        this.restEmpleadoMockMvc = MockMvcBuilders.standaloneSetup(empleadoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        empleado = new Empleado();
        empleado.setNombre(DEFAULT_NOMBRE);
        empleado.setEmail(DEFAULT_EMAIL);
        empleado.setRol(DEFAULT_ROL);
    }

    @Test
    @Transactional
    public void createEmpleado() throws Exception {
        int databaseSizeBeforeCreate = empleadoRepository.findAll().size();

        // Create the Empleado

        restEmpleadoMockMvc.perform(post("/api/empleados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(empleado)))
                .andExpect(status().isCreated());

        // Validate the Empleado in the database
        List<Empleado> empleados = empleadoRepository.findAll();
        assertThat(empleados).hasSize(databaseSizeBeforeCreate + 1);
        Empleado testEmpleado = empleados.get(empleados.size() - 1);
        assertThat(testEmpleado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEmpleado.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmpleado.getRol()).isEqualTo(DEFAULT_ROL);
    }

    @Test
    @Transactional
    public void getAllEmpleados() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get all the empleados
        restEmpleadoMockMvc.perform(get("/api/empleados?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].rol").value(hasItem(DEFAULT_ROL.toString())));
    }

    @Test
    @Transactional
    public void getEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get the empleado
        restEmpleadoMockMvc.perform(get("/api/empleados/{id}", empleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.rol").value(DEFAULT_ROL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmpleado() throws Exception {
        // Get the empleado
        restEmpleadoMockMvc.perform(get("/api/empleados/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado
        Empleado updatedEmpleado = new Empleado();
        updatedEmpleado.setId(empleado.getId());
        updatedEmpleado.setNombre(UPDATED_NOMBRE);
        updatedEmpleado.setEmail(UPDATED_EMAIL);
        updatedEmpleado.setRol(UPDATED_ROL);

        restEmpleadoMockMvc.perform(put("/api/empleados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmpleado)))
                .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleados = empleadoRepository.findAll();
        assertThat(empleados).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleados.get(empleados.size() - 1);
        assertThat(testEmpleado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpleado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmpleado.getRol()).isEqualTo(UPDATED_ROL);
    }

    @Test
    @Transactional
    public void deleteEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);
        int databaseSizeBeforeDelete = empleadoRepository.findAll().size();

        // Get the empleado
        restEmpleadoMockMvc.perform(delete("/api/empleados/{id}", empleado.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Empleado> empleados = empleadoRepository.findAll();
        assertThat(empleados).hasSize(databaseSizeBeforeDelete - 1);
    }
}
