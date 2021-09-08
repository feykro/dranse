package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.LigneCommande;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LigneCommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {}
