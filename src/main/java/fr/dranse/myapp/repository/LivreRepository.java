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
    @Query(
        value = "select distinct livre from Livre livre left join fetch livre.categories",
        countQuery = "select count(distinct livre) from Livre livre"
    )
    Page<Livre> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct livre from Livre livre left join fetch livre.categories")
    List<Livre> findAllWithEagerRelationships();

    @Query("select livre from Livre livre left join fetch livre.categories where livre.id =:id")
    Optional<Livre> findOneWithEagerRelationships(@Param("id") Long id);
}
