package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Gamification;
import com.pernix_central.repository.GamificationRepository;
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
 * REST controller for managing Gamification.
 */
@RestController
@RequestMapping("/api")
public class GamificationResource {

    private final Logger log = LoggerFactory.getLogger(GamificationResource.class);
        
    @Inject
    private GamificationRepository gamificationRepository;
    
    /**
     * POST  /gamifications : Create a new gamification.
     *
     * @param gamification the gamification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gamification, or with status 400 (Bad Request) if the gamification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gamifications",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gamification> createGamification(@RequestBody Gamification gamification) throws URISyntaxException {
        log.debug("REST request to save Gamification : {}", gamification);
        if (gamification.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gamification", "idexists", "A new gamification cannot already have an ID")).body(null);
        }
        Gamification result = gamificationRepository.save(gamification);
        return ResponseEntity.created(new URI("/api/gamifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gamification", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gamifications : Updates an existing gamification.
     *
     * @param gamification the gamification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gamification,
     * or with status 400 (Bad Request) if the gamification is not valid,
     * or with status 500 (Internal Server Error) if the gamification couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gamifications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gamification> updateGamification(@RequestBody Gamification gamification) throws URISyntaxException {
        log.debug("REST request to update Gamification : {}", gamification);
        if (gamification.getId() == null) {
            return createGamification(gamification);
        }
        Gamification result = gamificationRepository.save(gamification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gamification", gamification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gamifications : get all the gamifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gamifications in body
     */
    @RequestMapping(value = "/gamifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Gamification> getAllGamifications() {
        log.debug("REST request to get all Gamifications");
        List<Gamification> gamifications = gamificationRepository.findAll();
        return gamifications;
    }

    /**
     * GET  /gamifications/:id : get the "id" gamification.
     *
     * @param id the id of the gamification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gamification, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/gamifications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gamification> getGamification(@PathVariable Long id) {
        log.debug("REST request to get Gamification : {}", id);
        Gamification gamification = gamificationRepository.findOne(id);
        return Optional.ofNullable(gamification)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gamifications/:id : delete the "id" gamification.
     *
     * @param id the id of the gamification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/gamifications/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGamification(@PathVariable Long id) {
        log.debug("REST request to delete Gamification : {}", id);
        gamificationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gamification", id.toString())).build();
    }

}
