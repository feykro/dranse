package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Commande;
import fr.dranse.myapp.domain.LigneCommande;
import fr.dranse.myapp.domain.Livre;
import fr.dranse.myapp.domain.Utilisateur;
import fr.dranse.myapp.repository.CommandeRepository;
import fr.dranse.myapp.repository.LigneCommandeRepository;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.repository.search.CommandeSearchRepository;
import fr.dranse.myapp.service.CommandeService;

import java.util.Optional;

import fr.dranse.myapp.service.LivreService;
import fr.dranse.myapp.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commande}.
 */
@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final Logger log = LoggerFactory.getLogger(CommandeServiceImpl.class);

    private final CommandeRepository commandeRepository;

    private final CommandeSearchRepository commandeSearchRepository;

    private final LigneCommandeRepository ligneCommandeRepository;

    private final LivreRepository livreRepository;

    @Autowired
    LivreService livreService;

    @Autowired
    UtilisateurService utilisateurService;

    public CommandeServiceImpl(CommandeRepository commandeRepository, CommandeSearchRepository commandeSearchRepository, LigneCommandeRepository ligneCommandeRepository, LivreRepository livreRepository) {
        this.commandeRepository = commandeRepository;
        this.commandeSearchRepository = commandeSearchRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.livreRepository = livreRepository;
    }

    @Override
    public Commande save(Commande commande) {
        log.debug("Request to save Commande : {}", commande);
        Commande result = commandeRepository.save(commande);
        commandeSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Commande> partialUpdate(Commande commande) {
        log.debug("Request to partially update Commande : {}", commande);

        return commandeRepository
            .findById(commande.getId())
            .map(
                existingCommande -> {
                    if (commande.getDate() != null) {
                        existingCommande.setDate(commande.getDate());
                    }
                    if (commande.getPaysLivraison() != null) {
                        existingCommande.setPaysLivraison(commande.getPaysLivraison());
                    }
                    if (commande.getCodePostalLivraison() != null) {
                        existingCommande.setCodePostalLivraison(commande.getCodePostalLivraison());
                    }
                    if (commande.getVilleLivraison() != null) {
                        existingCommande.setVilleLivraison(commande.getVilleLivraison());
                    }
                    if (commande.getRueLivraison() != null) {
                        existingCommande.setRueLivraison(commande.getRueLivraison());
                    }
                    if (commande.getNomLivraison() != null) {
                        existingCommande.setNomLivraison(commande.getNomLivraison());
                    }
                    if (commande.getPaysFacturation() != null) {
                        existingCommande.setPaysFacturation(commande.getPaysFacturation());
                    }
                    if (commande.getCodePostalFacturation() != null) {
                        existingCommande.setCodePostalFacturation(commande.getCodePostalFacturation());
                    }
                    if (commande.getVilleFacturation() != null) {
                        existingCommande.setVilleFacturation(commande.getVilleFacturation());
                    }
                    if (commande.getRueFacturation() != null) {
                        existingCommande.setRueFacturation(commande.getRueFacturation());
                    }
                    if (commande.getNomFacturation() != null) {
                        existingCommande.setNomFacturation(commande.getNomFacturation());
                    }
                    if (commande.getPayee() != null) {
                        existingCommande.setPayee(commande.getPayee());
                    }

                    return existingCommande;
                }
            )
            .map(commandeRepository::save)
            .map(
                savedCommande -> {
                    commandeSearchRepository.save(savedCommande);

                    return savedCommande;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Commande> findAll(Pageable pageable) {
        log.debug("Request to get all Commandes");
        return commandeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commande> findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        return commandeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        commandeRepository.deleteById(id);
        commandeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Commande> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Commandes for query {}", query);
        return commandeSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    @Transactional()
    public Commande newCommande(LigneCommande ligneCommande) {
        Commande commande = new Commande();
        commande.addLigneCommande(ligneCommande);
        ligneCommande.setCommande(commande);
        Commande result = commandeRepository.save(commande);
        ligneCommandeRepository.save(ligneCommande);
        return result;
    }

    @Override
    @Transactional()
    public Commande ajouterLigne(Long id, LigneCommande ligneCommande) {
        Commande commande = commandeRepository.getOne(id);
        commande.addLigneCommande(ligneCommande);
        ligneCommande.setCommande(commande); // todo remove (implicit in addLigne commande)
        // todo verify if not already added
        Commande result = commandeRepository.save(commande);
        ligneCommandeRepository.save(ligneCommande);
        return result;
    }

    @Override
    @Transactional()
    public Commande SupprimerLigne(Long idCommande, Long idLigne) {
        Commande commande = commandeRepository.getOne(idCommande);
        LigneCommande ligneCommande = ligneCommandeRepository.getOne(idLigne);
        commande.removeLigneCommande(ligneCommande);
        ligneCommandeRepository.delete(ligneCommande);
        // todo verify if not another commande
        return commandeRepository.save(commande);
    }


    // todo faire la difference entre ajouter et modifier !! (ajout = modifier avec ++)
    // verifier si il ne decommande pas des livre non précedemment commandé
    public Commande modifierLigneCommande(Long idCommande, Long idLivre, int quantite) {
        Optional<Commande> opt = commandeRepository.findById(idCommande);
        if (opt.isEmpty()) {
            return null;
        }
        Commande commande = opt.get();
        boolean newLivre = true;
        for (LigneCommande ligne : commande.getLigneCommandes()) {
            if (ligne.getLivre().getId() == idLivre) {
                newLivre = false;
                if (ligne.getQuantite() != quantite) {
                    if(livreService.reserver(idLivre, quantite - ligne.getQuantite()) != null){
                        ligne.updateQuantite(quantite);
                        ligneCommandeRepository.save(ligne);
                    }
                }
            }
        }
        if (newLivre) { // creation d'une nouvelle ligneCommande
            LigneCommande ligneCommande = new LigneCommande();
            Livre livre = livreService.reserver(idLivre, quantite);
            if(livre != null){
                ligneCommande.setLivreQuantite(livre, quantite);
                commande.addLigneCommande(ligneCommande);
                ligneCommandeRepository.save(ligneCommande);
            }
        }
        return commandeRepository.save(commande);
    }

    public Page<Commande> getHistory(Pageable pageable) {
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        //System.out.print("\n\nHere is the login : " + userLogin + "\n\n\n");
        Optional<Utilisateur> utilisateur = utilisateurService.utilisateurFromLogin(userLogin);
        System.out.println("\n\nHERE IS THE USER ID " + utilisateur.get().getId().toString() + "\n\n\n");
        return commandeRepository.getHistory(utilisateur.get().getId(), pageable);
    }

    public boolean commander(Commande commande) {
        // todo check for null fields
        commande.setPayee(true);
        commandeRepository.save(commande);
        return true;
    }
}
