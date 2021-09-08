package fr.dranse.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.dranse.myapp.IntegrationTest;
import fr.dranse.myapp.domain.Utilisateur;
import fr.dranse.myapp.repository.UtilisateurRepository;
import fr.dranse.myapp.repository.search.UtilisateurSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UtilisateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UtilisateurResourceIT {

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADR_RUE = "AAAAAAAAAA";
    private static final String UPDATED_ADR_RUE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ADR_CODE_POSTAL = 1;
    private static final Integer UPDATED_ADR_CODE_POSTAL = 2;

    private static final String DEFAULT_ADR_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_ADR_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_ADR_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_ADR_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_CB = "AAAAAAAAAA";
    private static final String UPDATED_NUM_CB = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/utilisateurs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * This repository is mocked in the fr.dranse.myapp.repository.search test package.
     *
     * @see fr.dranse.myapp.repository.search.UtilisateurSearchRepositoryMockConfiguration
     */
    @Autowired
    private UtilisateurSearchRepository mockUtilisateurSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
            .mail(DEFAULT_MAIL)
            .motDePasse(DEFAULT_MOT_DE_PASSE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .adrRue(DEFAULT_ADR_RUE)
            .adrCodePostal(DEFAULT_ADR_CODE_POSTAL)
            .adrPays(DEFAULT_ADR_PAYS)
            .adrVille(DEFAULT_ADR_VILLE)
            .telephone(DEFAULT_TELEPHONE)
            .numCB(DEFAULT_NUM_CB);
        return utilisateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createUpdatedEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
            .mail(UPDATED_MAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .adrRue(UPDATED_ADR_RUE)
            .adrCodePostal(UPDATED_ADR_CODE_POSTAL)
            .adrPays(UPDATED_ADR_PAYS)
            .adrVille(UPDATED_ADR_VILLE)
            .telephone(UPDATED_TELEPHONE)
            .numCB(UPDATED_NUM_CB);
        return utilisateur;
    }

    @BeforeEach
    public void initTest() {
        utilisateur = createEntity(em);
    }

    @Test
    @Transactional
    void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();
        // Create the Utilisateur
        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisateur)))
            .andExpect(status().isCreated());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testUtilisateur.getMotDePasse()).isEqualTo(DEFAULT_MOT_DE_PASSE);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUtilisateur.getAdrRue()).isEqualTo(DEFAULT_ADR_RUE);
        assertThat(testUtilisateur.getAdrCodePostal()).isEqualTo(DEFAULT_ADR_CODE_POSTAL);
        assertThat(testUtilisateur.getAdrPays()).isEqualTo(DEFAULT_ADR_PAYS);
        assertThat(testUtilisateur.getAdrVille()).isEqualTo(DEFAULT_ADR_VILLE);
        assertThat(testUtilisateur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testUtilisateur.getNumCB()).isEqualTo(DEFAULT_NUM_CB);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(1)).save(testUtilisateur);
    }

    @Test
    @Transactional
    void createUtilisateurWithExistingId() throws Exception {
        // Create the Utilisateur with an existing ID
        utilisateur.setId(1L);

        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisateur)))
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeCreate);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(0)).save(utilisateur);
    }

    @Test
    @Transactional
    void getAllUtilisateurs() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].adrRue").value(hasItem(DEFAULT_ADR_RUE)))
            .andExpect(jsonPath("$.[*].adrCodePostal").value(hasItem(DEFAULT_ADR_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].adrPays").value(hasItem(DEFAULT_ADR_PAYS)))
            .andExpect(jsonPath("$.[*].adrVille").value(hasItem(DEFAULT_ADR_VILLE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].numCB").value(hasItem(DEFAULT_NUM_CB)));
    }

    @Test
    @Transactional
    void getUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.motDePasse").value(DEFAULT_MOT_DE_PASSE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.adrRue").value(DEFAULT_ADR_RUE))
            .andExpect(jsonPath("$.adrCodePostal").value(DEFAULT_ADR_CODE_POSTAL))
            .andExpect(jsonPath("$.adrPays").value(DEFAULT_ADR_PAYS))
            .andExpect(jsonPath("$.adrVille").value(DEFAULT_ADR_VILLE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.numCB").value(DEFAULT_NUM_CB));
    }

    @Test
    @Transactional
    void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(utilisateur.getId()).get();
        // Disconnect from session so that the updates on updatedUtilisateur are not directly saved in db
        em.detach(updatedUtilisateur);
        updatedUtilisateur
            .mail(UPDATED_MAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .adrRue(UPDATED_ADR_RUE)
            .adrCodePostal(UPDATED_ADR_CODE_POSTAL)
            .adrPays(UPDATED_ADR_PAYS)
            .adrVille(UPDATED_ADR_VILLE)
            .telephone(UPDATED_TELEPHONE)
            .numCB(UPDATED_NUM_CB);

        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUtilisateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtilisateur.getMotDePasse()).isEqualTo(UPDATED_MOT_DE_PASSE);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getAdrRue()).isEqualTo(UPDATED_ADR_RUE);
        assertThat(testUtilisateur.getAdrCodePostal()).isEqualTo(UPDATED_ADR_CODE_POSTAL);
        assertThat(testUtilisateur.getAdrPays()).isEqualTo(UPDATED_ADR_PAYS);
        assertThat(testUtilisateur.getAdrVille()).isEqualTo(UPDATED_ADR_VILLE);
        assertThat(testUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testUtilisateur.getNumCB()).isEqualTo(UPDATED_NUM_CB);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository).save(testUtilisateur);
    }

    @Test
    @Transactional
    void putNonExistingUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(0)).save(utilisateur);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(0)).save(utilisateur);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(0)).save(utilisateur);
    }

    @Test
    @Transactional
    void partialUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .mail(UPDATED_MAIL)
            .prenom(UPDATED_PRENOM)
            .adrPays(UPDATED_ADR_PAYS)
            .adrVille(UPDATED_ADR_VILLE)
            .numCB(UPDATED_NUM_CB);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtilisateur.getMotDePasse()).isEqualTo(DEFAULT_MOT_DE_PASSE);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getAdrRue()).isEqualTo(DEFAULT_ADR_RUE);
        assertThat(testUtilisateur.getAdrCodePostal()).isEqualTo(DEFAULT_ADR_CODE_POSTAL);
        assertThat(testUtilisateur.getAdrPays()).isEqualTo(UPDATED_ADR_PAYS);
        assertThat(testUtilisateur.getAdrVille()).isEqualTo(UPDATED_ADR_VILLE);
        assertThat(testUtilisateur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testUtilisateur.getNumCB()).isEqualTo(UPDATED_NUM_CB);
    }

    @Test
    @Transactional
    void fullUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .mail(UPDATED_MAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .adrRue(UPDATED_ADR_RUE)
            .adrCodePostal(UPDATED_ADR_CODE_POSTAL)
            .adrPays(UPDATED_ADR_PAYS)
            .adrVille(UPDATED_ADR_VILLE)
            .telephone(UPDATED_TELEPHONE)
            .numCB(UPDATED_NUM_CB);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtilisateur.getMotDePasse()).isEqualTo(UPDATED_MOT_DE_PASSE);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getAdrRue()).isEqualTo(UPDATED_ADR_RUE);
        assertThat(testUtilisateur.getAdrCodePostal()).isEqualTo(UPDATED_ADR_CODE_POSTAL);
        assertThat(testUtilisateur.getAdrPays()).isEqualTo(UPDATED_ADR_PAYS);
        assertThat(testUtilisateur.getAdrVille()).isEqualTo(UPDATED_ADR_VILLE);
        assertThat(testUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testUtilisateur.getNumCB()).isEqualTo(UPDATED_NUM_CB);
    }

    @Test
    @Transactional
    void patchNonExistingUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(0)).save(utilisateur);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(0)).save(utilisateur);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();
        utilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(utilisateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(0)).save(utilisateur);
    }

    @Test
    @Transactional
    void deleteUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        int databaseSizeBeforeDelete = utilisateurRepository.findAll().size();

        // Delete the utilisateur
        restUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Utilisateur in Elasticsearch
        verify(mockUtilisateurSearchRepository, times(1)).deleteById(utilisateur.getId());
    }

    @Test
    @Transactional
    void searchUtilisateur() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        when(mockUtilisateurSearchRepository.search(queryStringQuery("id:" + utilisateur.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(utilisateur), PageRequest.of(0, 1), 1));

        // Search the utilisateur
        restUtilisateurMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].adrRue").value(hasItem(DEFAULT_ADR_RUE)))
            .andExpect(jsonPath("$.[*].adrCodePostal").value(hasItem(DEFAULT_ADR_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].adrPays").value(hasItem(DEFAULT_ADR_PAYS)))
            .andExpect(jsonPath("$.[*].adrVille").value(hasItem(DEFAULT_ADR_VILLE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].numCB").value(hasItem(DEFAULT_NUM_CB)));
    }
}
