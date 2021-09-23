package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.Livre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Livre entity.
 */
@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
    // additions
    //@Query("SELECT l from Livre l where l.id in (SELECT cat.livre_id from Categorie cat where cat.livre_id =:categorie)")
    //Page<Livre> findAllWithCategorie(Pageable pageable, @Param("categorie") String titre);

    @Query("SELECT l from Livre l where l.titre =:titre")
    Page<Livre> findAllWithTitle(Pageable pageable, @Param("titre") String titre);

    @Query("SELECT l from Livre l where l.auteur =:auteur")
    Page<Livre> findAllWithAuthor(Pageable pageable, @Param("auteur") String author);

    @Query(
        value = "select distinct livre from Livre livre left join fetch livre.livre_cats",
        countQuery = "select count(distinct livre) from Livre livre"
    )
    Page<Livre> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct livre from Livre livre left join fetch livre.livre_cats")
    List<Livre> findAllWithEagerRelationships();

    @Query("select livre from Livre livre left join fetch livre.livre_cats where livre.id =:id")
    Optional<Livre> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct livre from Livre livre join livre.livre_cats cat where cat.nom =:categorie")
    Page<Livre> findAllWithCat(Pageable pageable, @Param("categorie") String categorie);

    @Query("SELECT l, SUM(lc.quantite) as total "//
        + " FROM LigneCommande lc INNER JOIN lc.livre l" //
        + " GROUP BY l.id" //
        + " ORDER BY total DESC")
    List<Livre> getBestSeller(Pageable pageable);
}
