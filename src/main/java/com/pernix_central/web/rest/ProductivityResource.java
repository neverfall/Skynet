package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.Productivity;
import com.pernix_central.repository.ProductivityRepository;
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
 * REST controller for managing Productivity.
 */
@RestController
@RequestMapping("/api")
public class ProductivityResource {

    private final Logger log = LoggerFactory.getLogger(ProductivityResource.class);
        
    @Inject
    private ProductivityRepository productivityRepository;
    
    /**
     * POST  /productivities : Create a new productivity.
     *
     * @param productivity the productivity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productivity, or with status 400 (Bad Request) if the productivity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/productivities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Productivity> createProductivity(@RequestBody Productivity productivity) throws URISyntaxException {
        log.debug("REST request to save Productivity : {}", productivity);
        if (productivity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productivity", "idexists", "A new productivity cannot already have an ID")).body(null);
        }
        Productivity result = productivityRepository.save(productivity);
        return ResponseEntity.created(new URI("/api/productivities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productivity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productivities : Updates an existing productivity.
     *
     * @param productivity the productivity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productivity,
     * or with status 400 (Bad Request) if the productivity is not valid,
     * or with status 500 (Internal Server Error) if the productivity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/productivities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Productivity> updateProductivity(@RequestBody Productivity productivity) throws URISyntaxException {
        log.debug("REST request to update Productivity : {}", productivity);
        if (productivity.getId() == null) {
            return createProductivity(productivity);
        }
        Productivity result = productivityRepository.save(productivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productivity", productivity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productivities : get all the productivities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productivities in body
     */
    @RequestMapping(value = "/productivities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Productivity> getAllProductivities() {
        log.debug("REST request to get all Productivities");
        List<Productivity> productivities = productivityRepository.findAll();
        return productivities;
    }

    /**
     * GET  /productivities/:id : get the "id" productivity.
     *
     * @param id the id of the productivity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productivity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/productivities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Productivity> getProductivity(@PathVariable Long id) {
        log.debug("REST request to get Productivity : {}", id);
        Productivity productivity = productivityRepository.findOne(id);
        return Optional.ofNullable(productivity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productivities/:id : delete the "id" productivity.
     *
     * @param id the id of the productivity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/productivities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductivity(@PathVariable Long id) {
        log.debug("REST request to delete Productivity : {}", id);
        productivityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productivity", id.toString())).build();
    }

}
