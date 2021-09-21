package fr.dranse.myapp.web.rest;

import fr.dranse.myapp.domain.Categorie;
import fr.dranse.myapp.repository.CategorieRepository;
import fr.dranse.myapp.service.CategorieService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * CategorieControllerResource controller
 */
@RestController
@RequestMapping("/api/v1/categorie")
public class CategorieControllerResource {

    private final Logger log = LoggerFactory.getLogger(CategorieControllerResource.class);

    private final CategorieService categorieService;

    private final CategorieRepository categorieRepository;

    public CategorieControllerResource(CategorieService categorieService, CategorieRepository categorieRepository) {
        this.categorieService = categorieService;
        this.categorieRepository = categorieRepository;
    }

    /**
     * GET getCategories
     */
    @GetMapping("/most-popular")
    public List<Categorie> getAllCategories() {
        log.debug("REST request to get most popular cat Categories");
        return categorieService.getMostPopular();
    }
}
