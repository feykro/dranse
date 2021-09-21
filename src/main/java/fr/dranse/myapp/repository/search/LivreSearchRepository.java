package fr.dranse.myapp.repository.search;

import fr.dranse.myapp.domain.Livre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Livre} entity.
 */
public interface LivreSearchRepository extends ElasticsearchRepository<Livre, Long> {}
