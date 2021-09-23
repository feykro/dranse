package fr.dranse.myapp.repository.search;

import fr.dranse.myapp.domain.Livre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Livre} entity.
 */
public interface LivreSearchRepository extends ElasticsearchRepository<Livre, Long> {
    @Query("{\"match\" : {\"titre\" : {\"query\" : \"?0\", \"fuzziness\": \"2\"}}}")
    Page<SearchHit<Livre>> searchByTitle(String titre, Pageable pageable);

    @Query("{\"multi_match\" : {\"query\" : \"?0\", \"fields\": [ \"titre\", \"auteur\" ], \"fuzziness\": \"AUTO\"}}")
    Page<SearchHit<Livre>> globalSearch(String value, Pageable pageable);

    @Query("{\"fuzzy\" : {\"auteur\" : {\"value\" : \"?0\", \"fuzziness\": \"AUTO\"}}}")
    Page<SearchHit<Livre>> searchByAuthor(String auteur, Pageable pageable);
}
