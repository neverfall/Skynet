package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Duration;
import com.pernix_central.repository.DurationRepository;
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
 * REST controller for managing Duration.
 */
@RestController
@RequestMapping("/api")
public class DurationResource {

    private final Logger log = LoggerFactory.getLogger(DurationResource.class);
        
    @Inject
    private DurationRepository durationRepository;
    
    /**
     * POST  /durations : Create a new duration.
     *
     * @param duration the duration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new duration, or with status 400 (Bad Request) if the duration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/durations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Duration> createDuration(@RequestBody Duration duration) throws URISyntaxException {
        log.debug("REST request to save Duration : {}", duration);
        if (duration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("duration", "idexists", "A new duration cannot already have an ID")).body(null);
        }
        Duration result = durationRepository.save(duration);
        return ResponseEntity.created(new URI("/api/durations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("duration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /durations : Updates an existing duration.
     *
     * @param duration the duration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated duration,
     * or with status 400 (Bad Request) if the duration is not valid,
     * or with status 500 (Internal Server Error) if the duration couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/durations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Duration> updateDuration(@RequestBody Duration duration) throws URISyntaxException {
        log.debug("REST request to update Duration : {}", duration);
        if (duration.getId() == null) {
            return createDuration(duration);
        }
        Duration result = durationRepository.save(duration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("duration", duration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /durations : get all the durations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of durations in body
     */
    @RequestMapping(value = "/durations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Duration> getAllDurations() {
        log.debug("REST request to get all Durations");
        List<Duration> durations = durationRepository.findAll();
        return durations;
    }

    /**
     * GET  /durations/:id : get the "id" duration.
     *
     * @param id the id of the duration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the duration, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/durations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Duration> getDuration(@PathVariable Long id) {
        log.debug("REST request to get Duration : {}", id);
        Duration duration = durationRepository.findOne(id);
        return Optional.ofNullable(duration)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /durations/:id : delete the "id" duration.
     *
     * @param id the id of the duration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/durations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDuration(@PathVariable Long id) {
        log.debug("REST request to delete Duration : {}", id);
        durationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("duration", id.toString())).build();
    }

}
