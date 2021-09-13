package fr.dranse.myapp.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.dranse.myapp.IntegrationTest;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.service.LivreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the LivreControllerResource REST controller.
 *
 * @see LivreControllerResource
 */
@IntegrationTest
class LivreControllerResourceIT {

    private MockMvc restMockMvc;



    /**
     * Test creationLivre
     */
    @Test
    void testCreationLivre() throws Exception {
        restMockMvc.perform(post("/api/livre-controller/creation-livre")).andExpect(status().isOk());
    }

    /**
     * Test modifInfoLivre
     */
    @Test
    void testModifInfoLivre() throws Exception {
        restMockMvc.perform(put("/api/livre-controller/modif-info-livre")).andExpect(status().isOk());
    }

    /**
     * Test rechercheParAuteur
     */
    @Test
    void testRechercheParAuteur() throws Exception {
        restMockMvc.perform(get("/api/livre-controller/recherche-par-auteur")).andExpect(status().isOk());
    }

    /**
     * Test rechercheParTitre
     */
    @Test
    void testRechercheParTitre() throws Exception {
        restMockMvc.perform(get("/api/livre-controller/recherche-par-titre")).andExpect(status().isOk());
    }

    /**
     * Test rechercheParCategorie
     */
    @Test
    void testRechercheParCategorie() throws Exception {
        restMockMvc.perform(get("/api/livre-controller/recherche-par-categorie")).andExpect(status().isOk());
    }

    /**
     * Test getBestseller
     */
    @Test
    void testGetBestseller() throws Exception {
        restMockMvc.perform(get("/api/livre-controller/get-bestseller")).andExpect(status().isOk());
    }

    /**
     * Test rechercheDynamique
     */
    @Test
    void testRechercheDynamique() throws Exception {
        restMockMvc.perform(get("/api/livre-controller/recherche-dynamique")).andExpect(status().isOk());
    }

    /**
     * Test getLivre
     */
    @Test
    void testGetLivre() throws Exception {
        restMockMvc.perform(get("/api/livre-controller/get-livre")).andExpect(status().isOk());
    }

    /**
     * Test deleteLivre
     */
    @Test
    void testDeleteLivre() throws Exception {
        restMockMvc.perform(delete("/api/livre-controller/delete-livre")).andExpect(status().isOk());
    }
}
