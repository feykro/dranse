package fr.dranse.myapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.dranse.myapp.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the CommandeControllerResource REST controller.
 *
 * @see CommandeControllerResource
 */
@IntegrationTest
class CommandeControllerResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        /*   MockitoAnnotations.initMocks(this);

        CommandeControllerResource commandeControllerResource = new CommandeControllerResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(commandeControllerResource).build();*/
    }

    /**
     * Test creationCommande
     */
    @Test
    void testCreationCommande() throws Exception {
        restMockMvc.perform(post("/api/commande-controller/creation-commande")).andExpect(status().isOk());
    }

    /**
     * Test ajoutLigne
     */
    @Test
    void testAjoutLigne() throws Exception {
        restMockMvc.perform(put("/api/commande-controller/ajout-ligne")).andExpect(status().isOk());
    }

    /**
     * Test passerCommande
     */
    @Test
    void testPasserCommande() throws Exception {
        restMockMvc.perform(put("/api/commande-controller/passer-commande")).andExpect(status().isOk());
    }

    /**
     * Test getCommande
     */
    @Test
    void testGetCommande() throws Exception {
        restMockMvc.perform(get("/api/commande-controller/get-commande")).andExpect(status().isOk());
    }

    /**
     * Test deleteCommande
     */
    @Test
    void testDeleteCommande() throws Exception {
        restMockMvc.perform(delete("/api/commande-controller/delete-commande")).andExpect(status().isOk());
    }

    /**
     * Test deleteLigne
     */
    @Test
    void testDeleteLigne() throws Exception {
        restMockMvc.perform(delete("/api/commande-controller/delete-ligne")).andExpect(status().isOk());
    }
}
