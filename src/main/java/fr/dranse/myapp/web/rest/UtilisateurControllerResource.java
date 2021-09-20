package fr.dranse.myapp.web.rest;

import fr.dranse.myapp.domain.Utilisateur;
import fr.dranse.myapp.service.UtilisateurService;
import java.security.Principal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * UtilisateurControllerResource controller
 */
@RestController
@RequestMapping("/api/utilisateur-controller")
public class UtilisateurControllerResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurControllerResource.class);

    @Autowired
    UtilisateurService utilisateurService;

    /**
     * POST infoUtilisateur
     */
    @PostMapping("/info-utilisateur")
    public String infoUtilisateur() {
        return "infoUtilisateur";
    }

    /**
     * PUT modifMDP
     */
    @PutMapping("/modif-mdp")
    public String modifMDP() {
        return "modifMDP";
    }

    /**
     * PUT modifInfo
     */
    @PutMapping("/modif-info")
    public String modifInfo() {
        return "modifInfo";
    }

    /**
     * GET listeUtilisateurs
     */
    @GetMapping("/liste-utilisateurs")
    public String listeUtilisateurs() {
        return "listeUtilisateurs";
    }

    /**
     * GET lectureHistorique
     */
    @GetMapping("/lecture-historique")
    public String lectureHistorique() {
        return "lectureHistorique";
    }

    /**
     * GET lectureInfoLivraison
     */
    @GetMapping("/lecture-info-livraison")
    public String lectureInfoLivraison() {
        return "lectureInfoLivraison";
    }

    /**
     * GET lectureInfoCompte
     */
    @GetMapping("/lecture-info-compte")
    public String lectureInfoCompte() {
        return "lectureInfoCompte";
    }

    /**
     * DELETE deleteUtilisateur
     */
    @DeleteMapping("/delete-utilisateur")
    public String deleteUtilisateur() {
        return "deleteUtilisateur";
    }

    @GetMapping("/utilisateur-courant")
    ResponseEntity<Utilisateur> utilisateurCourant(Principal principal) {
        log.debug("REST request to get current user ");
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Utilisateur> utilisateur = utilisateurService.utilisateurFromLogin(userLogin);
        return ResponseUtil.wrapOrNotFound(utilisateur);
    }
}
