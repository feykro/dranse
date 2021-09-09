package fr.dranse.myapp.repository.search;

import fr.dranse.myapp.domain.LigneCommande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link LigneCommande} entity.
 */
public interface LigneCommandeSearchRepository extends ElasticsearchRepository<LigneCommande, Long> {}
