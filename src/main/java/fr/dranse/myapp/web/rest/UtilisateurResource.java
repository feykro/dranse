package fr.dranse.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Utilisateur;
import fr.dranse.myapp.repository.UtilisateurRepository;
import fr.dranse.myapp.service.UtilisateurService;
import fr.dranse.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link fr.dranse.myapp.domain.Utilisateur}.
 */
@RestController
@RequestMapping("/api")
public class UtilisateurResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurResource.class);

    private static final String ENTITY_NAME = "utilisateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilisateurService utilisateurService;

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurResource(UtilisateurService utilisateurService, UtilisateurRepository utilisateurRepository) {
        this.utilisateurService = utilisateurService;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * {@code POST  /utilisateurs} : Create a new utilisateur.
     *
     * @param utilisateur the utilisateur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilisateur, or with status {@code 400 (Bad Request)} if the utilisateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) throws URISyntaxException {
        log.debug("REST request to save Utilisateur : {}", utilisateur);
        if (utilisateur.getId() != null) {
            throw new BadRequestAlertException("A new utilisateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Utilisateur result = utilisateurService.save(utilisateur);
        return ResponseEntity
            .created(new URI("/api/utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utilisateurs/:id} : Updates an existing utilisateur.
     *
     * @param id the id of the utilisateur to save.
     * @param utilisateur the utilisateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateur,
     * or with status {@code 400 (Bad Request)} if the utilisateur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilisateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Utilisateur utilisateur
    ) throws URISyntaxException {
        log.debug("REST request to update Utilisateur : {}, {}", id, utilisateur);
        if (utilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Utilisateur result = utilisateurService.save(utilisateur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utilisateurs/:id} : Partial updates given fields of an existing utilisateur, field will ignore if it is null
     *
     * @param id the id of the utilisateur to save.
     * @param utilisateur the utilisateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisateur,
     * or with status {@code 400 (Bad Request)} if the utilisateur is not valid,
     * or with status {@code 404 (Not Found)} if the utilisateur is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilisateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utilisateurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Utilisateur> partialUpdateUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Utilisateur utilisateur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Utilisateur partially : {}, {}", id, utilisateur);
        if (utilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Utilisateur> result = utilisateurService.partialUpdate(utilisateur);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateur.getId().toString())
        );
    }

    /**
     * {@code GET  /utilisateurs} : get all the utilisateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilisateurs in body.
     */
    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs(Pageable pageable) {
        log.debug("REST request to get a page of Utilisateurs");
        Page<Utilisateur> page = utilisateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /utilisateurs/:id} : get the "id" utilisateur.
     *
     * @param id the id of the utilisateur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilisateur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
        log.debug("REST request to get Utilisateur : {}", id);
        Optional<Utilisateur> utilisateur = utilisateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utilisateur);
    }

    /**
     * {@code DELETE  /utilisateurs/:id} : delete the "id" utilisateur.
     *
     * @param id the id of the utilisateur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        log.debug("REST request to delete Utilisateur : {}", id);
        utilisateurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/utilisateurs?query=:query} : search for the utilisateur corresponding
     * to the query.
     *
     * @param query the query of the utilisateur search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/utilisateurs")
    public ResponseEntity<List<Utilisateur>> searchUtilisateurs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Utilisateurs for query {}", query);
        Page<Utilisateur> page = utilisateurService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
