package fr.dranse.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Avis;
import fr.dranse.myapp.repository.AvisRepository;
import fr.dranse.myapp.service.AvisService;
import fr.dranse.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.dranse.myapp.domain.Avis}.
 */
@RestController
@RequestMapping("/api")
public class AvisResource {

    private final Logger log = LoggerFactory.getLogger(AvisResource.class);

    private static final String ENTITY_NAME = "avis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvisService avisService;

    private final AvisRepository avisRepository;

    public AvisResource(AvisService avisService, AvisRepository avisRepository) {
        this.avisService = avisService;
        this.avisRepository = avisRepository;
    }

    /**
     * {@code POST  /avis} : Create a new avis.
     *
     * @param avis the avis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avis, or with status {@code 400 (Bad Request)} if the avis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/avis")
    public ResponseEntity<Avis> createAvis(@Valid @RequestBody Avis avis) throws URISyntaxException {
        log.debug("REST request to save Avis : {}", avis);
        if (avis.getId() != null) {
            throw new BadRequestAlertException("A new avis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Avis result = avisService.save(avis);
        return ResponseEntity
            .created(new URI("/api/avis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /avis/:id} : Updates an existing avis.
     *
     * @param id the id of the avis to save.
     * @param avis the avis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avis,
     * or with status {@code 400 (Bad Request)} if the avis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/avis/{id}")
    public ResponseEntity<Avis> updateAvis(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Avis avis)
        throws URISyntaxException {
        log.debug("REST request to update Avis : {}, {}", id, avis);
        if (avis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Avis result = avisService.save(avis);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avis.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /avis/:id} : Partial updates given fields of an existing avis, field will ignore if it is null
     *
     * @param id the id of the avis to save.
     * @param avis the avis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avis,
     * or with status {@code 400 (Bad Request)} if the avis is not valid,
     * or with status {@code 404 (Not Found)} if the avis is not found,
     * or with status {@code 500 (Internal Server Error)} if the avis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/avis/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Avis> partialUpdateAvis(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Avis avis
    ) throws URISyntaxException {
        log.debug("REST request to partial update Avis partially : {}, {}", id, avis);
        if (avis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Avis> result = avisService.partialUpdate(avis);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avis.getId().toString())
        );
    }

    /**
     * {@code GET  /avis} : get all the avis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avis in body.
     */
    @GetMapping("/avis")
    public ResponseEntity<List<Avis>> getAllAvis(Pageable pageable) {
        log.debug("REST request to get a page of Avis");
        Page<Avis> page = avisService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /avis/:id} : get the "id" avis.
     *
     * @param id the id of the avis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/avis/{id}")
    public ResponseEntity<Avis> getAvis(@PathVariable Long id) {
        log.debug("REST request to get Avis : {}", id);
        Optional<Avis> avis = avisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(avis);
    }

    /**
     * {@code DELETE  /avis/:id} : delete the "id" avis.
     *
     * @param id the id of the avis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/avis/{id}")
    public ResponseEntity<Void> deleteAvis(@PathVariable Long id) {
        log.debug("REST request to delete Avis : {}", id);
        avisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/avis?query=:query} : search for the avis corresponding
     * to the query.
     *
     * @param query the query of the avis search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/avis")
    public ResponseEntity<List<Avis>> searchAvis(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Avis for query {}", query);
        Page<Avis> page = avisService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
