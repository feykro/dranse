package fr.dranse.myapp.web.rest;

import static fr.dranse.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.dranse.myapp.IntegrationTest;
import fr.dranse.myapp.domain.Commande;
import fr.dranse.myapp.repository.CommandeRepository;
import fr.dranse.myapp.repository.search.CommandeSearchRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommandeResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PAYS_LIVRAISON = "AAAAAAAAAA";
    private static final String UPDATED_PAYS_LIVRAISON = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL_LIVRAISON = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL_LIVRAISON = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE_LIVRAISON = "AAAAAAAAAA";
    private static final String UPDATED_VILLE_LIVRAISON = "BBBBBBBBBB";

    private static final String DEFAULT_RUE_LIVRAISON = "AAAAAAAAAA";
    private static final String UPDATED_RUE_LIVRAISON = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LIVRAISON = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LIVRAISON = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS_FACTURATION = "AAAAAAAAAA";
    private static final String UPDATED_PAYS_FACTURATION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL_FACTURATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL_FACTURATION = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE_FACTURATION = "AAAAAAAAAA";
    private static final String UPDATED_VILLE_FACTURATION = "BBBBBBBBBB";

    private static final String DEFAULT_RUE_FACTURATION = "AAAAAAAAAA";
    private static final String UPDATED_RUE_FACTURATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_FACTURATION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FACTURATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAYEE = false;
    private static final Boolean UPDATED_PAYEE = true;

    private static final String ENTITY_API_URL = "/api/commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/commandes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeRepository commandeRepository;

    /**
     * This repository is mocked in the fr.dranse.myapp.repository.search test package.
     *
     * @see fr.dranse.myapp.repository.search.CommandeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommandeSearchRepository mockCommandeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandeMockMvc;

    private Commande commande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .dateCreation(DEFAULT_DATE)
            .paysLivraison(DEFAULT_PAYS_LIVRAISON)
            .codePostalLivraison(DEFAULT_CODE_POSTAL_LIVRAISON)
            .villeLivraison(DEFAULT_VILLE_LIVRAISON)
            .rueLivraison(DEFAULT_RUE_LIVRAISON)
            .nomLivraison(DEFAULT_NOM_LIVRAISON)
            .paysFacturation(DEFAULT_PAYS_FACTURATION)
            .codePostalFacturation(DEFAULT_CODE_POSTAL_FACTURATION)
            .villeFacturation(DEFAULT_VILLE_FACTURATION)
            .rueFacturation(DEFAULT_RUE_FACTURATION)
            .nomFacturation(DEFAULT_NOM_FACTURATION)
            .payee(DEFAULT_PAYEE);
        return commande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande()
            .dateModification(UPDATED_DATE)
            .paysLivraison(UPDATED_PAYS_LIVRAISON)
            .codePostalLivraison(UPDATED_CODE_POSTAL_LIVRAISON)
            .villeLivraison(UPDATED_VILLE_LIVRAISON)
            .rueLivraison(UPDATED_RUE_LIVRAISON)
            .nomLivraison(UPDATED_NOM_LIVRAISON)
            .paysFacturation(UPDATED_PAYS_FACTURATION)
            .codePostalFacturation(UPDATED_CODE_POSTAL_FACTURATION)
            .villeFacturation(UPDATED_VILLE_FACTURATION)
            .rueFacturation(UPDATED_RUE_FACTURATION)
            .nomFacturation(UPDATED_NOM_FACTURATION)
            .payee(UPDATED_PAYEE);
        return commande;
    }

    @BeforeEach
    public void initTest() {
        commande = createEntity(em);
    }

    @Test
    @Transactional
    void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();
        // Create the Commande
        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDateCreation()).isEqualTo(DEFAULT_DATE);
        assertThat(testCommande.getPaysLivraison()).isEqualTo(DEFAULT_PAYS_LIVRAISON);
        assertThat(testCommande.getCodePostalLivraison()).isEqualTo(DEFAULT_CODE_POSTAL_LIVRAISON);
        assertThat(testCommande.getVilleLivraison()).isEqualTo(DEFAULT_VILLE_LIVRAISON);
        assertThat(testCommande.getRueLivraison()).isEqualTo(DEFAULT_RUE_LIVRAISON);
        assertThat(testCommande.getNomLivraison()).isEqualTo(DEFAULT_NOM_LIVRAISON);
        assertThat(testCommande.getPaysFacturation()).isEqualTo(DEFAULT_PAYS_FACTURATION);
        assertThat(testCommande.getCodePostalFacturation()).isEqualTo(DEFAULT_CODE_POSTAL_FACTURATION);
        assertThat(testCommande.getVilleFacturation()).isEqualTo(DEFAULT_VILLE_FACTURATION);
        assertThat(testCommande.getRueFacturation()).isEqualTo(DEFAULT_RUE_FACTURATION);
        assertThat(testCommande.getNomFacturation()).isEqualTo(DEFAULT_NOM_FACTURATION);
        assertThat(testCommande.getPayee()).isEqualTo(DEFAULT_PAYEE);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(1)).save(testCommande);
    }

    @Test
    @Transactional
    void createCommandeWithExistingId() throws Exception {
        // Create the Commande with an existing ID
        commande.setId(1L);

        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(0)).save(commande);
    }

    @Test
    @Transactional
    void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].paysLivraison").value(hasItem(DEFAULT_PAYS_LIVRAISON)))
            .andExpect(jsonPath("$.[*].codePostalLivraison").value(hasItem(DEFAULT_CODE_POSTAL_LIVRAISON)))
            .andExpect(jsonPath("$.[*].villeLivraison").value(hasItem(DEFAULT_VILLE_LIVRAISON)))
            .andExpect(jsonPath("$.[*].rueLivraison").value(hasItem(DEFAULT_RUE_LIVRAISON)))
            .andExpect(jsonPath("$.[*].nomLivraison").value(hasItem(DEFAULT_NOM_LIVRAISON)))
            .andExpect(jsonPath("$.[*].paysFacturation").value(hasItem(DEFAULT_PAYS_FACTURATION)))
            .andExpect(jsonPath("$.[*].codePostalFacturation").value(hasItem(DEFAULT_CODE_POSTAL_FACTURATION)))
            .andExpect(jsonPath("$.[*].villeFacturation").value(hasItem(DEFAULT_VILLE_FACTURATION)))
            .andExpect(jsonPath("$.[*].rueFacturation").value(hasItem(DEFAULT_RUE_FACTURATION)))
            .andExpect(jsonPath("$.[*].nomFacturation").value(hasItem(DEFAULT_NOM_FACTURATION)))
            .andExpect(jsonPath("$.[*].payee").value(hasItem(DEFAULT_PAYEE.booleanValue())));
    }

    @Test
    @Transactional
    void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.paysLivraison").value(DEFAULT_PAYS_LIVRAISON))
            .andExpect(jsonPath("$.codePostalLivraison").value(DEFAULT_CODE_POSTAL_LIVRAISON))
            .andExpect(jsonPath("$.villeLivraison").value(DEFAULT_VILLE_LIVRAISON))
            .andExpect(jsonPath("$.rueLivraison").value(DEFAULT_RUE_LIVRAISON))
            .andExpect(jsonPath("$.nomLivraison").value(DEFAULT_NOM_LIVRAISON))
            .andExpect(jsonPath("$.paysFacturation").value(DEFAULT_PAYS_FACTURATION))
            .andExpect(jsonPath("$.codePostalFacturation").value(DEFAULT_CODE_POSTAL_FACTURATION))
            .andExpect(jsonPath("$.villeFacturation").value(DEFAULT_VILLE_FACTURATION))
            .andExpect(jsonPath("$.rueFacturation").value(DEFAULT_RUE_FACTURATION))
            .andExpect(jsonPath("$.nomFacturation").value(DEFAULT_NOM_FACTURATION))
            .andExpect(jsonPath("$.payee").value(DEFAULT_PAYEE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).get();
        // Disconnect from session so that the updates on updatedCommande are not directly saved in db
        em.detach(updatedCommande);
        updatedCommande
            .dateModification(UPDATED_DATE)
            .paysLivraison(UPDATED_PAYS_LIVRAISON)
            .codePostalLivraison(UPDATED_CODE_POSTAL_LIVRAISON)
            .villeLivraison(UPDATED_VILLE_LIVRAISON)
            .rueLivraison(UPDATED_RUE_LIVRAISON)
            .nomLivraison(UPDATED_NOM_LIVRAISON)
            .paysFacturation(UPDATED_PAYS_FACTURATION)
            .codePostalFacturation(UPDATED_CODE_POSTAL_FACTURATION)
            .villeFacturation(UPDATED_VILLE_FACTURATION)
            .rueFacturation(UPDATED_RUE_FACTURATION)
            .nomFacturation(UPDATED_NOM_FACTURATION)
            .payee(UPDATED_PAYEE);

        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommande))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDateCreation()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getPaysLivraison()).isEqualTo(UPDATED_PAYS_LIVRAISON);
        assertThat(testCommande.getCodePostalLivraison()).isEqualTo(UPDATED_CODE_POSTAL_LIVRAISON);
        assertThat(testCommande.getVilleLivraison()).isEqualTo(UPDATED_VILLE_LIVRAISON);
        assertThat(testCommande.getRueLivraison()).isEqualTo(UPDATED_RUE_LIVRAISON);
        assertThat(testCommande.getNomLivraison()).isEqualTo(UPDATED_NOM_LIVRAISON);
        assertThat(testCommande.getPaysFacturation()).isEqualTo(UPDATED_PAYS_FACTURATION);
        assertThat(testCommande.getCodePostalFacturation()).isEqualTo(UPDATED_CODE_POSTAL_FACTURATION);
        assertThat(testCommande.getVilleFacturation()).isEqualTo(UPDATED_VILLE_FACTURATION);
        assertThat(testCommande.getRueFacturation()).isEqualTo(UPDATED_RUE_FACTURATION);
        assertThat(testCommande.getNomFacturation()).isEqualTo(UPDATED_NOM_FACTURATION);
        assertThat(testCommande.getPayee()).isEqualTo(UPDATED_PAYEE);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository).save(testCommande);
    }

    @Test
    @Transactional
    void putNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(0)).save(commande);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(0)).save(commande);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(0)).save(commande);
    }

    @Test
    @Transactional
    void partialUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande
            .dateModification(UPDATED_DATE)
            .villeLivraison(UPDATED_VILLE_LIVRAISON)
            .rueLivraison(UPDATED_RUE_LIVRAISON)
            .paysFacturation(UPDATED_PAYS_FACTURATION)
            .codePostalFacturation(UPDATED_CODE_POSTAL_FACTURATION)
            .rueFacturation(UPDATED_RUE_FACTURATION)
            .nomFacturation(UPDATED_NOM_FACTURATION);

        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDateCreation()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getPaysLivraison()).isEqualTo(DEFAULT_PAYS_LIVRAISON);
        assertThat(testCommande.getCodePostalLivraison()).isEqualTo(DEFAULT_CODE_POSTAL_LIVRAISON);
        assertThat(testCommande.getVilleLivraison()).isEqualTo(UPDATED_VILLE_LIVRAISON);
        assertThat(testCommande.getRueLivraison()).isEqualTo(UPDATED_RUE_LIVRAISON);
        assertThat(testCommande.getNomLivraison()).isEqualTo(DEFAULT_NOM_LIVRAISON);
        assertThat(testCommande.getPaysFacturation()).isEqualTo(UPDATED_PAYS_FACTURATION);
        assertThat(testCommande.getCodePostalFacturation()).isEqualTo(UPDATED_CODE_POSTAL_FACTURATION);
        assertThat(testCommande.getVilleFacturation()).isEqualTo(DEFAULT_VILLE_FACTURATION);
        assertThat(testCommande.getRueFacturation()).isEqualTo(UPDATED_RUE_FACTURATION);
        assertThat(testCommande.getNomFacturation()).isEqualTo(UPDATED_NOM_FACTURATION);
        assertThat(testCommande.getPayee()).isEqualTo(DEFAULT_PAYEE);
    }

    @Test
    @Transactional
    void fullUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande
            .dateModification(UPDATED_DATE)
            .paysLivraison(UPDATED_PAYS_LIVRAISON)
            .codePostalLivraison(UPDATED_CODE_POSTAL_LIVRAISON)
            .villeLivraison(UPDATED_VILLE_LIVRAISON)
            .rueLivraison(UPDATED_RUE_LIVRAISON)
            .nomLivraison(UPDATED_NOM_LIVRAISON)
            .paysFacturation(UPDATED_PAYS_FACTURATION)
            .codePostalFacturation(UPDATED_CODE_POSTAL_FACTURATION)
            .villeFacturation(UPDATED_VILLE_FACTURATION)
            .rueFacturation(UPDATED_RUE_FACTURATION)
            .nomFacturation(UPDATED_NOM_FACTURATION)
            .payee(UPDATED_PAYEE);

        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDateModification()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getPaysLivraison()).isEqualTo(UPDATED_PAYS_LIVRAISON);
        assertThat(testCommande.getCodePostalLivraison()).isEqualTo(UPDATED_CODE_POSTAL_LIVRAISON);
        assertThat(testCommande.getVilleLivraison()).isEqualTo(UPDATED_VILLE_LIVRAISON);
        assertThat(testCommande.getRueLivraison()).isEqualTo(UPDATED_RUE_LIVRAISON);
        assertThat(testCommande.getNomLivraison()).isEqualTo(UPDATED_NOM_LIVRAISON);
        assertThat(testCommande.getPaysFacturation()).isEqualTo(UPDATED_PAYS_FACTURATION);
        assertThat(testCommande.getCodePostalFacturation()).isEqualTo(UPDATED_CODE_POSTAL_FACTURATION);
        assertThat(testCommande.getVilleFacturation()).isEqualTo(UPDATED_VILLE_FACTURATION);
        assertThat(testCommande.getRueFacturation()).isEqualTo(UPDATED_RUE_FACTURATION);
        assertThat(testCommande.getNomFacturation()).isEqualTo(UPDATED_NOM_FACTURATION);
        assertThat(testCommande.getPayee()).isEqualTo(UPDATED_PAYEE);
    }

    @Test
    @Transactional
    void patchNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(0)).save(commande);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(0)).save(commande);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(0)).save(commande);
    }

    @Test
    @Transactional
    void deleteCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Delete the commande
        restCommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, commande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Commande in Elasticsearch
        verify(mockCommandeSearchRepository, times(1)).deleteById(commande.getId());
    }

    @Test
    @Transactional
    void searchCommande() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        when(mockCommandeSearchRepository.search(queryStringQuery("id:" + commande.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(commande), PageRequest.of(0, 1), 1));

        // Search the commande
        restCommandeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].paysLivraison").value(hasItem(DEFAULT_PAYS_LIVRAISON)))
            .andExpect(jsonPath("$.[*].codePostalLivraison").value(hasItem(DEFAULT_CODE_POSTAL_LIVRAISON)))
            .andExpect(jsonPath("$.[*].villeLivraison").value(hasItem(DEFAULT_VILLE_LIVRAISON)))
            .andExpect(jsonPath("$.[*].rueLivraison").value(hasItem(DEFAULT_RUE_LIVRAISON)))
            .andExpect(jsonPath("$.[*].nomLivraison").value(hasItem(DEFAULT_NOM_LIVRAISON)))
            .andExpect(jsonPath("$.[*].paysFacturation").value(hasItem(DEFAULT_PAYS_FACTURATION)))
            .andExpect(jsonPath("$.[*].codePostalFacturation").value(hasItem(DEFAULT_CODE_POSTAL_FACTURATION)))
            .andExpect(jsonPath("$.[*].villeFacturation").value(hasItem(DEFAULT_VILLE_FACTURATION)))
            .andExpect(jsonPath("$.[*].rueFacturation").value(hasItem(DEFAULT_RUE_FACTURATION)))
            .andExpect(jsonPath("$.[*].nomFacturation").value(hasItem(DEFAULT_NOM_FACTURATION)))
            .andExpect(jsonPath("$.[*].payee").value(hasItem(DEFAULT_PAYEE.booleanValue())));
    }
}
