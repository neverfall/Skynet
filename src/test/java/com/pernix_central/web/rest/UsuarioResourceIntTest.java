package com.pernix_central.web.rest;

import com.pernix_central.SkynetApp;
import com.pernix_central.domain.Usuario;
import com.pernix_central.repository.UsuarioRepository;
import com.pernix_central.web.rest.dto.UsuarioDTO;
import com.pernix_central.web.rest.mapper.UsuarioMapper;

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
 * Test class for the UsuarioResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkynetApp.class)
@WebAppConfiguration
@IntegrationTest
public class UsuarioResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private UsuarioMapper usuarioMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UsuarioResource usuarioResource = new UsuarioResource();
        ReflectionTestUtils.setField(usuarioResource, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(usuarioResource, "usuarioMapper", usuarioMapper);
        this.restUsuarioMockMvc = MockMvcBuilders.standaloneSetup(usuarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        usuario = new Usuario();
        usuario.setName(DEFAULT_NAME);
        usuario.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.usuarioToUsuarioDTO(usuario);

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
                .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarios.get(usuarios.size() - 1);
        assertThat(testUsuario.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setEmail(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.usuarioToUsuarioDTO(usuario);

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
                .andExpect(status().isBadRequest());

        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarios
        restUsuarioMockMvc.perform(get("/api/usuarios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = new Usuario();
        updatedUsuario.setId(usuario.getId());
        updatedUsuario.setName(UPDATED_NAME);
        updatedUsuario.setEmail(UPDATED_EMAIL);
        UsuarioDTO usuarioDTO = usuarioMapper.usuarioToUsuarioDTO(updatedUsuario);

        restUsuarioMockMvc.perform(put("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
                .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarios.get(usuarios.size() - 1);
        assertThat(testUsuario.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Get the usuario
        restUsuarioMockMvc.perform(delete("/api/usuarios/{id}", usuario.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
