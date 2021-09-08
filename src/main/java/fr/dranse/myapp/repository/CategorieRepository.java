package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.Categorie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Categorie entity.
 */
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    @Query(
        value = "select distinct categorie from Categorie categorie left join fetch categorie.livres",
        countQuery = "select count(distinct categorie) from Categorie categorie"
    )
    Page<Categorie> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct categorie from Categorie categorie left join fetch categorie.livres")
    List<Categorie> findAllWithEagerRelationships();

    @Query("select categorie from Categorie categorie left join fetch categorie.livres where categorie.id =:id")
    Optional<Categorie> findOneWithEagerRelationships(@Param("id") Long id);
}
