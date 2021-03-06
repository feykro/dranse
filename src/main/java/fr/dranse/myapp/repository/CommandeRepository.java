package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.Commande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Commande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    @Query(
        value = "select distinct commande from Commande commande where commande.utilisateur =:id",
        countQuery = "select count(distinct categorie) from Categorie categorie"
    )
    Page<Commande> getHistory(@Param("id") Long id, Pageable pageable);
}
