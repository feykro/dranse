package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.Avis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Avis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {}
