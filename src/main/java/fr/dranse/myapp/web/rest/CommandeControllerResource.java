package fr.dranse.myapp.web.rest;

import fr.dranse.myapp.domain.Commande;
import fr.dranse.myapp.domain.LigneCommande;
import fr.dranse.myapp.repository.CommandeRepository;
import fr.dranse.myapp.service.CommandeService;
import fr.dranse.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

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

    public CommandeControllerResource(CommandeService commandeService, CommandeRepository commandeRepository) {
        this.commandeService = commandeService;
        this.commandeRepository = commandeRepository;
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
        Commande result = commandeService.newCommande(ligneCommande);
        return ResponseEntity
            .created(new URI("/api/commande-controller/get-commande/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.toString()))
            .body(result);
    }

    /**
     * PUT ajoutLigne
     */
    // todo transact
    @PutMapping("/ajout/{id}") // todo rename to update??
    public ResponseEntity<Commande> ajoutLigne(
        @PathVariable(value = "id", required = true) final Long id,
        @RequestBody LigneCommande ligneCommande
    ) throws URISyntaxException {
        log.debug("REST request to add line to existing command : {}, {}", id, ligneCommande);
        if (!commandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Commande result = commandeService.ajouterLigne(id, ligneCommande);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * PUT passerCommande
     */
    @PutMapping("/commander/{id}")
    public boolean passerCommande(@PathVariable(value = "id", required = true) final Long id) {
        // todo transactionnal
        return true;
    }

    /**
     * GET getCommande
     */
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommande(@PathVariable Long id) {
        log.debug("REST request to get Commande (1) : {}", id);
        Optional<Commande> commande = commandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commande);
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
}
