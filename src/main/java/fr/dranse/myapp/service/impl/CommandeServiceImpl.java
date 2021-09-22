package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Commande;
import fr.dranse.myapp.domain.LigneCommande;
import fr.dranse.myapp.domain.Livre;
import fr.dranse.myapp.repository.CommandeRepository;
import fr.dranse.myapp.repository.LigneCommandeRepository;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.repository.search.CommandeSearchRepository;
import fr.dranse.myapp.service.CommandeService;

import java.util.ArrayList;
import java.util.Optional;

import fr.dranse.myapp.service.LivreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Commande newCommande(Long idLivre, int quantite) {
        Commande commande = new Commande();
        commande = commandeRepository.save(commande);
        return ajouterLigneCommande(commande.getId(), idLivre, quantite);
    }

    @Override
    @Transactional()
    public Commande ajouterLigne(Long id, LigneCommande ligneCommande) {
        Commande commande = commandeRepository.getOne(id);
        commande.addLigneCommande(ligneCommande);
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


    public Commande ajouterLigneCommande(Long idCommande, Long idLivre, int quantite) {
        return modifierOuAjouterLigneCommande(idCommande, idLivre, quantite, true);
    }

    public Commande modifierLigneCommande(Long idCommande, Long idLivre, int quantite) {
        return modifierOuAjouterLigneCommande(idCommande, idLivre, quantite, false);
    }

    public Commande modifierOuAjouterLigneCommande(Long idCommande, Long idLivre, int quantite, boolean ajouter) {
        Optional<Commande> opt = commandeRepository.findById(idCommande);
        if (opt.isEmpty()) {
            return null;
        }
        Commande commande = opt.get();
        for (LigneCommande ligne : commande.getLigneCommandes()) {
            if (ligne.getLivre().getId() == idLivre) {
                if(ajouter){
                    if(livreService.reserver(idLivre, quantite) != null){
                        ligne.updateQuantite(quantite + ligne.getQuantite());
                        return commandeRepository.save(commande);
                    }else{
                        return null;
                    }
                }else{
                    if (ligne.getQuantite() != quantite) {
                        if(livreService.reserver(idLivre, quantite - ligne.getQuantite()) != null){
                            ligne.updateQuantite(quantite);
                            if(ligne.getQuantite() == 0){
                                commande.removeLigneCommande(ligne);
                                ligneCommandeRepository.delete(ligne);
                            }
                            return commandeRepository.save(commande);
                        }else{
                            return null;
                        }
                    }
                }
            }
        }
        // creation d'une nouvelle ligneCommande
        LigneCommande ligneCommande = new LigneCommande();
        Livre livre = livreService.reserver(idLivre, quantite);
        if(livre != null){
            ligneCommande.setLivreQuantite(livre, quantite);
            commande.addLigneCommande(ligneCommande);
            return commandeRepository.save(commande);
        }else{
            return null;
        }
    }

    public Page<Commande> getHistory(Long id, Pageable pageable) {
        return commandeRepository.getHistory(id, pageable);
    }

    public boolean commander(Commande commande) {
        Commande cmd = commandeRepository.getOne(commande.getId());
        cmd.setPayee(true);
        cmd.setUtilisateur(commande.getUtilisateur());
        cmd.setPaysLivraison(commande.getPaysLivraison());
        cmd.setCodePostalLivraison(commande.getCodePostalLivraison());
        cmd.setVilleLivraison(commande.getVilleLivraison());
        cmd.setRueLivraison(commande.getRueLivraison());
        cmd.setNomLivraison(commande.getNomLivraison());
        cmd.setPaysFacturation(commande.getPaysFacturation());
        cmd.setCodePostalFacturation(commande.getCodePostalFacturation());
        cmd.setVilleFacturation(commande.getVilleFacturation());
        cmd.setRueFacturation(commande.getRueFacturation());
        cmd.setNomFacturation(commande.getNomFacturation());
        commandeRepository.save(cmd);
        return true;
    }
}
