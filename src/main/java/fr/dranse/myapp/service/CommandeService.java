package fr.dranse.myapp.service;

import fr.dranse.myapp.domain.Commande;
import fr.dranse.myapp.domain.LigneCommande;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Commande}.
 */
public interface CommandeService {
    /**
     * Save a commande.
     *
     * @param commande the entity to save.
     * @return the persisted entity.
     */
    Commande save(Commande commande);

    /**
     * Partially updates a commande.
     *
     * @param commande the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Commande> partialUpdate(Commande commande);

    /**
     * Get all the commandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Commande> findAll(Pageable pageable);

    /**
     * Get the "id" commande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Commande> findOne(Long id);

    /**
     * Delete the "id" commande.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the commande corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Commande> search(String query, Pageable pageable);

    /**
     * Créer une nouvelle commande contenant ligneCommande
     *
     * @param ligneCommande
     * @return
     */
    Commande newCommande(LigneCommande ligneCommande);

    /**
     * Ajouter un ligneCommande à une commande existante
     *
     * @param id
     * @param ligneCommande
     * @return
     */
    Commande ajouterLigne(Long id, LigneCommande ligneCommande);

    /**
     * Supprime une ligneCommande d'une commande
     *
     * @param idCommande
     * @param idLigne
     * @return
     */
    Commande SupprimerLigne(Long idCommande, Long idLigne);
    // todo modifier le nombre d'items dans une ligneCommande


    /**
     *  Modifier une ligne commande appartenant à commande
     * @param idCommande
     * @param idLivre
     * @param quantite
     * @return
     */
    Commande modifierLigneCommande(Long idCommande, Long idLivre, int quantite);
    Commande ajouterLigneCommande(Long idCommande, Long idLivre, int quantite);

    /**
     * return the history of commands of an user
     * @param id
     * @param pageable
     * @return
     */
    Page<Commande> getHistory(Long id, Pageable pageable);

    /**
     * Valider et passer une commande.
     * @param commande
     * @return
     */
    boolean commander(Commande commande);
}
