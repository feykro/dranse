package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Commande;
import fr.dranse.myapp.repository.CommandeRepository;
import fr.dranse.myapp.repository.search.CommandeSearchRepository;
import fr.dranse.myapp.service.CommandeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public CommandeServiceImpl(CommandeRepository commandeRepository, CommandeSearchRepository commandeSearchRepository) {
        this.commandeRepository = commandeRepository;
        this.commandeSearchRepository = commandeSearchRepository;
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
}
