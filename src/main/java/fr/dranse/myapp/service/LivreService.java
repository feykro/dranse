package fr.dranse.myapp.service;

import fr.dranse.myapp.domain.Livre;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Livre}.
 */
public interface LivreService {
    /**
     * Save a livre.
     *
     * @param livre the entity to save.
     * @return the persisted entity.
     */
    Livre save(Livre livre);

    /**
     * Partially updates a livre.
     *
     * @param livre the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Livre> partialUpdate(Livre livre);

    /**
     * Get all the livres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Livre> findAll(Pageable pageable);

    /**
     * Get all the livres with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Livre> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" livre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Livre> findOne(Long id);

    Page<Livre> findByAuthor(Pageable pageable, String author);
    Page<Livre> findByTitle(Pageable pageable, String title);
    Page<Livre> findByCategorie(Pageable pageable, String Categorie);

    /**
     * Delete the "id" livre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the livre corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Livre> search(String query, Pageable pageable);

    Livre reserver(Long id, int quantite);
}
