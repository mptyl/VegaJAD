package it.tylconsulting.vega.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.tylconsulting.vega.IntegrationTest;
import it.tylconsulting.vega.domain.QuestionnaireGroup;
import it.tylconsulting.vega.repository.QuestionnaireGroupRepository;
import it.tylconsulting.vega.service.dto.QuestionnaireGroupDTO;
import it.tylconsulting.vega.service.mapper.QuestionnaireGroupMapper;
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
 * Integration tests for the {@link QuestionnaireGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionnaireGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questionnaire-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionnaireGroupRepository questionnaireGroupRepository;

    @Autowired
    private QuestionnaireGroupMapper questionnaireGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionnaireGroupMockMvc;

    private QuestionnaireGroup questionnaireGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionnaireGroup createEntity(EntityManager em) {
        QuestionnaireGroup questionnaireGroup = new QuestionnaireGroup().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return questionnaireGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionnaireGroup createUpdatedEntity(EntityManager em) {
        QuestionnaireGroup questionnaireGroup = new QuestionnaireGroup().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return questionnaireGroup;
    }

    @BeforeEach
    public void initTest() {
        questionnaireGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionnaireGroup() throws Exception {
        int databaseSizeBeforeCreate = questionnaireGroupRepository.findAll().size();
        // Create the QuestionnaireGroup
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);
        restQuestionnaireGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionnaireGroup testQuestionnaireGroup = questionnaireGroupList.get(questionnaireGroupList.size() - 1);
        assertThat(testQuestionnaireGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionnaireGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createQuestionnaireGroupWithExistingId() throws Exception {
        // Create the QuestionnaireGroup with an existing ID
        questionnaireGroup.setId(1L);
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);

        int databaseSizeBeforeCreate = questionnaireGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionnaireGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionnaireGroups() throws Exception {
        // Initialize the database
        questionnaireGroupRepository.saveAndFlush(questionnaireGroup);

        // Get all the questionnaireGroupList
        restQuestionnaireGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaireGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getQuestionnaireGroup() throws Exception {
        // Initialize the database
        questionnaireGroupRepository.saveAndFlush(questionnaireGroup);

        // Get the questionnaireGroup
        restQuestionnaireGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, questionnaireGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionnaireGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingQuestionnaireGroup() throws Exception {
        // Get the questionnaireGroup
        restQuestionnaireGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionnaireGroup() throws Exception {
        // Initialize the database
        questionnaireGroupRepository.saveAndFlush(questionnaireGroup);

        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();

        // Update the questionnaireGroup
        QuestionnaireGroup updatedQuestionnaireGroup = questionnaireGroupRepository.findById(questionnaireGroup.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionnaireGroup are not directly saved in db
        em.detach(updatedQuestionnaireGroup);
        updatedQuestionnaireGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(updatedQuestionnaireGroup);

        restQuestionnaireGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionnaireGroupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
        QuestionnaireGroup testQuestionnaireGroup = questionnaireGroupList.get(questionnaireGroupList.size() - 1);
        assertThat(testQuestionnaireGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionnaireGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingQuestionnaireGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();
        questionnaireGroup.setId(count.incrementAndGet());

        // Create the QuestionnaireGroup
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionnaireGroupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionnaireGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();
        questionnaireGroup.setId(count.incrementAndGet());

        // Create the QuestionnaireGroup
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionnaireGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();
        questionnaireGroup.setId(count.incrementAndGet());

        // Create the QuestionnaireGroup
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionnaireGroupWithPatch() throws Exception {
        // Initialize the database
        questionnaireGroupRepository.saveAndFlush(questionnaireGroup);

        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();

        // Update the questionnaireGroup using partial update
        QuestionnaireGroup partialUpdatedQuestionnaireGroup = new QuestionnaireGroup();
        partialUpdatedQuestionnaireGroup.setId(questionnaireGroup.getId());

        partialUpdatedQuestionnaireGroup.description(UPDATED_DESCRIPTION);

        restQuestionnaireGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaireGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaireGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
        QuestionnaireGroup testQuestionnaireGroup = questionnaireGroupList.get(questionnaireGroupList.size() - 1);
        assertThat(testQuestionnaireGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionnaireGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateQuestionnaireGroupWithPatch() throws Exception {
        // Initialize the database
        questionnaireGroupRepository.saveAndFlush(questionnaireGroup);

        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();

        // Update the questionnaireGroup using partial update
        QuestionnaireGroup partialUpdatedQuestionnaireGroup = new QuestionnaireGroup();
        partialUpdatedQuestionnaireGroup.setId(questionnaireGroup.getId());

        partialUpdatedQuestionnaireGroup.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restQuestionnaireGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaireGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaireGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
        QuestionnaireGroup testQuestionnaireGroup = questionnaireGroupList.get(questionnaireGroupList.size() - 1);
        assertThat(testQuestionnaireGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionnaireGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionnaireGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();
        questionnaireGroup.setId(count.incrementAndGet());

        // Create the QuestionnaireGroup
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionnaireGroupDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionnaireGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();
        questionnaireGroup.setId(count.incrementAndGet());

        // Create the QuestionnaireGroup
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionnaireGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireGroupRepository.findAll().size();
        questionnaireGroup.setId(count.incrementAndGet());

        // Create the QuestionnaireGroup
        QuestionnaireGroupDTO questionnaireGroupDTO = questionnaireGroupMapper.toDto(questionnaireGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionnaireGroup in the database
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionnaireGroup() throws Exception {
        // Initialize the database
        questionnaireGroupRepository.saveAndFlush(questionnaireGroup);

        int databaseSizeBeforeDelete = questionnaireGroupRepository.findAll().size();

        // Delete the questionnaireGroup
        restQuestionnaireGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionnaireGroup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionnaireGroup> questionnaireGroupList = questionnaireGroupRepository.findAll();
        assertThat(questionnaireGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
