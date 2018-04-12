package com.gesbtp.atos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gesbtp.atos.domain.Produits;

import com.gesbtp.atos.repository.ProduitsRepository;
import com.gesbtp.atos.repository.search.ProduitsSearchRepository;
import com.gesbtp.atos.web.rest.errors.BadRequestAlertException;
import com.gesbtp.atos.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Produits.
 */
@RestController
@RequestMapping("/api")
public class ProduitsResource {

    private final Logger log = LoggerFactory.getLogger(ProduitsResource.class);

    private static final String ENTITY_NAME = "produits";

    private final ProduitsRepository produitsRepository;

    private final ProduitsSearchRepository produitsSearchRepository;

    public ProduitsResource(ProduitsRepository produitsRepository, ProduitsSearchRepository produitsSearchRepository) {
        this.produitsRepository = produitsRepository;
        this.produitsSearchRepository = produitsSearchRepository;
    }

    /**
     * POST  /produits : Create a new produits.
     *
     * @param produits the produits to create
     * @return the ResponseEntity with status 201 (Created) and with body the new produits, or with status 400 (Bad Request) if the produits has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/produits")
    @Timed
    public ResponseEntity<Produits> createProduits(@Valid @RequestBody Produits produits) throws URISyntaxException {
        log.debug("REST request to save Produits : {}", produits);
        if (produits.getId() != null) {
            throw new BadRequestAlertException("A new produits cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Produits result = produitsRepository.save(produits);
        produitsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /produits : Updates an existing produits.
     *
     * @param produits the produits to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated produits,
     * or with status 400 (Bad Request) if the produits is not valid,
     * or with status 500 (Internal Server Error) if the produits couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/produits")
    @Timed
    public ResponseEntity<Produits> updateProduits(@Valid @RequestBody Produits produits) throws URISyntaxException {
        log.debug("REST request to update Produits : {}", produits);
        if (produits.getId() == null) {
            return createProduits(produits);
        }
        Produits result = produitsRepository.save(produits);
        produitsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, produits.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produits : get all the produits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of produits in body
     */
    @GetMapping("/produits")
    @Timed
    public List<Produits> getAllProduits() {
        log.debug("REST request to get all Produits");
        return produitsRepository.findAll();
        }

    /**
     * GET  /produits/:id : get the "id" produits.
     *
     * @param id the id of the produits to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the produits, or with status 404 (Not Found)
     */
    @GetMapping("/produits/{id}")
    @Timed
    public ResponseEntity<Produits> getProduits(@PathVariable Long id) {
        log.debug("REST request to get Produits : {}", id);
        Produits produits = produitsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(produits));
    }

    /**
     * DELETE  /produits/:id : delete the "id" produits.
     *
     * @param id the id of the produits to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/produits/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduits(@PathVariable Long id) {
        log.debug("REST request to delete Produits : {}", id);
        produitsRepository.delete(id);
        produitsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/produits?query=:query : search for the produits corresponding
     * to the query.
     *
     * @param query the query of the produits search
     * @return the result of the search
     */
    @GetMapping("/_search/produits")
    @Timed
    public List<Produits> searchProduits(@RequestParam String query) {
        log.debug("REST request to search Produits for query {}", query);
        return StreamSupport
            .stream(produitsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
