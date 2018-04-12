package com.gesbtp.atos.web.rest;

import com.gesbtp.atos.GesBtpApp;

import com.gesbtp.atos.domain.Produits;
import com.gesbtp.atos.repository.ProduitsRepository;
import com.gesbtp.atos.repository.search.ProduitsSearchRepository;
import com.gesbtp.atos.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.gesbtp.atos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProduitsResource REST controller.
 *
 * @see ProduitsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GesBtpApp.class)
public class ProduitsResourceIntTest {

    private static final String DEFAULT_NOM_PRODUIT = "AAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM_PRODUIT = "BBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_PRIX_UNITAIRE = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Autowired
    private ProduitsRepository produitsRepository;

    @Autowired
    private ProduitsSearchRepository produitsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProduitsMockMvc;

    private Produits produits;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProduitsResource produitsResource = new ProduitsResource(produitsRepository, produitsSearchRepository);
        this.restProduitsMockMvc = MockMvcBuilders.standaloneSetup(produitsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produits createEntity(EntityManager em) {
        Produits produits = new Produits()
            .nomProduit(DEFAULT_NOM_PRODUIT)
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
            .description(DEFAULT_DESCRIPTION);
        return produits;
    }

    @Before
    public void initTest() {
        produitsSearchRepository.deleteAll();
        produits = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduits() throws Exception {
        int databaseSizeBeforeCreate = produitsRepository.findAll().size();

        // Create the Produits
        restProduitsMockMvc.perform(post("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isCreated());

        // Validate the Produits in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeCreate + 1);
        Produits testProduits = produitsList.get(produitsList.size() - 1);
        assertThat(testProduits.getNomProduit()).isEqualTo(DEFAULT_NOM_PRODUIT);
        assertThat(testProduits.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testProduits.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Produits in Elasticsearch
        Produits produitsEs = produitsSearchRepository.findOne(testProduits.getId());
        assertThat(produitsEs).isEqualToIgnoringGivenFields(testProduits);
    }

    @Test
    @Transactional
    public void createProduitsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = produitsRepository.findAll().size();

        // Create the Produits with an existing ID
        produits.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProduitsMockMvc.perform(post("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isBadRequest());

        // Validate the Produits in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomProduitIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitsRepository.findAll().size();
        // set the field null
        produits.setNomProduit(null);

        // Create the Produits, which fails.

        restProduitsMockMvc.perform(post("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isBadRequest());

        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitsRepository.findAll().size();
        // set the field null
        produits.setDescription(null);

        // Create the Produits, which fails.

        restProduitsMockMvc.perform(post("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isBadRequest());

        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProduits() throws Exception {
        // Initialize the database
        produitsRepository.saveAndFlush(produits);

        // Get all the produitsList
        restProduitsMockMvc.perform(get("/api/produits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produits.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProduit").value(hasItem(DEFAULT_NOM_PRODUIT.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getProduits() throws Exception {
        // Initialize the database
        produitsRepository.saveAndFlush(produits);

        // Get the produits
        restProduitsMockMvc.perform(get("/api/produits/{id}", produits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(produits.getId().intValue()))
            .andExpect(jsonPath("$.nomProduit").value(DEFAULT_NOM_PRODUIT.toString()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProduits() throws Exception {
        // Get the produits
        restProduitsMockMvc.perform(get("/api/produits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduits() throws Exception {
        // Initialize the database
        produitsRepository.saveAndFlush(produits);
        produitsSearchRepository.save(produits);
        int databaseSizeBeforeUpdate = produitsRepository.findAll().size();

        // Update the produits
        Produits updatedProduits = produitsRepository.findOne(produits.getId());
        // Disconnect from session so that the updates on updatedProduits are not directly saved in db
        em.detach(updatedProduits);
        updatedProduits
            .nomProduit(UPDATED_NOM_PRODUIT)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .description(UPDATED_DESCRIPTION);

        restProduitsMockMvc.perform(put("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduits)))
            .andExpect(status().isOk());

        // Validate the Produits in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeUpdate);
        Produits testProduits = produitsList.get(produitsList.size() - 1);
        assertThat(testProduits.getNomProduit()).isEqualTo(UPDATED_NOM_PRODUIT);
        assertThat(testProduits.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testProduits.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Produits in Elasticsearch
        Produits produitsEs = produitsSearchRepository.findOne(testProduits.getId());
        assertThat(produitsEs).isEqualToIgnoringGivenFields(testProduits);
    }

    @Test
    @Transactional
    public void updateNonExistingProduits() throws Exception {
        int databaseSizeBeforeUpdate = produitsRepository.findAll().size();

        // Create the Produits

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProduitsMockMvc.perform(put("/api/produits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(produits)))
            .andExpect(status().isCreated());

        // Validate the Produits in the database
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProduits() throws Exception {
        // Initialize the database
        produitsRepository.saveAndFlush(produits);
        produitsSearchRepository.save(produits);
        int databaseSizeBeforeDelete = produitsRepository.findAll().size();

        // Get the produits
        restProduitsMockMvc.perform(delete("/api/produits/{id}", produits.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean produitsExistsInEs = produitsSearchRepository.exists(produits.getId());
        assertThat(produitsExistsInEs).isFalse();

        // Validate the database is empty
        List<Produits> produitsList = produitsRepository.findAll();
        assertThat(produitsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProduits() throws Exception {
        // Initialize the database
        produitsRepository.saveAndFlush(produits);
        produitsSearchRepository.save(produits);

        // Search the produits
        restProduitsMockMvc.perform(get("/api/_search/produits?query=id:" + produits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produits.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProduit").value(hasItem(DEFAULT_NOM_PRODUIT.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produits.class);
        Produits produits1 = new Produits();
        produits1.setId(1L);
        Produits produits2 = new Produits();
        produits2.setId(produits1.getId());
        assertThat(produits1).isEqualTo(produits2);
        produits2.setId(2L);
        assertThat(produits1).isNotEqualTo(produits2);
        produits1.setId(null);
        assertThat(produits1).isNotEqualTo(produits2);
    }
}
