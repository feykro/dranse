package fr.dranse.myapp.web.rest;

import fr.dranse.myapp.domain.Commande;
import fr.dranse.myapp.domain.LigneCommande;
import fr.dranse.myapp.domain.Utilisateur;
import fr.dranse.myapp.repository.CommandeRepository;
import fr.dranse.myapp.service.CommandeService;
import fr.dranse.myapp.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import fr.dranse.myapp.service.MailService;
import fr.dranse.myapp.service.UtilisateurService;

/**
 * CommandeControllerResource controller
 */
@RestController
@RequestMapping("/api/v1/commande")
public class CommandeControllerResource {

    private final Logger log = LoggerFactory.getLogger(CommandeControllerResource.class);
    private static final String ENTITY_NAME = "commande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandeService commandeService;
    private final CommandeRepository commandeRepository;
    private final MailService mailService;
    private final UtilisateurService utilisateurService;

    public CommandeControllerResource(CommandeService commandeService, CommandeRepository commandeRepository, MailService mailService, UtilisateurService utilisateurService) {
        this.commandeService = commandeService;
        this.commandeRepository = commandeRepository;
        this.mailService = mailService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * POST creationCommande
     */
    // todo transaction
    @PostMapping("")
    public ResponseEntity<Commande> creationCommande(@RequestBody LigneCommande ligneCommande) throws URISyntaxException {
        log.debug("REST request to create a new command with an item: {}", ligneCommande);
        if (ligneCommande.getId() != null) {
            throw new BadRequestAlertException("A new ligneCommande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Commande result = commandeService.newCommande(ligneCommande.getLivre().getId(), ligneCommande.getQuantite());
        return ResponseEntity
            .created(new URI("/api/commande-controller/get-commande/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.toString()))
            .body(result);
    }

    /**
     * PUT ajoutLigne
     */
    // todo transact
    @PutMapping("/modifier/{id}") // todo rename to update?? and simplifier avec 1qqt et 1 idlivre?
    public ResponseEntity<Commande> modifierLigne(
        @PathVariable(value = "id", required = true) final Long id,
        @RequestBody LigneCommande ligneCommande
    ) throws URISyntaxException {
        log.debug("REST request to modify line to existing command : {}, {}", id, ligneCommande);
        if (!commandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Commande result = commandeService.modifierLigneCommande(id, ligneCommande.getLivre().getId(), ligneCommande.getQuantite());
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    @PutMapping("/ajout/{id}")
    public ResponseEntity<Commande> ajoutLigne(
        @PathVariable(value = "id", required = true) final Long id,
        @RequestBody LigneCommande ligneCommande
    ) throws URISyntaxException {
        log.debug("REST request to add line to existing command : {}, {}", id, ligneCommande);
        if (!commandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Commande result = commandeService.ajouterLigneCommande(id, ligneCommande.getLivre().getId(), ligneCommande.getQuantite());
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * PUT passerCommande
     */
    @PutMapping("/commander/{id}")
    public boolean passerCommande(
        @PathVariable(value = "id", required = true) final Long id,
        @RequestBody Commande commande
    ) {
        // todo transactionnal
        boolean ret = commandeService.commander(commande);
        if (ret){
            String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();

            Optional<Utilisateur> utilisateur = utilisateurService.utilisateurFromLogin(userLogin);
            /*mailService.sendEmail(
                utilisateur.get().getUserP().getEmail(),
                "Confirmation commande",
                "\n\n\nHello !",
                false,
                false);*/
            mailService.sendEmailFromTemplate(utilisateur.get().getUserP(),
                "mail/confirmationCommande",
                "confirmation.commande.title");
        }
        return ret;
    }

    /**
     * GET getCommande
     */
    @GetMapping("/{id}")
    public Commande getCommande(@PathVariable Long id) {
        log.debug("REST request to get Commande (1) : {}", id);
        Optional<Commande> commande = commandeService.findOne(id);
        if(commande.isEmpty()){
            return null;
        }else{
            return commande.get();
        }
    }

    /**
     * DELETE deleteCommande
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        log.debug("REST request to delete Commande (1) : {}", id);
        commandeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * DELETE deleteLigne
     */
    @DeleteMapping("/ligne/{idCommande}/{idLigne}")
    public ResponseEntity<Void> deleteLigne(@PathVariable Long idCommande, @PathVariable Long idLigne) {
        log.debug("REST request to delete ligne {} from commande {}", idCommande, idLigne);
        commandeService.SupprimerLigne(idCommande, idLigne);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, idLigne.toString()))
            .build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<Commande>> getHistory(Pageable pageable) {
        log.debug("REST request to get the history of a user");
        Page<Commande> page = commandeService.getHistory(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
