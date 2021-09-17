package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.Categorie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Categorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    @Query("select distinct categorie from Categorie categorie order by categorie.id")
    List<Categorie> getMostPopular(Pageable pageable);
    //todo : manu
}
