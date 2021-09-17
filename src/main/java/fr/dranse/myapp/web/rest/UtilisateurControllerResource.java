package fr.dranse.myapp.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * UtilisateurControllerResource controller
 */
@RestController
@RequestMapping("/api/utilisateur-controller")
public class UtilisateurControllerResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurControllerResource.class);

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
}
