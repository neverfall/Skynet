package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Empleado;
import com.pernix_central.repository.EmpleadoRepository;
import com.pernix_central.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Empleado.
 */
@RestController
@RequestMapping("/api")
public class EmpleadoResource {

    private final Logger log = LoggerFactory.getLogger(EmpleadoResource.class);
        
    @Inject
    private EmpleadoRepository empleadoRepository;
    
    /**
     * POST  /empleados : Create a new empleado.
     *
     * @param empleado the empleado to create
     * @return the ResponseEntity with status 201 (Created) and with body the new empleado, or with status 400 (Bad Request) if the empleado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/empleados",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Empleado> createEmpleado(@RequestBody Empleado empleado) throws URISyntaxException {
        log.debug("REST request to save Empleado : {}", empleado);
        if (empleado.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("empleado", "idexists", "A new empleado cannot already have an ID")).body(null);
        }
        Empleado result = empleadoRepository.save(empleado);
        return ResponseEntity.created(new URI("/api/empleados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("empleado", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /empleados : Updates an existing empleado.
     *
     * @param empleado the empleado to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated empleado,
     * or with status 400 (Bad Request) if the empleado is not valid,
     * or with status 500 (Internal Server Error) if the empleado couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/empleados",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Empleado> updateEmpleado(@RequestBody Empleado empleado) throws URISyntaxException {
        log.debug("REST request to update Empleado : {}", empleado);
        if (empleado.getId() == null) {
            return createEmpleado(empleado);
        }
        Empleado result = empleadoRepository.save(empleado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("empleado", empleado.getId().toString()))
            .body(result);
    }

    /**
     * GET  /empleados : get all the empleados.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of empleados in body
     */
    @RequestMapping(value = "/empleados",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Empleado> getAllEmpleados() {
        log.debug("REST request to get all Empleados");
        List<Empleado> empleados = empleadoRepository.findAll();
        return empleados;
    }

    /**
     * GET  /empleados/:id : get the "id" empleado.
     *
     * @param id the id of the empleado to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the empleado, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/empleados/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Empleado> getEmpleado(@PathVariable Long id) {
        log.debug("REST request to get Empleado : {}", id);
        Empleado empleado = empleadoRepository.findOne(id);
        return Optional.ofNullable(empleado)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /empleados/:id : delete the "id" empleado.
     *
     * @param id the id of the empleado to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/empleados/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        log.debug("REST request to delete Empleado : {}", id);
        empleadoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("empleado", id.toString())).build();
    }

}
