package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Collaborator;
import com.pernix_central.repository.CollaboratorRepository;
import com.pernix_central.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Collaborator.
 */
@RestController
@RequestMapping("/api")
public class CollaboratorResource {

    private final Logger log = LoggerFactory.getLogger(CollaboratorResource.class);
        
    @Inject
    private CollaboratorRepository collaboratorRepository;
    
    /**
     * POST  /collaborators : Create a new collaborator.
     *
     * @param collaborator the collaborator to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collaborator, or with status 400 (Bad Request) if the collaborator has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collaborators",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collaborator> createCollaborator(@Valid @RequestBody Collaborator collaborator) throws URISyntaxException {
        log.debug("REST request to save Collaborator : {}", collaborator);
        if (collaborator.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("collaborator", "idexists", "A new collaborator cannot already have an ID")).body(null);
        }
        Collaborator result = collaboratorRepository.save(collaborator);
        return ResponseEntity.created(new URI("/api/collaborators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("collaborator", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collaborators : Updates an existing collaborator.
     *
     * @param collaborator the collaborator to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collaborator,
     * or with status 400 (Bad Request) if the collaborator is not valid,
     * or with status 500 (Internal Server Error) if the collaborator couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/collaborators",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collaborator> updateCollaborator(@Valid @RequestBody Collaborator collaborator) throws URISyntaxException {
        log.debug("REST request to update Collaborator : {}", collaborator);
        if (collaborator.getId() == null) {
            return createCollaborator(collaborator);
        }
        Collaborator result = collaboratorRepository.save(collaborator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("collaborator", collaborator.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collaborators : get all the collaborators.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of collaborators in body
     */
    @RequestMapping(value = "/collaborators",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Collaborator> getAllCollaborators() {
        log.debug("REST request to get all Collaborators");
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        return collaborators;
    }

    /**
     * GET  /collaborators/:id : get the "id" collaborator.
     *
     * @param id the id of the collaborator to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collaborator, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/collaborators/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collaborator> getCollaborator(@PathVariable Long id) {
        log.debug("REST request to get Collaborator : {}", id);
        Collaborator collaborator = collaboratorRepository.findOne(id);
        return Optional.ofNullable(collaborator)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /collaborators/:id : delete the "id" collaborator.
     *
     * @param id the id of the collaborator to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/collaborators/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCollaborator(@PathVariable Long id) {
        log.debug("REST request to delete Collaborator : {}", id);
        collaboratorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("collaborator", id.toString())).build();
    }

}
