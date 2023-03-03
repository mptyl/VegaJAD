package it.tylconsulting.vega.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.tylconsulting.vega.IntegrationTest;
import it.tylconsulting.vega.domain.QuestionnaireProfile;
import it.tylconsulting.vega.repository.QuestionnaireProfileRepository;
import it.tylconsulting.vega.service.dto.QuestionnaireProfileDTO;
import it.tylconsulting.vega.service.mapper.QuestionnaireProfileMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionnaireProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionnaireProfileResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questionnaire-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionnaireProfileRepository questionnaireProfileRepository;

    @Autowired
    private QuestionnaireProfileMapper questionnaireProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionnaireProfileMockMvc;

    private QuestionnaireProfile questionnaireProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionnaireProfile createEntity(EntityManager em) {
        QuestionnaireProfile questionnaireProfile = new QuestionnaireProfile().description(DEFAULT_DESCRIPTION);
        return questionnaireProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionnaireProfile createUpdatedEntity(EntityManager em) {
        QuestionnaireProfile questionnaireProfile = new QuestionnaireProfile().description(UPDATED_DESCRIPTION);
        return questionnaireProfile;
    }

    @BeforeEach
    public void initTest() {
        questionnaireProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionnaireProfile() throws Exception {
        int databaseSizeBeforeCreate = questionnaireProfileRepository.findAll().size();
        // Create the QuestionnaireProfile
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);
        restQuestionnaireProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionnaireProfile testQuestionnaireProfile = questionnaireProfileList.get(questionnaireProfileList.size() - 1);
        assertThat(testQuestionnaireProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createQuestionnaireProfileWithExistingId() throws Exception {
        // Create the QuestionnaireProfile with an existing ID
        questionnaireProfile.setId(1L);
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);

        int databaseSizeBeforeCreate = questionnaireProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionnaireProfileMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionnaireProfiles() throws Exception {
        // Initialize the database
        questionnaireProfileRepository.saveAndFlush(questionnaireProfile);

        // Get all the questionnaireProfileList
        restQuestionnaireProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaireProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getQuestionnaireProfile() throws Exception {
        // Initialize the database
        questionnaireProfileRepository.saveAndFlush(questionnaireProfile);

        // Get the questionnaireProfile
        restQuestionnaireProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, questionnaireProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionnaireProfile.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingQuestionnaireProfile() throws Exception {
        // Get the questionnaireProfile
        restQuestionnaireProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionnaireProfile() throws Exception {
        // Initialize the database
        questionnaireProfileRepository.saveAndFlush(questionnaireProfile);

        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();

        // Update the questionnaireProfile
        QuestionnaireProfile updatedQuestionnaireProfile = questionnaireProfileRepository.findById(questionnaireProfile.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionnaireProfile are not directly saved in db
        em.detach(updatedQuestionnaireProfile);
        updatedQuestionnaireProfile.description(UPDATED_DESCRIPTION);
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(updatedQuestionnaireProfile);

        restQuestionnaireProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionnaireProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
        QuestionnaireProfile testQuestionnaireProfile = questionnaireProfileList.get(questionnaireProfileList.size() - 1);
        assertThat(testQuestionnaireProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingQuestionnaireProfile() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();
        questionnaireProfile.setId(count.incrementAndGet());

        // Create the QuestionnaireProfile
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionnaireProfileDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionnaireProfile() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();
        questionnaireProfile.setId(count.incrementAndGet());

        // Create the QuestionnaireProfile
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionnaireProfile() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();
        questionnaireProfile.setId(count.incrementAndGet());

        // Create the QuestionnaireProfile
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireProfileMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionnaireProfileWithPatch() throws Exception {
        // Initialize the database
        questionnaireProfileRepository.saveAndFlush(questionnaireProfile);

        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();

        // Update the questionnaireProfile using partial update
        QuestionnaireProfile partialUpdatedQuestionnaireProfile = new QuestionnaireProfile();
        partialUpdatedQuestionnaireProfile.setId(questionnaireProfile.getId());

        partialUpdatedQuestionnaireProfile.description(UPDATED_DESCRIPTION);

        restQuestionnaireProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaireProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaireProfile))
            )
            .andExpect(status().isOk());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
        QuestionnaireProfile testQuestionnaireProfile = questionnaireProfileList.get(questionnaireProfileList.size() - 1);
        assertThat(testQuestionnaireProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateQuestionnaireProfileWithPatch() throws Exception {
        // Initialize the database
        questionnaireProfileRepository.saveAndFlush(questionnaireProfile);

        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();

        // Update the questionnaireProfile using partial update
        QuestionnaireProfile partialUpdatedQuestionnaireProfile = new QuestionnaireProfile();
        partialUpdatedQuestionnaireProfile.setId(questionnaireProfile.getId());

        partialUpdatedQuestionnaireProfile.description(UPDATED_DESCRIPTION);

        restQuestionnaireProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaireProfile.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaireProfile))
            )
            .andExpect(status().isOk());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
        QuestionnaireProfile testQuestionnaireProfile = questionnaireProfileList.get(questionnaireProfileList.size() - 1);
        assertThat(testQuestionnaireProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionnaireProfile() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();
        questionnaireProfile.setId(count.incrementAndGet());

        // Create the QuestionnaireProfile
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionnaireProfileDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionnaireProfile() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();
        questionnaireProfile.setId(count.incrementAndGet());

        // Create the QuestionnaireProfile
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionnaireProfile() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireProfileRepository.findAll().size();
        questionnaireProfile.setId(count.incrementAndGet());

        // Create the QuestionnaireProfile
        QuestionnaireProfileDTO questionnaireProfileDTO = questionnaireProfileMapper.toDto(questionnaireProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireProfileMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionnaireProfile in the database
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionnaireProfile() throws Exception {
        // Initialize the database
        questionnaireProfileRepository.saveAndFlush(questionnaireProfile);

        int databaseSizeBeforeDelete = questionnaireProfileRepository.findAll().size();

        // Delete the questionnaireProfile
        restQuestionnaireProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionnaireProfile.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionnaireProfile> questionnaireProfileList = questionnaireProfileRepository.findAll();
        assertThat(questionnaireProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
