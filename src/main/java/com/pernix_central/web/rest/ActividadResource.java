package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Actividad;
import com.pernix_central.repository.ActividadRepository;
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
 * REST controller for managing Actividad.
 */
@RestController
@RequestMapping("/api")
public class ActividadResource {

    private final Logger log = LoggerFactory.getLogger(ActividadResource.class);
        
    @Inject
    private ActividadRepository actividadRepository;
    
    /**
     * POST  /actividads : Create a new actividad.
     *
     * @param actividad the actividad to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actividad, or with status 400 (Bad Request) if the actividad has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/actividads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actividad> createActividad(@RequestBody Actividad actividad) throws URISyntaxException {
        log.debug("REST request to save Actividad : {}", actividad);
        if (actividad.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("actividad", "idexists", "A new actividad cannot already have an ID")).body(null);
        }
        Actividad result = actividadRepository.save(actividad);
        return ResponseEntity.created(new URI("/api/actividads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("actividad", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actividads : Updates an existing actividad.
     *
     * @param actividad the actividad to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actividad,
     * or with status 400 (Bad Request) if the actividad is not valid,
     * or with status 500 (Internal Server Error) if the actividad couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/actividads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actividad> updateActividad(@RequestBody Actividad actividad) throws URISyntaxException {
        log.debug("REST request to update Actividad : {}", actividad);
        if (actividad.getId() == null) {
            return createActividad(actividad);
        }
        Actividad result = actividadRepository.save(actividad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("actividad", actividad.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actividads : get all the actividads.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actividads in body
     */
    @RequestMapping(value = "/actividads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Actividad> getAllActividads() {
        log.debug("REST request to get all Actividads");
        List<Actividad> actividads = actividadRepository.findAll();
        return actividads;
    }

    /**
     * GET  /actividads/:id : get the "id" actividad.
     *
     * @param id the id of the actividad to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actividad, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/actividads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actividad> getActividad(@PathVariable Long id) {
        log.debug("REST request to get Actividad : {}", id);
        Actividad actividad = actividadRepository.findOne(id);
        return Optional.ofNullable(actividad)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /actividads/:id : delete the "id" actividad.
     *
     * @param id the id of the actividad to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/actividads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActividad(@PathVariable Long id) {
        log.debug("REST request to delete Actividad : {}", id);
        actividadRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("actividad", id.toString())).build();
    }

}
