package fr.dranse.myapp.web.rest;

import fr.dranse.myapp.domain.Livre;
import fr.dranse.myapp.repository.CategorieRepository;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.service.CategorieService;
import fr.dranse.myapp.service.LivreService;
import fr.dranse.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * LivreControllerResource controller
 */
@RestController
@RequestMapping("/api/livre-controller")
public class LivreControllerResource {

    private final Logger log = LoggerFactory.getLogger(LivreControllerResource.class);

    private static final String ENTITY_NAME = "livre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    //additions
    private final LivreService livreService;

    private final LivreRepository livreRepository;

    private final CategorieService categorieService;

    private final CategorieRepository categorieRepository;

    public LivreControllerResource(LivreService livreService, LivreRepository livreRepository, CategorieService categorieService, CategorieRepository categorieRepository) {
        this.livreService = livreService;
        this.livreRepository = livreRepository;
        this.categorieService = categorieService;
        this.categorieRepository = categorieRepository;
    }

    /**
     * POST creationLivre
     */
    @PostMapping("/creation-livre")
    //public String creationLivre() {
    //    return "creationLivre";
    //}
    public ResponseEntity<Livre> creationLivre(@RequestBody Livre livre) throws URISyntaxException {
        log.debug("REST request to save Livre : {}", livre);
        if (livre.getId() != null) {
            throw new BadRequestAlertException("A new livre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (
            (livre.getTitre() == null) ||
            (livre.getAuteur() == null) ||
            (livre.getPrix() == null) ||
            (livre.getSynopsis() == null) ||
            (livre.getEditeur() == null) ||
            (livre.getStock() == null)
        ) {
            throw new BadRequestAlertException(
                "A new livre needs a titre, auteur, prix, synopsis, editeur, stock",
                ENTITY_NAME,
                "idexists"
            );
        }
        Livre result = livreService.save(livre);
        return ResponseEntity
            .created(new URI("/api/livres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT modifInfoLivre
     */
    @PutMapping("/modif-info-livre/{id}")
    //public String modifInfoLivre() {
    //  return "modifInfoLivre";
    //}

    public ResponseEntity<Livre> modifInfoLivre(@PathVariable(value = "id", required = false) final Long id, @RequestBody Livre livre)
        throws URISyntaxException {
        log.debug("REST request to update Livre : {}, {}", id, livre);
        if (livre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livre.getId())) {
            throw new BadRequestAlertException("Invalid ID " + livre.getId() + " vs " + id, ENTITY_NAME, "idinvalid");
        }

        if (!livreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Livre result = livreService.save(livre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livre.getId().toString()))
            .body(result);
    }

    /**
     * TODO: (maybe) commanderLivre
     */

    // ADD HERE

    /**
     * GET rechercheParAuteur
     */
    @GetMapping("/recherche-par-auteur/{auteur}")
    //public String rechercheParAuteur() {
    //    return "rechercheParAuteur";
    //}
    public ResponseEntity<List<Livre>> rechercheParAuteur(Pageable pageable, @PathVariable String auteur) {
        Page<Livre> page = livreService.findByAuthor(pageable, auteur);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET rechercheParTitre
     */
    @GetMapping("/recherche-par-titre/{titre}")
    //public String rechercheParTitre() {
    //    return "rechercheParTitre";
    //}
    public ResponseEntity<List<Livre>> rechercheParTitre(Pageable pageable, @PathVariable String titre) {
        Page<Livre> page = livreService.findByTitle(pageable, titre);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * TODO: GET rechercheParCategorie
     */
    @GetMapping("/recherche-par-categorie")
    public String rechercheParCategorie() {
        return "rechercheParCategorie";
    }

    /*public ResponseEntity<List<Livre>> rechercheParCategorie(Pageable pageable, @PathVariable String categorie){
        Page<Livre> page = livreService.findByCategorie(pageable, categorie);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/

    /**
     * GET getBestseller
     */
    @GetMapping("/get-bestseller")
    public String getBestseller() {
        return "getBestseller";
    }

    /**
     * GET rechercheDynamique
     */
    @GetMapping("/recherche-dynamique")
    public String rechercheDynamique() {
        return "rechercheDynamique";
    }

    /**
     * GET getLivre
     */
    @GetMapping("/get-livre/{id}")
    public ResponseEntity<Livre> rechercheParTitre(Pageable pageable, @PathVariable Long id) {
        Optional<Livre> livre = livreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livre);
    }

    /**
     * DELETE deleteLivre
     */
    @DeleteMapping("/delete-livre/{id}")
    public ResponseEntity<Void> deleteLivre(@PathVariable Long id) {
        log.debug("REST request to delete Livre : {}", id);
        livreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
