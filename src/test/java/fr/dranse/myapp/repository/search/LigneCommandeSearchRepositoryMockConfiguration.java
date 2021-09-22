package fr.dranse.myapp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link LigneCommandeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LigneCommandeSearchRepositoryMockConfiguration {

    @MockBean
    private LigneCommandeSearchRepository mockLigneCommandeSearchRepository;
}
