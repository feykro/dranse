package fr.dranse.myapp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CommandeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CommandeSearchRepositoryMockConfiguration {

    @MockBean
    private CommandeSearchRepository mockCommandeSearchRepository;
}
