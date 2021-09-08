package fr.dranse.myapp.service;

import fr.dranse.myapp.domain.LigneCommande;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LigneCommande}.
 */
public interface LigneCommandeService {
    /**
     * Save a ligneCommande.
     *
     * @param ligneCommande the entity to save.
     * @return the persisted entity.
     */
    LigneCommande save(LigneCommande ligneCommande);

    /**
     * Partially updates a ligneCommande.
     *
     * @param ligneCommande the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LigneCommande> partialUpdate(LigneCommande ligneCommande);

    /**
     * Get all the ligneCommandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LigneCommande> findAll(Pageable pageable);

    /**
     * Get the "id" ligneCommande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LigneCommande> findOne(Long id);

    /**
     * Delete the "id" ligneCommande.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ligneCommande corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LigneCommande> search(String query, Pageable pageable);
}
