package fr.dranse.myapp.web.rest;

import static fr.dranse.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.dranse.myapp.IntegrationTest;
import fr.dranse.myapp.domain.Avis;
import fr.dranse.myapp.repository.AvisRepository;
import fr.dranse.myapp.repository.search.AvisSearchRepository;
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
 * Integration tests for the {@link AvisResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AvisResourceIT {

    private static final Integer DEFAULT_NOTE = 0;
    private static final Integer UPDATED_NOTE = 1;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_PUBLICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_PUBLICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_AFFICHE = false;
    private static final Boolean UPDATED_AFFICHE = true;

    private static final String ENTITY_API_URL = "/api/avis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/avis";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvisRepository avisRepository;

    /**
     * This repository is mocked in the fr.dranse.myapp.repository.search test package.
     *
     * @see fr.dranse.myapp.repository.search.AvisSearchRepositoryMockConfiguration
     */
    @Autowired
    private AvisSearchRepository mockAvisSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvisMockMvc;

    private Avis avis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avis createEntity(EntityManager em) {
        Avis avis = new Avis()
            .note(DEFAULT_NOTE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .datePublication(DEFAULT_DATE_PUBLICATION)
            .affiche(DEFAULT_AFFICHE);
        return avis;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avis createUpdatedEntity(EntityManager em) {
        Avis avis = new Avis()
            .note(UPDATED_NOTE)
            .commentaire(UPDATED_COMMENTAIRE)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .affiche(UPDATED_AFFICHE);
        return avis;
    }

    @BeforeEach
    public void initTest() {
        avis = createEntity(em);
    }

    @Test
    @Transactional
    void createAvis() throws Exception {
        int databaseSizeBeforeCreate = avisRepository.findAll().size();
        // Create the Avis
        restAvisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avis)))
            .andExpect(status().isCreated());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeCreate + 1);
        Avis testAvis = avisList.get(avisList.size() - 1);
        assertThat(testAvis.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testAvis.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testAvis.getDatePublication()).isEqualTo(DEFAULT_DATE_PUBLICATION);
        assertThat(testAvis.getAffiche()).isEqualTo(DEFAULT_AFFICHE);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(1)).save(testAvis);
    }

    @Test
    @Transactional
    void createAvisWithExistingId() throws Exception {
        // Create the Avis with an existing ID
        avis.setId(1L);

        int databaseSizeBeforeCreate = avisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avis)))
            .andExpect(status().isBadRequest());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeCreate);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(0)).save(avis);
    }

    @Test
    @Transactional
    void getAllAvis() throws Exception {
        // Initialize the database
        avisRepository.saveAndFlush(avis);

        // Get all the avisList
        restAvisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avis.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(sameInstant(DEFAULT_DATE_PUBLICATION))))
            .andExpect(jsonPath("$.[*].affiche").value(hasItem(DEFAULT_AFFICHE.booleanValue())));
    }

    @Test
    @Transactional
    void getAvis() throws Exception {
        // Initialize the database
        avisRepository.saveAndFlush(avis);

        // Get the avis
        restAvisMockMvc
            .perform(get(ENTITY_API_URL_ID, avis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avis.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.datePublication").value(sameInstant(DEFAULT_DATE_PUBLICATION)))
            .andExpect(jsonPath("$.affiche").value(DEFAULT_AFFICHE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAvis() throws Exception {
        // Get the avis
        restAvisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAvis() throws Exception {
        // Initialize the database
        avisRepository.saveAndFlush(avis);

        int databaseSizeBeforeUpdate = avisRepository.findAll().size();

        // Update the avis
        Avis updatedAvis = avisRepository.findById(avis.getId()).get();
        // Disconnect from session so that the updates on updatedAvis are not directly saved in db
        em.detach(updatedAvis);
        updatedAvis.note(UPDATED_NOTE).commentaire(UPDATED_COMMENTAIRE).datePublication(UPDATED_DATE_PUBLICATION).affiche(UPDATED_AFFICHE);

        restAvisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvis.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAvis))
            )
            .andExpect(status().isOk());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);
        Avis testAvis = avisList.get(avisList.size() - 1);
        assertThat(testAvis.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testAvis.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testAvis.getDatePublication()).isEqualTo(UPDATED_DATE_PUBLICATION);
        assertThat(testAvis.getAffiche()).isEqualTo(UPDATED_AFFICHE);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository).save(testAvis);
    }

    @Test
    @Transactional
    void putNonExistingAvis() throws Exception {
        int databaseSizeBeforeUpdate = avisRepository.findAll().size();
        avis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avis.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avis))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(0)).save(avis);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvis() throws Exception {
        int databaseSizeBeforeUpdate = avisRepository.findAll().size();
        avis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avis))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(0)).save(avis);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvis() throws Exception {
        int databaseSizeBeforeUpdate = avisRepository.findAll().size();
        avis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avis)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(0)).save(avis);
    }

    @Test
    @Transactional
    void partialUpdateAvisWithPatch() throws Exception {
        // Initialize the database
        avisRepository.saveAndFlush(avis);

        int databaseSizeBeforeUpdate = avisRepository.findAll().size();

        // Update the avis using partial update
        Avis partialUpdatedAvis = new Avis();
        partialUpdatedAvis.setId(avis.getId());

        partialUpdatedAvis
            .note(UPDATED_NOTE)
            .commentaire(UPDATED_COMMENTAIRE)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .affiche(UPDATED_AFFICHE);

        restAvisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvis))
            )
            .andExpect(status().isOk());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);
        Avis testAvis = avisList.get(avisList.size() - 1);
        assertThat(testAvis.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testAvis.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testAvis.getDatePublication()).isEqualTo(UPDATED_DATE_PUBLICATION);
        assertThat(testAvis.getAffiche()).isEqualTo(UPDATED_AFFICHE);
    }

    @Test
    @Transactional
    void fullUpdateAvisWithPatch() throws Exception {
        // Initialize the database
        avisRepository.saveAndFlush(avis);

        int databaseSizeBeforeUpdate = avisRepository.findAll().size();

        // Update the avis using partial update
        Avis partialUpdatedAvis = new Avis();
        partialUpdatedAvis.setId(avis.getId());

        partialUpdatedAvis
            .note(UPDATED_NOTE)
            .commentaire(UPDATED_COMMENTAIRE)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .affiche(UPDATED_AFFICHE);

        restAvisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvis))
            )
            .andExpect(status().isOk());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);
        Avis testAvis = avisList.get(avisList.size() - 1);
        assertThat(testAvis.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testAvis.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testAvis.getDatePublication()).isEqualTo(UPDATED_DATE_PUBLICATION);
        assertThat(testAvis.getAffiche()).isEqualTo(UPDATED_AFFICHE);
    }

    @Test
    @Transactional
    void patchNonExistingAvis() throws Exception {
        int databaseSizeBeforeUpdate = avisRepository.findAll().size();
        avis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avis))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(0)).save(avis);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvis() throws Exception {
        int databaseSizeBeforeUpdate = avisRepository.findAll().size();
        avis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avis))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(0)).save(avis);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvis() throws Exception {
        int databaseSizeBeforeUpdate = avisRepository.findAll().size();
        avis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvisMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(avis)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avis in the database
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(0)).save(avis);
    }

    @Test
    @Transactional
    void deleteAvis() throws Exception {
        // Initialize the database
        avisRepository.saveAndFlush(avis);

        int databaseSizeBeforeDelete = avisRepository.findAll().size();

        // Delete the avis
        restAvisMockMvc
            .perform(delete(ENTITY_API_URL_ID, avis.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Avis> avisList = avisRepository.findAll();
        assertThat(avisList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Avis in Elasticsearch
        verify(mockAvisSearchRepository, times(1)).deleteById(avis.getId());
    }

    @Test
    @Transactional
    void searchAvis() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        avisRepository.saveAndFlush(avis);
        when(mockAvisSearchRepository.search(queryStringQuery("id:" + avis.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(avis), PageRequest.of(0, 1), 1));

        // Search the avis
        restAvisMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + avis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avis.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(sameInstant(DEFAULT_DATE_PUBLICATION))))
            .andExpect(jsonPath("$.[*].affiche").value(hasItem(DEFAULT_AFFICHE.booleanValue())));
    }
}
