package fr.dranse.myapp.repository.search;

import fr.dranse.myapp.domain.Livre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Livre} entity.
 */
public interface LivreSearchRepository extends ElasticsearchRepository<Livre, Long> {

    @Query("{\"fuzzy\" : {\"titre\" : {\"value\" : \"?0\", \"fuzziness\": \"AUTO\"}}}")
    Page<Livre> searchByTitle(String titre, Pageable pageable);
}
