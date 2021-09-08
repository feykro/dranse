package fr.dranse.myapp.service;

import fr.dranse.myapp.domain.Categorie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Categorie}.
 */
public interface CategorieService {
    /**
     * Save a categorie.
     *
     * @param categorie the entity to save.
     * @return the persisted entity.
     */
    Categorie save(Categorie categorie);

    /**
     * Partially updates a categorie.
     *
     * @param categorie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Categorie> partialUpdate(Categorie categorie);

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Categorie> findAll(Pageable pageable);

    /**
     * Get all the categories with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Categorie> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" categorie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Categorie> findOne(Long id);

    /**
     * Delete the "id" categorie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the categorie corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Categorie> search(String query, Pageable pageable);
}
