package fr.dranse.myapp.web.rest;

import fr.dranse.myapp.domain.Livre;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.service.LivreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

/**
 * LivreControllerResource controller
 */
@RestController
@RequestMapping("/api/livre-controller")
public class LivreControllerResource {

    private final Logger log = LoggerFactory.getLogger(LivreControllerResource.class);

    //additions
    private final LivreService livreService;

    private final LivreRepository livreRepository;

    public LivreControllerResource(LivreService livreService, LivreRepository livreRepository) {
        this.livreService = livreService;
        this.livreRepository = livreRepository;
    }


    /**
     * POST creationLivre
     */
    @PostMapping("/creation-livre")
    public String creationLivre() {
        return "creationLivre";
    }

    /**
     * PUT modifInfoLivre
     */
    @PutMapping("/modif-info-livre")
    public String modifInfoLivre() {
        return "modifInfoLivre";
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
    public ResponseEntity<List<Livre>> rechercheParAuteur(Pageable pageable, @PathVariable String auteur){
        Page<Livre> page = livreService.findByAuthor(pageable, auteur);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET rechercheParTitre
     */
    @GetMapping("/recherche-par-titre")
    public String rechercheParTitre() {
        return "rechercheParTitre";
    }

    /**
     * GET rechercheParCategorie
     */
    @GetMapping("/recherche-par-categorie")
    public String rechercheParCategorie() {
        return "rechercheParCategorie";
    }

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
    @GetMapping("/get-livre")
    public String getLivre() {
        return "getLivre";
    }

    /**
     * DELETE deleteLivre
     */
    @DeleteMapping("/delete-livre")
    public String deleteLivre() {
        return "deleteLivre";
    }
}
