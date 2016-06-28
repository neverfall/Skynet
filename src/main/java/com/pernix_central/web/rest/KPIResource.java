package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.KPI;
import com.pernix_central.repository.KPIRepository;
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
 * REST controller for managing KPI.
 */
@RestController
@RequestMapping("/api")
public class KPIResource {

    private final Logger log = LoggerFactory.getLogger(KPIResource.class);
        
    @Inject
    private KPIRepository kPIRepository;
    
    /**
     * POST  /k-pis : Create a new kPI.
     *
     * @param kPI the kPI to create
     * @return the ResponseEntity with status 201 (Created) and with body the new kPI, or with status 400 (Bad Request) if the kPI has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/k-pis",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KPI> createKPI(@RequestBody KPI kPI) throws URISyntaxException {
        log.debug("REST request to save KPI : {}", kPI);
        if (kPI.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("kPI", "idexists", "A new kPI cannot already have an ID")).body(null);
        }
        KPI result = kPIRepository.save(kPI);
        return ResponseEntity.created(new URI("/api/k-pis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("kPI", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /k-pis : Updates an existing kPI.
     *
     * @param kPI the kPI to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated kPI,
     * or with status 400 (Bad Request) if the kPI is not valid,
     * or with status 500 (Internal Server Error) if the kPI couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/k-pis",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KPI> updateKPI(@RequestBody KPI kPI) throws URISyntaxException {
        log.debug("REST request to update KPI : {}", kPI);
        if (kPI.getId() == null) {
            return createKPI(kPI);
        }
        KPI result = kPIRepository.save(kPI);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("kPI", kPI.getId().toString()))
            .body(result);
    }

    /**
     * GET  /k-pis : get all the kPIS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of kPIS in body
     */
    @RequestMapping(value = "/k-pis",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<KPI> getAllKPIS() {
        log.debug("REST request to get all KPIS");
        List<KPI> kPIS = kPIRepository.findAll();
        return kPIS;
    }

    /**
     * GET  /k-pis/:id : get the "id" kPI.
     *
     * @param id the id of the kPI to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the kPI, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/k-pis/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KPI> getKPI(@PathVariable Long id) {
        log.debug("REST request to get KPI : {}", id);
        KPI kPI = kPIRepository.findOne(id);
        return Optional.ofNullable(kPI)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /k-pis/:id : delete the "id" kPI.
     *
     * @param id the id of the kPI to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/k-pis/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteKPI(@PathVariable Long id) {
        log.debug("REST request to delete KPI : {}", id);
        kPIRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("kPI", id.toString())).build();
    }

}
