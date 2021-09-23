package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Categorie;
import fr.dranse.myapp.domain.Livre;
import fr.dranse.myapp.repository.CategorieRepository;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.repository.search.LivreSearchRepository;
import fr.dranse.myapp.service.LivreService;
import java.util.*;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Livre}.
 */
@Service
@Transactional
public class LivreServiceImpl implements LivreService {

    private final Logger log = LoggerFactory.getLogger(LivreServiceImpl.class);

    private final LivreRepository livreRepository;

    private final LivreSearchRepository livreSearchRepository;

    private final CategorieRepository categorieRepository;

    public LivreServiceImpl(
        LivreRepository livreRepository,
        LivreSearchRepository livreSearchRepository,
        CategorieRepository categorieRepository
    ) {
        this.livreRepository = livreRepository;
        this.livreSearchRepository = livreSearchRepository;
        this.categorieRepository = categorieRepository;
    }

    @Override
    public Livre save(Livre livre) {
        log.debug("Request to save Livre : {}", livre);
        Set<Categorie> bufferSet = new HashSet<>();
        // todo: add robustness (don't allow cats that don't exist)
        for (Categorie cat : livre.getLivre_cats()) {
            Categorie currentCat = categorieRepository.getOne(cat.getId());
            bufferSet.add(currentCat);
        }
        livre.getLivre_cats().clear();
        for (Categorie cat : bufferSet) {
            livre.addLivre_cat(cat);
        }
        Livre result = livreRepository.save(livre);
        // todo: figure out why the following line results in stackoverflow
        //livreSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Livre> partialUpdate(Livre livre) {
        log.debug("Request to partially update Livre : {}", livre);

        return livreRepository
            .findById(livre.getId())
            .map(
                existingLivre -> {
                    if (livre.getTitre() != null) {
                        existingLivre.setTitre(livre.getTitre());
                    }
                    if (livre.getAuteur() != null) {
                        existingLivre.setAuteur(livre.getAuteur());
                    }
                    if (livre.getPrix() != null) {
                        existingLivre.setPrix(livre.getPrix());
                    }
                    if (livre.getSynopsis() != null) {
                        existingLivre.setSynopsis(livre.getSynopsis());
                    }
                    if (livre.getEdition() != null) {
                        existingLivre.setEdition(livre.getEdition());
                    }
                    if (livre.getAnneePublication() != null) {
                        existingLivre.setAnneePublication(livre.getAnneePublication());
                    }
                    if (livre.getEditeur() != null) {
                        existingLivre.setEditeur(livre.getEditeur());
                    }
                    if (livre.getStock() != null) {
                        existingLivre.setStock(livre.getStock());
                    }
                    if (livre.getUrlImage() != null) {
                        existingLivre.setUrlImage(livre.getUrlImage());
                    }

                    return existingLivre;
                }
            )
            .map(livreRepository::save)
            .map(
                savedLivre -> {
                    livreSearchRepository.save(savedLivre);

                    return savedLivre;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livre> findAll(Pageable pageable) {
        log.debug("Request to get all Livres");
        return livreRepository.findAll(pageable);
    }

    public Page<Livre> findAllWithEagerRelationships(Pageable pageable) {
        return livreRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livre> findOne(Long id) {
        log.debug("Request to get Livre : {}", id);
        return livreRepository.findOneWithEagerRelationships(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livre> findByAuthor(Pageable pageable, String author) {
        log.debug("Request to get Livre by author {}", author);
        Page<SearchHit<Livre>> res = livreSearchRepository.searchByAuthor(author, pageable);
        return res.map(sh -> sh.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livre> findByTitle(Pageable pageable, String title) {
        log.debug("Request to get Livre by title {}", title);
        Page<SearchHit<Livre>> res = livreSearchRepository.searchByTitle(title, pageable);
        return res.map(sh -> sh.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livre> findByCategorie(Pageable pageable, String categorie) {
        log.debug("Request to get Livre by categorie {}", categorie);
        return livreRepository.findAllWithCat(pageable, categorie);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Livre : {}", id);
        livreRepository.deleteById(id);
        livreSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Livre> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Livres for query {}", query);
        return livreSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Livre reserver(Long id, int quantite) {
        Optional<Livre> opt = livreRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        Livre livre = opt.get();
        if (livre.getStock() >= quantite) {
            livre.setStock(livre.getStock() - quantite);
            return livreRepository.save(livre);
        } else {
            return null;
        }
    }

    public List<Livre> getBestSeller() {
        return livreRepository.getBestSeller(PageRequest.of(0, 5));
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void elasticSync() {
        System.out.println("\n\nSync with elastic...");
        livreSearchRepository.saveAll(livreRepository.findAll().subList(0, 300));
        System.out.println("end...\n");
    }
}
