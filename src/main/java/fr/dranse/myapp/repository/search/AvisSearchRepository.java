package fr.dranse.myapp.repository.search;

import fr.dranse.myapp.domain.Avis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Avis} entity.
 */
public interface AvisSearchRepository extends ElasticsearchRepository<Avis, Long> {}
