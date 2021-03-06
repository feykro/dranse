package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Categorie;
import fr.dranse.myapp.domain.Livre;
import fr.dranse.myapp.repository.CategorieRepository;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.repository.search.CategorieSearchRepository;
import fr.dranse.myapp.service.CategorieService;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categorie}.
 */
@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieServiceImpl.class);

    private final CategorieRepository categorieRepository;

    private final CategorieSearchRepository categorieSearchRepository;

    private final LivreRepository livreRepository;

    public CategorieServiceImpl(CategorieRepository categorieRepository, CategorieSearchRepository categorieSearchRepository, LivreRepository livreRepository) {
        this.categorieRepository = categorieRepository;
        this.categorieSearchRepository = categorieSearchRepository;
        this.livreRepository = livreRepository;
    }

    @Override
    public Categorie save(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        // todo: optimize this (Hugo)
        Set<Livre> bufferSet = new HashSet<Livre>();
        for (Livre livre : categorie.getLivres()){
            Livre currentLivre = livreRepository.getOne(livre.getId());
            bufferSet.add(currentLivre);
        }
        categorie.getLivres().clear();

        for (Livre l : bufferSet) {
                categorie.addLivre(l);
        }

        Categorie result = categorieRepository.save(categorie);
        // todo : figure out why the following line produces stackoverflow
        //categorieSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Categorie> partialUpdate(Categorie categorie) {
        log.debug("Request to partially update Categorie : {}", categorie);

        return categorieRepository
            .findById(categorie.getId())
            .map(
                existingCategorie -> {
                    if (categorie.getNom() != null) {
                        existingCategorie.setNom(categorie.getNom());
                    }
                    if (categorie.getDescription() != null) {
                        existingCategorie.setDescription(categorie.getDescription());
                    }

                    return existingCategorie;
                }
            )
            .map(categorieRepository::save)
            .map(
                savedCategorie -> {
                    categorieSearchRepository.save(savedCategorie);

                    return savedCategorie;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categorie> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categorieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categorie> findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        return categorieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);
        categorieRepository.deleteById(id);
        categorieSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categorie> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Categories for query {}", query);
        return categorieSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<Categorie> getMostPopular() {
        log.debug("Request to get most popular Categories");
        return categorieRepository.getMostPopular(PageRequest.of(0, 15));
    }
}
