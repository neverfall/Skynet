package com.pernix_central.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pernix_central.domain.CustomerSatisfactionGrade;
import com.pernix_central.repository.CustomerSatisfactionGradeRepository;
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
 * REST controller for managing CustomerSatisfactionGrade.
 */
@RestController
@RequestMapping("/api")
public class CustomerSatisfactionGradeResource {

    private final Logger log = LoggerFactory.getLogger(CustomerSatisfactionGradeResource.class);
        
    @Inject
    private CustomerSatisfactionGradeRepository customerSatisfactionGradeRepository;
    
    /**
     * POST  /customer-satisfaction-grades : Create a new customerSatisfactionGrade.
     *
     * @param customerSatisfactionGrade the customerSatisfactionGrade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerSatisfactionGrade, or with status 400 (Bad Request) if the customerSatisfactionGrade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-satisfaction-grades",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerSatisfactionGrade> createCustomerSatisfactionGrade(@RequestBody CustomerSatisfactionGrade customerSatisfactionGrade) throws URISyntaxException {
        log.debug("REST request to save CustomerSatisfactionGrade : {}", customerSatisfactionGrade);
        if (customerSatisfactionGrade.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerSatisfactionGrade", "idexists", "A new customerSatisfactionGrade cannot already have an ID")).body(null);
        }
        CustomerSatisfactionGrade result = customerSatisfactionGradeRepository.save(customerSatisfactionGrade);
        return ResponseEntity.created(new URI("/api/customer-satisfaction-grades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerSatisfactionGrade", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-satisfaction-grades : Updates an existing customerSatisfactionGrade.
     *
     * @param customerSatisfactionGrade the customerSatisfactionGrade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerSatisfactionGrade,
     * or with status 400 (Bad Request) if the customerSatisfactionGrade is not valid,
     * or with status 500 (Internal Server Error) if the customerSatisfactionGrade couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-satisfaction-grades",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerSatisfactionGrade> updateCustomerSatisfactionGrade(@RequestBody CustomerSatisfactionGrade customerSatisfactionGrade) throws URISyntaxException {
        log.debug("REST request to update CustomerSatisfactionGrade : {}", customerSatisfactionGrade);
        if (customerSatisfactionGrade.getId() == null) {
            return createCustomerSatisfactionGrade(customerSatisfactionGrade);
        }
        CustomerSatisfactionGrade result = customerSatisfactionGradeRepository.save(customerSatisfactionGrade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerSatisfactionGrade", customerSatisfactionGrade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-satisfaction-grades : get all the customerSatisfactionGrades.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customerSatisfactionGrades in body
     */
    @RequestMapping(value = "/customer-satisfaction-grades",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CustomerSatisfactionGrade> getAllCustomerSatisfactionGrades() {
        log.debug("REST request to get all CustomerSatisfactionGrades");
        List<CustomerSatisfactionGrade> customerSatisfactionGrades = customerSatisfactionGradeRepository.findAll();
        return customerSatisfactionGrades;
    }

    /**
     * GET  /customer-satisfaction-grades/:id : get the "id" customerSatisfactionGrade.
     *
     * @param id the id of the customerSatisfactionGrade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerSatisfactionGrade, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/customer-satisfaction-grades/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerSatisfactionGrade> getCustomerSatisfactionGrade(@PathVariable Long id) {
        log.debug("REST request to get CustomerSatisfactionGrade : {}", id);
        CustomerSatisfactionGrade customerSatisfactionGrade = customerSatisfactionGradeRepository.findOne(id);
        return Optional.ofNullable(customerSatisfactionGrade)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customer-satisfaction-grades/:id : delete the "id" customerSatisfactionGrade.
     *
     * @param id the id of the customerSatisfactionGrade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/customer-satisfaction-grades/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerSatisfactionGrade(@PathVariable Long id) {
        log.debug("REST request to delete CustomerSatisfactionGrade : {}", id);
        customerSatisfactionGradeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerSatisfactionGrade", id.toString())).build();
    }

}
