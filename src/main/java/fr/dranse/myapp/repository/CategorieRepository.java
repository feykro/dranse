package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.Categorie;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

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
