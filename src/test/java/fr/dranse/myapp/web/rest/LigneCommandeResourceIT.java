package fr.dranse.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.dranse.myapp.IntegrationTest;
import fr.dranse.myapp.domain.LigneCommande;
import fr.dranse.myapp.repository.LigneCommandeRepository;
import fr.dranse.myapp.repository.search.LigneCommandeSearchRepository;
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
 * Integration tests for the {@link LigneCommandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LigneCommandeResourceIT {

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Float DEFAULT_PRIX_PAYE = 1F;
    private static final Float UPDATED_PRIX_PAYE = 2F;

    private static final String ENTITY_API_URL = "/api/ligne-commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ligne-commandes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    /**
     * This repository is mocked in the fr.dranse.myapp.repository.search test package.
     *
     * @see fr.dranse.myapp.repository.search.LigneCommandeSearchRepositoryMockConfiguration
     */
    @Autowired
    private LigneCommandeSearchRepository mockLigneCommandeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneCommandeMockMvc;

    private LigneCommande ligneCommande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneCommande createEntity(EntityManager em) {
        LigneCommande ligneCommande = new LigneCommande().quantite(DEFAULT_QUANTITE).prixPaye(DEFAULT_PRIX_PAYE);
        return ligneCommande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneCommande createUpdatedEntity(EntityManager em) {
        LigneCommande ligneCommande = new LigneCommande().quantite(UPDATED_QUANTITE).prixPaye(UPDATED_PRIX_PAYE);
        return ligneCommande;
    }

    @BeforeEach
    public void initTest() {
        ligneCommande = createEntity(em);
    }

    @Test
    @Transactional
    void createLigneCommande() throws Exception {
        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().size();
        // Create the LigneCommande
        restLigneCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isCreated());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeCreate + 1);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneCommande.getPrixPaye()).isEqualTo(DEFAULT_PRIX_PAYE);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(1)).save(testLigneCommande);
    }

    @Test
    @Transactional
    void createLigneCommandeWithExistingId() throws Exception {
        // Create the LigneCommande with an existing ID
        ligneCommande.setId(1L);

        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeCreate);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(0)).save(ligneCommande);
    }

    @Test
    @Transactional
    void getAllLigneCommandes() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        // Get all the ligneCommandeList
        restLigneCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].prixPaye").value(hasItem(DEFAULT_PRIX_PAYE.doubleValue())));
    }

    @Test
    @Transactional
    void getLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        // Get the ligneCommande
        restLigneCommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, ligneCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneCommande.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.prixPaye").value(DEFAULT_PRIX_PAYE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingLigneCommande() throws Exception {
        // Get the ligneCommande
        restLigneCommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

        // Update the ligneCommande
        LigneCommande updatedLigneCommande = ligneCommandeRepository.findById(ligneCommande.getId()).get();
        // Disconnect from session so that the updates on updatedLigneCommande are not directly saved in db
        em.detach(updatedLigneCommande);
        updatedLigneCommande.quantite(UPDATED_QUANTITE).prixPaye(UPDATED_PRIX_PAYE);

        restLigneCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLigneCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLigneCommande))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommande.getPrixPaye()).isEqualTo(UPDATED_PRIX_PAYE);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository).save(testLigneCommande);
    }

    @Test
    @Transactional
    void putNonExistingLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(0)).save(ligneCommande);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(0)).save(ligneCommande);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(0)).save(ligneCommande);
    }

    @Test
    @Transactional
    void partialUpdateLigneCommandeWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

        // Update the ligneCommande using partial update
        LigneCommande partialUpdatedLigneCommande = new LigneCommande();
        partialUpdatedLigneCommande.setId(ligneCommande.getId());

        partialUpdatedLigneCommande.quantite(UPDATED_QUANTITE);

        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommande))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommande.getPrixPaye()).isEqualTo(DEFAULT_PRIX_PAYE);
    }

    @Test
    @Transactional
    void fullUpdateLigneCommandeWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

        // Update the ligneCommande using partial update
        LigneCommande partialUpdatedLigneCommande = new LigneCommande();
        partialUpdatedLigneCommande.setId(ligneCommande.getId());

        partialUpdatedLigneCommande.quantite(UPDATED_QUANTITE).prixPaye(UPDATED_PRIX_PAYE);

        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommande))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommande.getPrixPaye()).isEqualTo(UPDATED_PRIX_PAYE);
    }

    @Test
    @Transactional
    void patchNonExistingLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligneCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(0)).save(ligneCommande);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(0)).save(ligneCommande);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();
        ligneCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ligneCommande))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(0)).save(ligneCommande);
    }

    @Test
    @Transactional
    void deleteLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        int databaseSizeBeforeDelete = ligneCommandeRepository.findAll().size();

        // Delete the ligneCommande
        restLigneCommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligneCommande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LigneCommande in Elasticsearch
        verify(mockLigneCommandeSearchRepository, times(1)).deleteById(ligneCommande.getId());
    }

    @Test
    @Transactional
    void searchLigneCommande() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);
        when(mockLigneCommandeSearchRepository.search(queryStringQuery("id:" + ligneCommande.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ligneCommande), PageRequest.of(0, 1), 1));

        // Search the ligneCommande
        restLigneCommandeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ligneCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].prixPaye").value(hasItem(DEFAULT_PRIX_PAYE.doubleValue())));
    }
}
