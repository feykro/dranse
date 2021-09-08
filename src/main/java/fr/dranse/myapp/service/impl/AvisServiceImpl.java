package fr.dranse.myapp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import fr.dranse.myapp.domain.Avis;
import fr.dranse.myapp.repository.AvisRepository;
import fr.dranse.myapp.repository.search.AvisSearchRepository;
import fr.dranse.myapp.service.AvisService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Avis}.
 */
@Service
@Transactional
public class AvisServiceImpl implements AvisService {

    private final Logger log = LoggerFactory.getLogger(AvisServiceImpl.class);

    private final AvisRepository avisRepository;

    private final AvisSearchRepository avisSearchRepository;

    public AvisServiceImpl(AvisRepository avisRepository, AvisSearchRepository avisSearchRepository) {
        this.avisRepository = avisRepository;
        this.avisSearchRepository = avisSearchRepository;
    }

    @Override
    public Avis save(Avis avis) {
        log.debug("Request to save Avis : {}", avis);
        Avis result = avisRepository.save(avis);
        avisSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Avis> partialUpdate(Avis avis) {
        log.debug("Request to partially update Avis : {}", avis);

        return avisRepository
            .findById(avis.getId())
            .map(
                existingAvis -> {
                    if (avis.getNote() != null) {
                        existingAvis.setNote(avis.getNote());
                    }
                    if (avis.getCommentaire() != null) {
                        existingAvis.setCommentaire(avis.getCommentaire());
                    }
                    if (avis.getDatePublication() != null) {
                        existingAvis.setDatePublication(avis.getDatePublication());
                    }
                    if (avis.getAffiche() != null) {
                        existingAvis.setAffiche(avis.getAffiche());
                    }

                    return existingAvis;
                }
            )
            .map(avisRepository::save)
            .map(
                savedAvis -> {
                    avisSearchRepository.save(savedAvis);

                    return savedAvis;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Avis> findAll(Pageable pageable) {
        log.debug("Request to get all Avis");
        return avisRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Avis> findOne(Long id) {
        log.debug("Request to get Avis : {}", id);
        return avisRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Avis : {}", id);
        avisRepository.deleteById(id);
        avisSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Avis> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Avis for query {}", query);
        return avisSearchRepository.search(queryStringQuery(query), pageable);
    }
}
