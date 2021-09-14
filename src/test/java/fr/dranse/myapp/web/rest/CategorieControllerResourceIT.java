package fr.dranse.myapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.dranse.myapp.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the CategorieControllerResource REST controller.
 *
 * @see CategorieControllerResource
 */
@IntegrationTest
class CategorieControllerResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        /*MockitoAnnotations.initMocks(this);

        CategorieControllerResource categorieControllerResource = new CategorieControllerResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(categorieControllerResource).build();*/
    }

    /**
     * Test getCategories
     */
    @Test
    void testGetCategories() throws Exception {
        restMockMvc.perform(get("/api/categorie-controller/get-categories")).andExpect(status().isOk());
    }
}
