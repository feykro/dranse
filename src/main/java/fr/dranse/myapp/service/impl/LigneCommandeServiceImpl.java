package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.LigneCommande;
import fr.dranse.myapp.repository.LigneCommandeRepository;
import fr.dranse.myapp.repository.search.LigneCommandeSearchRepository;
import fr.dranse.myapp.service.LigneCommandeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LigneCommande}.
 */
@Service
@Transactional
public class LigneCommandeServiceImpl implements LigneCommandeService {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeServiceImpl.class);

    private final LigneCommandeRepository ligneCommandeRepository;

    private final LigneCommandeSearchRepository ligneCommandeSearchRepository;

    public LigneCommandeServiceImpl(
        LigneCommandeRepository ligneCommandeRepository,
        LigneCommandeSearchRepository ligneCommandeSearchRepository
    ) {
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.ligneCommandeSearchRepository = ligneCommandeSearchRepository;
    }

    @Override
    public LigneCommande save(LigneCommande ligneCommande) {
        log.debug("Request to save LigneCommande : {}", ligneCommande);
        LigneCommande result = ligneCommandeRepository.save(ligneCommande);
        ligneCommandeSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<LigneCommande> partialUpdate(LigneCommande ligneCommande) {
        log.debug("Request to partially update LigneCommande : {}", ligneCommande);

        return ligneCommandeRepository
            .findById(ligneCommande.getId())
            .map(
                existingLigneCommande -> {
                    if (ligneCommande.getQuantite() != null) {
                        existingLigneCommande.setQuantite(ligneCommande.getQuantite());
                    }
                    if (ligneCommande.getPrixPaye() != null) {
                        existingLigneCommande.setPrixPaye(ligneCommande.getPrixPaye());
                    }

                    return existingLigneCommande;
                }
            )
            .map(ligneCommandeRepository::save)
            .map(
                savedLigneCommande -> {
                    ligneCommandeSearchRepository.save(savedLigneCommande);

                    return savedLigneCommande;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LigneCommande> findAll(Pageable pageable) {
        log.debug("Request to get all LigneCommandes");
        return ligneCommandeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LigneCommande> findOne(Long id) {
        log.debug("Request to get LigneCommande : {}", id);
        return ligneCommandeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LigneCommande : {}", id);
        ligneCommandeRepository.deleteById(id);
        ligneCommandeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LigneCommande> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneCommandes for query {}", query);
        return ligneCommandeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
