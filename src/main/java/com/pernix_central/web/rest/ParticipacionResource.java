package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Participacion;
import com.pernix_central.repository.ParticipacionRepository;
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
 * REST controller for managing Participacion.
 */
@RestController
@RequestMapping("/api")
public class ParticipacionResource {

    private final Logger log = LoggerFactory.getLogger(ParticipacionResource.class);
        
    @Inject
    private ParticipacionRepository participacionRepository;
    
    /**
     * POST  /participacions : Create a new participacion.
     *
     * @param participacion the participacion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new participacion, or with status 400 (Bad Request) if the participacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/participacions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Participacion> createParticipacion(@RequestBody Participacion participacion) throws URISyntaxException {
        log.debug("REST request to save Participacion : {}", participacion);
        if (participacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("participacion", "idexists", "A new participacion cannot already have an ID")).body(null);
        }
        Participacion result = participacionRepository.save(participacion);
        return ResponseEntity.created(new URI("/api/participacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("participacion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /participacions : Updates an existing participacion.
     *
     * @param participacion the participacion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated participacion,
     * or with status 400 (Bad Request) if the participacion is not valid,
     * or with status 500 (Internal Server Error) if the participacion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/participacions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Participacion> updateParticipacion(@RequestBody Participacion participacion) throws URISyntaxException {
        log.debug("REST request to update Participacion : {}", participacion);
        if (participacion.getId() == null) {
            return createParticipacion(participacion);
        }
        Participacion result = participacionRepository.save(participacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("participacion", participacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /participacions : get all the participacions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of participacions in body
     */
    @RequestMapping(value = "/participacions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Participacion> getAllParticipacions() {
        log.debug("REST request to get all Participacions");
        List<Participacion> participacions = participacionRepository.findAll();
        return participacions;
    }

    /**
     * GET  /participacions/:id : get the "id" participacion.
     *
     * @param id the id of the participacion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the participacion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/participacions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Participacion> getParticipacion(@PathVariable Long id) {
        log.debug("REST request to get Participacion : {}", id);
        Participacion participacion = participacionRepository.findOne(id);
        return Optional.ofNullable(participacion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /participacions/:id : delete the "id" participacion.
     *
     * @param id the id of the participacion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/participacions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteParticipacion(@PathVariable Long id) {
        log.debug("REST request to delete Participacion : {}", id);
        participacionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("participacion", id.toString())).build();
    }

}
