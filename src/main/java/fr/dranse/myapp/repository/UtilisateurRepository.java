package fr.dranse.myapp.repository;

import fr.dranse.myapp.domain.Utilisateur;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    @Query("select u from Utilisateur u where u.userP = (select userp from User userp where userp.login =:login)")
    Optional<Utilisateur> utilisateurFromLogin(@Param("login") String login);
}
