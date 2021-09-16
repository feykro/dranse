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
 * Test class for the UtilisateurControllerResource REST controller.
 *
 * @see UtilisateurControllerResource
 */
@IntegrationTest
class UtilisateurControllerResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        UtilisateurControllerResource utilisateurControllerResource = new UtilisateurControllerResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(utilisateurControllerResource).build();
    }

    /**
     * Test infoUtilisateur
     */
    @Test
    void testInfoUtilisateur() throws Exception {
        restMockMvc.perform(post("/api/utilisateur-controller/info-utilisateur")).andExpect(status().isOk());
    }

    /**
     * Test modifMDP
     */
    @Test
    void testModifMDP() throws Exception {
        restMockMvc.perform(put("/api/utilisateur-controller/modif-mdp")).andExpect(status().isOk());
    }

    /**
     * Test modifInfo
     */
    @Test
    void testModifInfo() throws Exception {
        restMockMvc.perform(put("/api/utilisateur-controller/modif-info")).andExpect(status().isOk());
    }

    /**
     * Test listeUtilisateurs
     */
    @Test
    void testListeUtilisateurs() throws Exception {
        restMockMvc.perform(get("/api/utilisateur-controller/liste-utilisateurs")).andExpect(status().isOk());
    }

    /**
     * Test lectureHistorique
     */
    @Test
    void testLectureHistorique() throws Exception {
        restMockMvc.perform(get("/api/utilisateur-controller/lecture-historique")).andExpect(status().isOk());
    }

    /**
     * Test lectureInfoLivraison
     */
    @Test
    void testLectureInfoLivraison() throws Exception {
        restMockMvc.perform(get("/api/utilisateur-controller/lecture-info-livraison")).andExpect(status().isOk());
    }

    /**
     * Test lectureInfoCompte
     */
    @Test
    void testLectureInfoCompte() throws Exception {
        restMockMvc.perform(get("/api/utilisateur-controller/lecture-info-compte")).andExpect(status().isOk());
    }

    /**
     * Test deleteUtilisateur
     */
    @Test
    void testDeleteUtilisateur() throws Exception {
        restMockMvc.perform(delete("/api/utilisateur-controller/delete-utilisateur")).andExpect(status().isOk());
    }
}
