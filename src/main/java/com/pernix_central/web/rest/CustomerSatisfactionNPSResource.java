package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.CustomerSatisfactionNPS;
import com.pernix_central.repository.CustomerSatisfactionNPSRepository;
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
 * REST controller for managing CustomerSatisfactionNPS.
 */
@RestController
@RequestMapping("/api")
public class CustomerSatisfactionNPSResource {

    private final Logger log = LoggerFactory.getLogger(CustomerSatisfactionNPSResource.class);
        
    @Inject
    private CustomerSatisfactionNPSRepository customerSatisfactionNPSRepository;
    
    /**
     * POST  /customer-satisfaction-nps : Create a new customerSatisfactionNPS.
     *
     * @param customerSatisfactionNPS the customerSatisfactionNPS to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerSatisfactionNPS, or with status 400 (Bad Request) if the customerSatisfactionNPS has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-satisfaction-nps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerSatisfactionNPS> createCustomerSatisfactionNPS(@RequestBody CustomerSatisfactionNPS customerSatisfactionNPS) throws URISyntaxException {
        log.debug("REST request to save CustomerSatisfactionNPS : {}", customerSatisfactionNPS);
        if (customerSatisfactionNPS.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerSatisfactionNPS", "idexists", "A new customerSatisfactionNPS cannot already have an ID")).body(null);
        }
        CustomerSatisfactionNPS result = customerSatisfactionNPSRepository.save(customerSatisfactionNPS);
        return ResponseEntity.created(new URI("/api/customer-satisfaction-nps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerSatisfactionNPS", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-satisfaction-nps : Updates an existing customerSatisfactionNPS.
     *
     * @param customerSatisfactionNPS the customerSatisfactionNPS to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerSatisfactionNPS,
     * or with status 400 (Bad Request) if the customerSatisfactionNPS is not valid,
     * or with status 500 (Internal Server Error) if the customerSatisfactionNPS couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-satisfaction-nps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerSatisfactionNPS> updateCustomerSatisfactionNPS(@RequestBody CustomerSatisfactionNPS customerSatisfactionNPS) throws URISyntaxException {
        log.debug("REST request to update CustomerSatisfactionNPS : {}", customerSatisfactionNPS);
        if (customerSatisfactionNPS.getId() == null) {
            return createCustomerSatisfactionNPS(customerSatisfactionNPS);
        }
        CustomerSatisfactionNPS result = customerSatisfactionNPSRepository.save(customerSatisfactionNPS);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerSatisfactionNPS", customerSatisfactionNPS.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-satisfaction-nps : get all the customerSatisfactionNPS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customerSatisfactionNPS in body
     */
    @RequestMapping(value = "/customer-satisfaction-nps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CustomerSatisfactionNPS> getAllCustomerSatisfactionNPS() {
        log.debug("REST request to get all CustomerSatisfactionNPS");
        List<CustomerSatisfactionNPS> customerSatisfactionNPS = customerSatisfactionNPSRepository.findAll();
        return customerSatisfactionNPS;
    }

    /**
     * GET  /customer-satisfaction-nps/:id : get the "id" customerSatisfactionNPS.
     *
     * @param id the id of the customerSatisfactionNPS to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerSatisfactionNPS, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/customer-satisfaction-nps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerSatisfactionNPS> getCustomerSatisfactionNPS(@PathVariable Long id) {
        log.debug("REST request to get CustomerSatisfactionNPS : {}", id);
        CustomerSatisfactionNPS customerSatisfactionNPS = customerSatisfactionNPSRepository.findOne(id);
        return Optional.ofNullable(customerSatisfactionNPS)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customer-satisfaction-nps/:id : delete the "id" customerSatisfactionNPS.
     *
     * @param id the id of the customerSatisfactionNPS to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/customer-satisfaction-nps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerSatisfactionNPS(@PathVariable Long id) {
        log.debug("REST request to delete CustomerSatisfactionNPS : {}", id);
        customerSatisfactionNPSRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerSatisfactionNPS", id.toString())).build();
    }

}
