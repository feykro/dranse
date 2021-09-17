package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Utilisateur;
import fr.dranse.myapp.repository.UtilisateurRepository;
import fr.dranse.myapp.repository.search.UtilisateurSearchRepository;
import fr.dranse.myapp.service.UtilisateurService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurServiceImpl.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurSearchRepository utilisateurSearchRepository;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, UtilisateurSearchRepository utilisateurSearchRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurSearchRepository = utilisateurSearchRepository;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        log.debug("Request to save Utilisateur : {}", utilisateur);
        System.out.println("\n\nIn service.save : " + utilisateur.toString() + "\n\n");
        Utilisateur result = utilisateurRepository.save(utilisateur);
        utilisateurSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Utilisateur> partialUpdate(Utilisateur utilisateur) {
        log.debug("Request to partially update Utilisateur : {}", utilisateur);

        return utilisateurRepository
            .findById(utilisateur.getId())
            .map(
                existingUtilisateur -> {
                    if (utilisateur.getAdrRue() != null) {
                        existingUtilisateur.setAdrRue(utilisateur.getAdrRue());
                    }
                    if (utilisateur.getAdrCodePostal() != null) {
                        existingUtilisateur.setAdrCodePostal(utilisateur.getAdrCodePostal());
                    }
                    if (utilisateur.getAdrPays() != null) {
                        existingUtilisateur.setAdrPays(utilisateur.getAdrPays());
                    }
                    if (utilisateur.getAdrVille() != null) {
                        existingUtilisateur.setAdrVille(utilisateur.getAdrVille());
                    }
                    if (utilisateur.getTelephone() != null) {
                        existingUtilisateur.setTelephone(utilisateur.getTelephone());
                    }
                    if (utilisateur.getNumCB() != null) {
                        existingUtilisateur.setNumCB(utilisateur.getNumCB());
                    }

                    return existingUtilisateur;
                }
            )
            .map(utilisateurRepository::save)
            .map(
                savedUtilisateur -> {
                    utilisateurSearchRepository.save(savedUtilisateur);

                    return savedUtilisateur;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Utilisateur> findAll(Pageable pageable) {
        log.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findOne(Long id) {
        log.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
        utilisateurSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Utilisateur> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Utilisateurs for query {}", query);
        return utilisateurSearchRepository.search(queryStringQuery(query), pageable);
    }
}
