package fr.dranse.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.dranse.myapp.IntegrationTest;
import fr.dranse.myapp.domain.Livre;
import fr.dranse.myapp.repository.LivreRepository;
import fr.dranse.myapp.repository.search.LivreSearchRepository;
import fr.dranse.myapp.service.LivreService;
import java.util.ArrayList;
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
 * Integration tests for the {@link LivreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LivreResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTEUR = "AAAAAAAAAA";
    private static final String UPDATED_AUTEUR = "BBBBBBBBBB";

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    private static final String DEFAULT_SYNOPSIS = "AAAAAAAAAA";
    private static final String UPDATED_SYNOPSIS = "BBBBBBBBBB";

    private static final Integer DEFAULT_EDITION = 1;
    private static final Integer UPDATED_EDITION = 2;

    private static final Integer DEFAULT_ANNEE_PUBLICATION = 1;
    private static final Integer UPDATED_ANNEE_PUBLICATION = 2;

    private static final String DEFAULT_EDITEUR = "AAAAAAAAAA";
    private static final String UPDATED_EDITEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final String DEFAULT_URL_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_URL_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/livres";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivreRepository livreRepository;

    @Mock
    private LivreRepository livreRepositoryMock;

    @Mock
    private LivreService livreServiceMock;

    /**
     * This repository is mocked in the fr.dranse.myapp.repository.search test package.
     *
     * @see fr.dranse.myapp.repository.search.LivreSearchRepositoryMockConfiguration
     */
    @Autowired
    private LivreSearchRepository mockLivreSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivreMockMvc;

    private Livre livre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livre createEntity(EntityManager em) {
        Livre livre = new Livre()
            .titre(DEFAULT_TITRE)
            .auteur(DEFAULT_AUTEUR)
            .prix(DEFAULT_PRIX)
            .synopsis(DEFAULT_SYNOPSIS)
            .edition(DEFAULT_EDITION)
            .anneePublication(DEFAULT_ANNEE_PUBLICATION)
            .editeur(DEFAULT_EDITEUR)
            .stock(DEFAULT_STOCK)
            .urlImage(DEFAULT_URL_IMAGE);
        return livre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livre createUpdatedEntity(EntityManager em) {
        Livre livre = new Livre()
            .titre(UPDATED_TITRE)
            .auteur(UPDATED_AUTEUR)
            .prix(UPDATED_PRIX)
            .synopsis(UPDATED_SYNOPSIS)
            .edition(UPDATED_EDITION)
            .anneePublication(UPDATED_ANNEE_PUBLICATION)
            .editeur(UPDATED_EDITEUR)
            .stock(UPDATED_STOCK)
            .urlImage(UPDATED_URL_IMAGE);
        return livre;
    }

    @BeforeEach
    public void initTest() {
        livre = createEntity(em);
    }

    @Test
    @Transactional
    void createLivre() throws Exception {
        int databaseSizeBeforeCreate = livreRepository.findAll().size();
        // Create the Livre
        restLivreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livre)))
            .andExpect(status().isCreated());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeCreate + 1);
        Livre testLivre = livreList.get(livreList.size() - 1);
        assertThat(testLivre.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testLivre.getAuteur()).isEqualTo(DEFAULT_AUTEUR);
        assertThat(testLivre.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testLivre.getSynopsis()).isEqualTo(DEFAULT_SYNOPSIS);
        assertThat(testLivre.getEdition()).isEqualTo(DEFAULT_EDITION);
        assertThat(testLivre.getAnneePublication()).isEqualTo(DEFAULT_ANNEE_PUBLICATION);
        assertThat(testLivre.getEditeur()).isEqualTo(DEFAULT_EDITEUR);
        assertThat(testLivre.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testLivre.getUrlImage()).isEqualTo(DEFAULT_URL_IMAGE);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(1)).save(testLivre);
    }

    @Test
    @Transactional
    void createLivreWithExistingId() throws Exception {
        // Create the Livre with an existing ID
        livre.setId(1L);

        int databaseSizeBeforeCreate = livreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livre)))
            .andExpect(status().isBadRequest());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeCreate);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(0)).save(livre);
    }

    @Test
    @Transactional
    void getAllLivres() throws Exception {
        // Initialize the database
        livreRepository.saveAndFlush(livre);

        // Get all the livreList
        restLivreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livre.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].auteur").value(hasItem(DEFAULT_AUTEUR)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())))
            .andExpect(jsonPath("$.[*].synopsis").value(hasItem(DEFAULT_SYNOPSIS)))
            .andExpect(jsonPath("$.[*].edition").value(hasItem(DEFAULT_EDITION)))
            .andExpect(jsonPath("$.[*].anneePublication").value(hasItem(DEFAULT_ANNEE_PUBLICATION)))
            .andExpect(jsonPath("$.[*].editeur").value(hasItem(DEFAULT_EDITEUR)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].urlImage").value(hasItem(DEFAULT_URL_IMAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLivresWithEagerRelationshipsIsEnabled() throws Exception {
        when(livreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLivreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(livreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLivresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(livreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLivreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(livreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLivre() throws Exception {
        // Initialize the database
        livreRepository.saveAndFlush(livre);

        // Get the livre
        restLivreMockMvc
            .perform(get(ENTITY_API_URL_ID, livre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livre.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.auteur").value(DEFAULT_AUTEUR))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()))
            .andExpect(jsonPath("$.synopsis").value(DEFAULT_SYNOPSIS))
            .andExpect(jsonPath("$.edition").value(DEFAULT_EDITION))
            .andExpect(jsonPath("$.anneePublication").value(DEFAULT_ANNEE_PUBLICATION))
            .andExpect(jsonPath("$.editeur").value(DEFAULT_EDITEUR))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.urlImage").value(DEFAULT_URL_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingLivre() throws Exception {
        // Get the livre
        restLivreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLivre() throws Exception {
        // Initialize the database
        livreRepository.saveAndFlush(livre);

        int databaseSizeBeforeUpdate = livreRepository.findAll().size();

        // Update the livre
        Livre updatedLivre = livreRepository.findById(livre.getId()).get();
        // Disconnect from session so that the updates on updatedLivre are not directly saved in db
        em.detach(updatedLivre);
        updatedLivre
            .titre(UPDATED_TITRE)
            .auteur(UPDATED_AUTEUR)
            .prix(UPDATED_PRIX)
            .synopsis(UPDATED_SYNOPSIS)
            .edition(UPDATED_EDITION)
            .anneePublication(UPDATED_ANNEE_PUBLICATION)
            .editeur(UPDATED_EDITEUR)
            .stock(UPDATED_STOCK)
            .urlImage(UPDATED_URL_IMAGE);

        restLivreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLivre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLivre))
            )
            .andExpect(status().isOk());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);
        Livre testLivre = livreList.get(livreList.size() - 1);
        assertThat(testLivre.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testLivre.getAuteur()).isEqualTo(UPDATED_AUTEUR);
        assertThat(testLivre.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testLivre.getSynopsis()).isEqualTo(UPDATED_SYNOPSIS);
        assertThat(testLivre.getEdition()).isEqualTo(UPDATED_EDITION);
        assertThat(testLivre.getAnneePublication()).isEqualTo(UPDATED_ANNEE_PUBLICATION);
        assertThat(testLivre.getEditeur()).isEqualTo(UPDATED_EDITEUR);
        assertThat(testLivre.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testLivre.getUrlImage()).isEqualTo(UPDATED_URL_IMAGE);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository).save(testLivre);
    }

    @Test
    @Transactional
    void putNonExistingLivre() throws Exception {
        int databaseSizeBeforeUpdate = livreRepository.findAll().size();
        livre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(0)).save(livre);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivre() throws Exception {
        int databaseSizeBeforeUpdate = livreRepository.findAll().size();
        livre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(0)).save(livre);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivre() throws Exception {
        int databaseSizeBeforeUpdate = livreRepository.findAll().size();
        livre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(0)).save(livre);
    }

    @Test
    @Transactional
    void partialUpdateLivreWithPatch() throws Exception {
        // Initialize the database
        livreRepository.saveAndFlush(livre);

        int databaseSizeBeforeUpdate = livreRepository.findAll().size();

        // Update the livre using partial update
        Livre partialUpdatedLivre = new Livre();
        partialUpdatedLivre.setId(livre.getId());

        partialUpdatedLivre.auteur(UPDATED_AUTEUR).prix(UPDATED_PRIX).synopsis(UPDATED_SYNOPSIS).edition(UPDATED_EDITION);

        restLivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivre))
            )
            .andExpect(status().isOk());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);
        Livre testLivre = livreList.get(livreList.size() - 1);
        assertThat(testLivre.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testLivre.getAuteur()).isEqualTo(UPDATED_AUTEUR);
        assertThat(testLivre.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testLivre.getSynopsis()).isEqualTo(UPDATED_SYNOPSIS);
        assertThat(testLivre.getEdition()).isEqualTo(UPDATED_EDITION);
        assertThat(testLivre.getAnneePublication()).isEqualTo(DEFAULT_ANNEE_PUBLICATION);
        assertThat(testLivre.getEditeur()).isEqualTo(DEFAULT_EDITEUR);
        assertThat(testLivre.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testLivre.getUrlImage()).isEqualTo(DEFAULT_URL_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateLivreWithPatch() throws Exception {
        // Initialize the database
        livreRepository.saveAndFlush(livre);

        int databaseSizeBeforeUpdate = livreRepository.findAll().size();

        // Update the livre using partial update
        Livre partialUpdatedLivre = new Livre();
        partialUpdatedLivre.setId(livre.getId());

        partialUpdatedLivre
            .titre(UPDATED_TITRE)
            .auteur(UPDATED_AUTEUR)
            .prix(UPDATED_PRIX)
            .synopsis(UPDATED_SYNOPSIS)
            .edition(UPDATED_EDITION)
            .anneePublication(UPDATED_ANNEE_PUBLICATION)
            .editeur(UPDATED_EDITEUR)
            .stock(UPDATED_STOCK)
            .urlImage(UPDATED_URL_IMAGE);

        restLivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivre))
            )
            .andExpect(status().isOk());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);
        Livre testLivre = livreList.get(livreList.size() - 1);
        assertThat(testLivre.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testLivre.getAuteur()).isEqualTo(UPDATED_AUTEUR);
        assertThat(testLivre.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testLivre.getSynopsis()).isEqualTo(UPDATED_SYNOPSIS);
        assertThat(testLivre.getEdition()).isEqualTo(UPDATED_EDITION);
        assertThat(testLivre.getAnneePublication()).isEqualTo(UPDATED_ANNEE_PUBLICATION);
        assertThat(testLivre.getEditeur()).isEqualTo(UPDATED_EDITEUR);
        assertThat(testLivre.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testLivre.getUrlImage()).isEqualTo(UPDATED_URL_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingLivre() throws Exception {
        int databaseSizeBeforeUpdate = livreRepository.findAll().size();
        livre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(0)).save(livre);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivre() throws Exception {
        int databaseSizeBeforeUpdate = livreRepository.findAll().size();
        livre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(0)).save(livre);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivre() throws Exception {
        int databaseSizeBeforeUpdate = livreRepository.findAll().size();
        livre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(livre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livre in the database
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(0)).save(livre);
    }

    @Test
    @Transactional
    void deleteLivre() throws Exception {
        // Initialize the database
        livreRepository.saveAndFlush(livre);

        int databaseSizeBeforeDelete = livreRepository.findAll().size();

        // Delete the livre
        restLivreMockMvc
            .perform(delete(ENTITY_API_URL_ID, livre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Livre> livreList = livreRepository.findAll();
        assertThat(livreList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Livre in Elasticsearch
        verify(mockLivreSearchRepository, times(1)).deleteById(livre.getId());
    }

    @Test
    @Transactional
    void searchLivre() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        livreRepository.saveAndFlush(livre);
        when(mockLivreSearchRepository.search(queryStringQuery("id:" + livre.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(livre), PageRequest.of(0, 1), 1));

        // Search the livre
        restLivreMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + livre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livre.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].auteur").value(hasItem(DEFAULT_AUTEUR)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())))
            .andExpect(jsonPath("$.[*].synopsis").value(hasItem(DEFAULT_SYNOPSIS)))
            .andExpect(jsonPath("$.[*].edition").value(hasItem(DEFAULT_EDITION)))
            .andExpect(jsonPath("$.[*].anneePublication").value(hasItem(DEFAULT_ANNEE_PUBLICATION)))
            .andExpect(jsonPath("$.[*].editeur").value(hasItem(DEFAULT_EDITEUR)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].urlImage").value(hasItem(DEFAULT_URL_IMAGE)));
    }
}
