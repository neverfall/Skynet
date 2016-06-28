package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.FollowUp;
import com.pernix_central.repository.FollowUpRepository;
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
 * REST controller for managing FollowUp.
 */
@RestController
@RequestMapping("/api")
public class FollowUpResource {

    private final Logger log = LoggerFactory.getLogger(FollowUpResource.class);
        
    @Inject
    private FollowUpRepository followUpRepository;
    
    /**
     * POST  /follow-ups : Create a new followUp.
     *
     * @param followUp the followUp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new followUp, or with status 400 (Bad Request) if the followUp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/follow-ups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FollowUp> createFollowUp(@RequestBody FollowUp followUp) throws URISyntaxException {
        log.debug("REST request to save FollowUp : {}", followUp);
        if (followUp.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("followUp", "idexists", "A new followUp cannot already have an ID")).body(null);
        }
        FollowUp result = followUpRepository.save(followUp);
        return ResponseEntity.created(new URI("/api/follow-ups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("followUp", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /follow-ups : Updates an existing followUp.
     *
     * @param followUp the followUp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated followUp,
     * or with status 400 (Bad Request) if the followUp is not valid,
     * or with status 500 (Internal Server Error) if the followUp couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/follow-ups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FollowUp> updateFollowUp(@RequestBody FollowUp followUp) throws URISyntaxException {
        log.debug("REST request to update FollowUp : {}", followUp);
        if (followUp.getId() == null) {
            return createFollowUp(followUp);
        }
        FollowUp result = followUpRepository.save(followUp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("followUp", followUp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /follow-ups : get all the followUps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of followUps in body
     */
    @RequestMapping(value = "/follow-ups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FollowUp> getAllFollowUps() {
        log.debug("REST request to get all FollowUps");
        List<FollowUp> followUps = followUpRepository.findAll();
        return followUps;
    }

    /**
     * GET  /follow-ups/:id : get the "id" followUp.
     *
     * @param id the id of the followUp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the followUp, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/follow-ups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FollowUp> getFollowUp(@PathVariable Long id) {
        log.debug("REST request to get FollowUp : {}", id);
        FollowUp followUp = followUpRepository.findOne(id);
        return Optional.ofNullable(followUp)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /follow-ups/:id : delete the "id" followUp.
     *
     * @param id the id of the followUp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/follow-ups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFollowUp(@PathVariable Long id) {
        log.debug("REST request to delete FollowUp : {}", id);
        followUpRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("followUp", id.toString())).build();
    }

}
