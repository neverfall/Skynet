package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Usuario;
import com.pernix_central.repository.UsuarioRepository;
import com.pernix_central.web.rest.util.HeaderUtil;
import com.pernix_central.web.rest.dto.UsuarioDTO;
import com.pernix_central.web.rest.mapper.UsuarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Usuario.
 */
@RestController
@RequestMapping("/api")
public class UsuarioResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioResource.class);
        
    @Inject
    private UsuarioRepository usuarioRepository;
    
    @Inject
    private UsuarioMapper usuarioMapper;
    
    /**
     * POST  /usuarios : Create a new usuario.
     *
     * @param usuarioDTO the usuarioDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new usuarioDTO, or with status 400 (Bad Request) if the usuario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/usuarios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) throws URISyntaxException {
        log.debug("REST request to save Usuario : {}", usuarioDTO);
        if (usuarioDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("usuario", "idexists", "A new usuario cannot already have an ID")).body(null);
        }
        Usuario usuario = usuarioMapper.usuarioDTOToUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        UsuarioDTO result = usuarioMapper.usuarioToUsuarioDTO(usuario);
        return ResponseEntity.created(new URI("/api/usuarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("usuario", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /usuarios : Updates an existing usuario.
     *
     * @param usuarioDTO the usuarioDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated usuarioDTO,
     * or with status 400 (Bad Request) if the usuarioDTO is not valid,
     * or with status 500 (Internal Server Error) if the usuarioDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/usuarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UsuarioDTO> updateUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) throws URISyntaxException {
        log.debug("REST request to update Usuario : {}", usuarioDTO);
        if (usuarioDTO.getId() == null) {
            return createUsuario(usuarioDTO);
        }
        Usuario usuario = usuarioMapper.usuarioDTOToUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        UsuarioDTO result = usuarioMapper.usuarioToUsuarioDTO(usuario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("usuario", usuarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /usuarios : get all the usuarios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of usuarios in body
     */
    @RequestMapping(value = "/usuarios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getAllUsuarios() {
        log.debug("REST request to get all Usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.usuariosToUsuarioDTOs(usuarios);
    }

    /**
     * GET  /usuarios/:id : get the "id" usuario.
     *
     * @param id the id of the usuarioDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usuarioDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/usuarios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Long id) {
        log.debug("REST request to get Usuario : {}", id);
        Usuario usuario = usuarioRepository.findOne(id);
        UsuarioDTO usuarioDTO = usuarioMapper.usuarioToUsuarioDTO(usuario);
        return Optional.ofNullable(usuarioDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /usuarios/:id : delete the "id" usuario.
     *
     * @param id the id of the usuarioDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/usuarios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        log.debug("REST request to delete Usuario : {}", id);
        usuarioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("usuario", id.toString())).build();
    }

}
