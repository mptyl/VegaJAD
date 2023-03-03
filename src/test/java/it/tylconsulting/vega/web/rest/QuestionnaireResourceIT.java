package it.tylconsulting.vega.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.tylconsulting.vega.IntegrationTest;
import it.tylconsulting.vega.domain.Questionnaire;
import it.tylconsulting.vega.domain.QuestionnaireGroup;
import it.tylconsulting.vega.domain.QuestionnaireProfile;
import it.tylconsulting.vega.domain.enumeration.QuestionnaireType;
import it.tylconsulting.vega.repository.QuestionnaireRepository;
import it.tylconsulting.vega.service.criteria.QuestionnaireCriteria;
import it.tylconsulting.vega.service.dto.QuestionnaireDTO;
import it.tylconsulting.vega.service.mapper.QuestionnaireMapper;
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
 * Integration tests for the {@link QuestionnaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionnaireResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_ALT = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_ALT = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTIONS = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMPILATION_TIME = 1;
    private static final Integer UPDATED_COMPILATION_TIME = 2;
    private static final Integer SMALLER_COMPILATION_TIME = 1 - 1;

    private static final Integer DEFAULT_FORCED_TERMINATION_TIME = 1;
    private static final Integer UPDATED_FORCED_TERMINATION_TIME = 2;
    private static final Integer SMALLER_FORCED_TERMINATION_TIME = 1 - 1;

    private static final Integer DEFAULT_USED_SECONDS = 1;
    private static final Integer UPDATED_USED_SECONDS = 2;
    private static final Integer SMALLER_USED_SECONDS = 1 - 1;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final String DEFAULT_XML = "AAAAAAAAAA";
    private static final String UPDATED_XML = "BBBBBBBBBB";

    private static final String DEFAULT_JSON = "AAAAAAAAAA";
    private static final String UPDATED_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_SAVE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SAVE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SEARCH_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SEARCH_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT_TO_EVALUATION = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_TO_EVALUATION = "BBBBBBBBBB";

    private static final QuestionnaireType DEFAULT_QUESTIONNAIRE_TYPE = QuestionnaireType.ANCORE;
    private static final QuestionnaireType UPDATED_QUESTIONNAIRE_TYPE = QuestionnaireType.AUTOVALUTAZIONI;

    private static final Integer DEFAULT_ATTACHMENTS = 1;
    private static final Integer UPDATED_ATTACHMENTS = 2;
    private static final Integer SMALLER_ATTACHMENTS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/questionnaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionnaireMockMvc;

    private Questionnaire questionnaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .name(DEFAULT_NAME)
            .version(DEFAULT_VERSION)
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .notes(DEFAULT_NOTES)
            .image(DEFAULT_IMAGE)
            .imageAlt(DEFAULT_IMAGE_ALT)
            .instructions(DEFAULT_INSTRUCTIONS)
            .compilationTime(DEFAULT_COMPILATION_TIME)
            .forcedTerminationTime(DEFAULT_FORCED_TERMINATION_TIME)
            .usedSeconds(DEFAULT_USED_SECONDS)
            .status(DEFAULT_STATUS)
            .xml(DEFAULT_XML)
            .json(DEFAULT_JSON)
            .saveText(DEFAULT_SAVE_TEXT)
            .searchText(DEFAULT_SEARCH_TEXT)
            .subjectToEvaluation(DEFAULT_SUBJECT_TO_EVALUATION)
            .questionnaireType(DEFAULT_QUESTIONNAIRE_TYPE)
            .attachments(DEFAULT_ATTACHMENTS);
        return questionnaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createUpdatedEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .notes(UPDATED_NOTES)
            .image(UPDATED_IMAGE)
            .imageAlt(UPDATED_IMAGE_ALT)
            .instructions(UPDATED_INSTRUCTIONS)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .forcedTerminationTime(UPDATED_FORCED_TERMINATION_TIME)
            .usedSeconds(UPDATED_USED_SECONDS)
            .status(UPDATED_STATUS)
            .xml(UPDATED_XML)
            .json(UPDATED_JSON)
            .saveText(UPDATED_SAVE_TEXT)
            .searchText(UPDATED_SEARCH_TEXT)
            .subjectToEvaluation(UPDATED_SUBJECT_TO_EVALUATION)
            .questionnaireType(UPDATED_QUESTIONNAIRE_TYPE)
            .attachments(UPDATED_ATTACHMENTS);
        return questionnaire;
    }

    @BeforeEach
    public void initTest() {
        questionnaire = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionnaire() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();
        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);
        restQuestionnaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionnaire.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testQuestionnaire.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuestionnaire.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testQuestionnaire.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testQuestionnaire.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testQuestionnaire.getImageAlt()).isEqualTo(DEFAULT_IMAGE_ALT);
        assertThat(testQuestionnaire.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
        assertThat(testQuestionnaire.getCompilationTime()).isEqualTo(DEFAULT_COMPILATION_TIME);
        assertThat(testQuestionnaire.getForcedTerminationTime()).isEqualTo(DEFAULT_FORCED_TERMINATION_TIME);
        assertThat(testQuestionnaire.getUsedSeconds()).isEqualTo(DEFAULT_USED_SECONDS);
        assertThat(testQuestionnaire.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuestionnaire.getXml()).isEqualTo(DEFAULT_XML);
        assertThat(testQuestionnaire.getJson()).isEqualTo(DEFAULT_JSON);
        assertThat(testQuestionnaire.getSaveText()).isEqualTo(DEFAULT_SAVE_TEXT);
        assertThat(testQuestionnaire.getSearchText()).isEqualTo(DEFAULT_SEARCH_TEXT);
        assertThat(testQuestionnaire.getSubjectToEvaluation()).isEqualTo(DEFAULT_SUBJECT_TO_EVALUATION);
        assertThat(testQuestionnaire.getQuestionnaireType()).isEqualTo(DEFAULT_QUESTIONNAIRE_TYPE);
        assertThat(testQuestionnaire.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
    }

    @Test
    @Transactional
    void createQuestionnaireWithExistingId() throws Exception {
        // Create the Questionnaire with an existing ID
        questionnaire.setId(1L);
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionnaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionnaires() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].imageAlt").value(hasItem(DEFAULT_IMAGE_ALT)))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(DEFAULT_COMPILATION_TIME)))
            .andExpect(jsonPath("$.[*].forcedTerminationTime").value(hasItem(DEFAULT_FORCED_TERMINATION_TIME)))
            .andExpect(jsonPath("$.[*].usedSeconds").value(hasItem(DEFAULT_USED_SECONDS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].xml").value(hasItem(DEFAULT_XML)))
            .andExpect(jsonPath("$.[*].json").value(hasItem(DEFAULT_JSON)))
            .andExpect(jsonPath("$.[*].saveText").value(hasItem(DEFAULT_SAVE_TEXT)))
            .andExpect(jsonPath("$.[*].searchText").value(hasItem(DEFAULT_SEARCH_TEXT)))
            .andExpect(jsonPath("$.[*].subjectToEvaluation").value(hasItem(DEFAULT_SUBJECT_TO_EVALUATION)))
            .andExpect(jsonPath("$.[*].questionnaireType").value(hasItem(DEFAULT_QUESTIONNAIRE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS)));
    }

    @Test
    @Transactional
    void getQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get the questionnaire
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL_ID, questionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionnaire.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.imageAlt").value(DEFAULT_IMAGE_ALT))
            .andExpect(jsonPath("$.instructions").value(DEFAULT_INSTRUCTIONS))
            .andExpect(jsonPath("$.compilationTime").value(DEFAULT_COMPILATION_TIME))
            .andExpect(jsonPath("$.forcedTerminationTime").value(DEFAULT_FORCED_TERMINATION_TIME))
            .andExpect(jsonPath("$.usedSeconds").value(DEFAULT_USED_SECONDS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.xml").value(DEFAULT_XML))
            .andExpect(jsonPath("$.json").value(DEFAULT_JSON))
            .andExpect(jsonPath("$.saveText").value(DEFAULT_SAVE_TEXT))
            .andExpect(jsonPath("$.searchText").value(DEFAULT_SEARCH_TEXT))
            .andExpect(jsonPath("$.subjectToEvaluation").value(DEFAULT_SUBJECT_TO_EVALUATION))
            .andExpect(jsonPath("$.questionnaireType").value(DEFAULT_QUESTIONNAIRE_TYPE.toString()))
            .andExpect(jsonPath("$.attachments").value(DEFAULT_ATTACHMENTS));
    }

    @Test
    @Transactional
    void getQuestionnairesByIdFiltering() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        Long id = questionnaire.getId();

        defaultQuestionnaireShouldBeFound("id.equals=" + id);
        defaultQuestionnaireShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionnaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionnaireShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionnaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionnaireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where name equals to DEFAULT_NAME
        defaultQuestionnaireShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the questionnaireList where name equals to UPDATED_NAME
        defaultQuestionnaireShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where name in DEFAULT_NAME or UPDATED_NAME
        defaultQuestionnaireShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the questionnaireList where name equals to UPDATED_NAME
        defaultQuestionnaireShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where name is not null
        defaultQuestionnaireShouldBeFound("name.specified=true");

        // Get all the questionnaireList where name is null
        defaultQuestionnaireShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNameContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where name contains DEFAULT_NAME
        defaultQuestionnaireShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the questionnaireList where name contains UPDATED_NAME
        defaultQuestionnaireShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where name does not contain DEFAULT_NAME
        defaultQuestionnaireShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the questionnaireList where name does not contain UPDATED_NAME
        defaultQuestionnaireShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where version equals to DEFAULT_VERSION
        defaultQuestionnaireShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the questionnaireList where version equals to UPDATED_VERSION
        defaultQuestionnaireShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultQuestionnaireShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the questionnaireList where version equals to UPDATED_VERSION
        defaultQuestionnaireShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where version is not null
        defaultQuestionnaireShouldBeFound("version.specified=true");

        // Get all the questionnaireList where version is null
        defaultQuestionnaireShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByVersionContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where version contains DEFAULT_VERSION
        defaultQuestionnaireShouldBeFound("version.contains=" + DEFAULT_VERSION);

        // Get all the questionnaireList where version contains UPDATED_VERSION
        defaultQuestionnaireShouldNotBeFound("version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where version does not contain DEFAULT_VERSION
        defaultQuestionnaireShouldNotBeFound("version.doesNotContain=" + DEFAULT_VERSION);

        // Get all the questionnaireList where version does not contain UPDATED_VERSION
        defaultQuestionnaireShouldBeFound("version.doesNotContain=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where title equals to DEFAULT_TITLE
        defaultQuestionnaireShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the questionnaireList where title equals to UPDATED_TITLE
        defaultQuestionnaireShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultQuestionnaireShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the questionnaireList where title equals to UPDATED_TITLE
        defaultQuestionnaireShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where title is not null
        defaultQuestionnaireShouldBeFound("title.specified=true");

        // Get all the questionnaireList where title is null
        defaultQuestionnaireShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByTitleContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where title contains DEFAULT_TITLE
        defaultQuestionnaireShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the questionnaireList where title contains UPDATED_TITLE
        defaultQuestionnaireShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where title does not contain DEFAULT_TITLE
        defaultQuestionnaireShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the questionnaireList where title does not contain UPDATED_TITLE
        defaultQuestionnaireShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subTitle equals to DEFAULT_SUB_TITLE
        defaultQuestionnaireShouldBeFound("subTitle.equals=" + DEFAULT_SUB_TITLE);

        // Get all the questionnaireList where subTitle equals to UPDATED_SUB_TITLE
        defaultQuestionnaireShouldNotBeFound("subTitle.equals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubTitleIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subTitle in DEFAULT_SUB_TITLE or UPDATED_SUB_TITLE
        defaultQuestionnaireShouldBeFound("subTitle.in=" + DEFAULT_SUB_TITLE + "," + UPDATED_SUB_TITLE);

        // Get all the questionnaireList where subTitle equals to UPDATED_SUB_TITLE
        defaultQuestionnaireShouldNotBeFound("subTitle.in=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subTitle is not null
        defaultQuestionnaireShouldBeFound("subTitle.specified=true");

        // Get all the questionnaireList where subTitle is null
        defaultQuestionnaireShouldNotBeFound("subTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubTitleContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subTitle contains DEFAULT_SUB_TITLE
        defaultQuestionnaireShouldBeFound("subTitle.contains=" + DEFAULT_SUB_TITLE);

        // Get all the questionnaireList where subTitle contains UPDATED_SUB_TITLE
        defaultQuestionnaireShouldNotBeFound("subTitle.contains=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubTitleNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subTitle does not contain DEFAULT_SUB_TITLE
        defaultQuestionnaireShouldNotBeFound("subTitle.doesNotContain=" + DEFAULT_SUB_TITLE);

        // Get all the questionnaireList where subTitle does not contain UPDATED_SUB_TITLE
        defaultQuestionnaireShouldBeFound("subTitle.doesNotContain=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where notes equals to DEFAULT_NOTES
        defaultQuestionnaireShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the questionnaireList where notes equals to UPDATED_NOTES
        defaultQuestionnaireShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultQuestionnaireShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the questionnaireList where notes equals to UPDATED_NOTES
        defaultQuestionnaireShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where notes is not null
        defaultQuestionnaireShouldBeFound("notes.specified=true");

        // Get all the questionnaireList where notes is null
        defaultQuestionnaireShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNotesContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where notes contains DEFAULT_NOTES
        defaultQuestionnaireShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the questionnaireList where notes contains UPDATED_NOTES
        defaultQuestionnaireShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where notes does not contain DEFAULT_NOTES
        defaultQuestionnaireShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the questionnaireList where notes does not contain UPDATED_NOTES
        defaultQuestionnaireShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where image equals to DEFAULT_IMAGE
        defaultQuestionnaireShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the questionnaireList where image equals to UPDATED_IMAGE
        defaultQuestionnaireShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultQuestionnaireShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the questionnaireList where image equals to UPDATED_IMAGE
        defaultQuestionnaireShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where image is not null
        defaultQuestionnaireShouldBeFound("image.specified=true");

        // Get all the questionnaireList where image is null
        defaultQuestionnaireShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where image contains DEFAULT_IMAGE
        defaultQuestionnaireShouldBeFound("image.contains=" + DEFAULT_IMAGE);

        // Get all the questionnaireList where image contains UPDATED_IMAGE
        defaultQuestionnaireShouldNotBeFound("image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where image does not contain DEFAULT_IMAGE
        defaultQuestionnaireShouldNotBeFound("image.doesNotContain=" + DEFAULT_IMAGE);

        // Get all the questionnaireList where image does not contain UPDATED_IMAGE
        defaultQuestionnaireShouldBeFound("image.doesNotContain=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageAltIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where imageAlt equals to DEFAULT_IMAGE_ALT
        defaultQuestionnaireShouldBeFound("imageAlt.equals=" + DEFAULT_IMAGE_ALT);

        // Get all the questionnaireList where imageAlt equals to UPDATED_IMAGE_ALT
        defaultQuestionnaireShouldNotBeFound("imageAlt.equals=" + UPDATED_IMAGE_ALT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageAltIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where imageAlt in DEFAULT_IMAGE_ALT or UPDATED_IMAGE_ALT
        defaultQuestionnaireShouldBeFound("imageAlt.in=" + DEFAULT_IMAGE_ALT + "," + UPDATED_IMAGE_ALT);

        // Get all the questionnaireList where imageAlt equals to UPDATED_IMAGE_ALT
        defaultQuestionnaireShouldNotBeFound("imageAlt.in=" + UPDATED_IMAGE_ALT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageAltIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where imageAlt is not null
        defaultQuestionnaireShouldBeFound("imageAlt.specified=true");

        // Get all the questionnaireList where imageAlt is null
        defaultQuestionnaireShouldNotBeFound("imageAlt.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageAltContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where imageAlt contains DEFAULT_IMAGE_ALT
        defaultQuestionnaireShouldBeFound("imageAlt.contains=" + DEFAULT_IMAGE_ALT);

        // Get all the questionnaireList where imageAlt contains UPDATED_IMAGE_ALT
        defaultQuestionnaireShouldNotBeFound("imageAlt.contains=" + UPDATED_IMAGE_ALT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByImageAltNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where imageAlt does not contain DEFAULT_IMAGE_ALT
        defaultQuestionnaireShouldNotBeFound("imageAlt.doesNotContain=" + DEFAULT_IMAGE_ALT);

        // Get all the questionnaireList where imageAlt does not contain UPDATED_IMAGE_ALT
        defaultQuestionnaireShouldBeFound("imageAlt.doesNotContain=" + UPDATED_IMAGE_ALT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByInstructionsIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where instructions equals to DEFAULT_INSTRUCTIONS
        defaultQuestionnaireShouldBeFound("instructions.equals=" + DEFAULT_INSTRUCTIONS);

        // Get all the questionnaireList where instructions equals to UPDATED_INSTRUCTIONS
        defaultQuestionnaireShouldNotBeFound("instructions.equals=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByInstructionsIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where instructions in DEFAULT_INSTRUCTIONS or UPDATED_INSTRUCTIONS
        defaultQuestionnaireShouldBeFound("instructions.in=" + DEFAULT_INSTRUCTIONS + "," + UPDATED_INSTRUCTIONS);

        // Get all the questionnaireList where instructions equals to UPDATED_INSTRUCTIONS
        defaultQuestionnaireShouldNotBeFound("instructions.in=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByInstructionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where instructions is not null
        defaultQuestionnaireShouldBeFound("instructions.specified=true");

        // Get all the questionnaireList where instructions is null
        defaultQuestionnaireShouldNotBeFound("instructions.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByInstructionsContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where instructions contains DEFAULT_INSTRUCTIONS
        defaultQuestionnaireShouldBeFound("instructions.contains=" + DEFAULT_INSTRUCTIONS);

        // Get all the questionnaireList where instructions contains UPDATED_INSTRUCTIONS
        defaultQuestionnaireShouldNotBeFound("instructions.contains=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByInstructionsNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where instructions does not contain DEFAULT_INSTRUCTIONS
        defaultQuestionnaireShouldNotBeFound("instructions.doesNotContain=" + DEFAULT_INSTRUCTIONS);

        // Get all the questionnaireList where instructions does not contain UPDATED_INSTRUCTIONS
        defaultQuestionnaireShouldBeFound("instructions.doesNotContain=" + UPDATED_INSTRUCTIONS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByCompilationTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where compilationTime equals to DEFAULT_COMPILATION_TIME
        defaultQuestionnaireShouldBeFound("compilationTime.equals=" + DEFAULT_COMPILATION_TIME);

        // Get all the questionnaireList where compilationTime equals to UPDATED_COMPILATION_TIME
        defaultQuestionnaireShouldNotBeFound("compilationTime.equals=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByCompilationTimeIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where compilationTime in DEFAULT_COMPILATION_TIME or UPDATED_COMPILATION_TIME
        defaultQuestionnaireShouldBeFound("compilationTime.in=" + DEFAULT_COMPILATION_TIME + "," + UPDATED_COMPILATION_TIME);

        // Get all the questionnaireList where compilationTime equals to UPDATED_COMPILATION_TIME
        defaultQuestionnaireShouldNotBeFound("compilationTime.in=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByCompilationTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where compilationTime is not null
        defaultQuestionnaireShouldBeFound("compilationTime.specified=true");

        // Get all the questionnaireList where compilationTime is null
        defaultQuestionnaireShouldNotBeFound("compilationTime.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByCompilationTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where compilationTime is greater than or equal to DEFAULT_COMPILATION_TIME
        defaultQuestionnaireShouldBeFound("compilationTime.greaterThanOrEqual=" + DEFAULT_COMPILATION_TIME);

        // Get all the questionnaireList where compilationTime is greater than or equal to UPDATED_COMPILATION_TIME
        defaultQuestionnaireShouldNotBeFound("compilationTime.greaterThanOrEqual=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByCompilationTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where compilationTime is less than or equal to DEFAULT_COMPILATION_TIME
        defaultQuestionnaireShouldBeFound("compilationTime.lessThanOrEqual=" + DEFAULT_COMPILATION_TIME);

        // Get all the questionnaireList where compilationTime is less than or equal to SMALLER_COMPILATION_TIME
        defaultQuestionnaireShouldNotBeFound("compilationTime.lessThanOrEqual=" + SMALLER_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByCompilationTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where compilationTime is less than DEFAULT_COMPILATION_TIME
        defaultQuestionnaireShouldNotBeFound("compilationTime.lessThan=" + DEFAULT_COMPILATION_TIME);

        // Get all the questionnaireList where compilationTime is less than UPDATED_COMPILATION_TIME
        defaultQuestionnaireShouldBeFound("compilationTime.lessThan=" + UPDATED_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByCompilationTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where compilationTime is greater than DEFAULT_COMPILATION_TIME
        defaultQuestionnaireShouldNotBeFound("compilationTime.greaterThan=" + DEFAULT_COMPILATION_TIME);

        // Get all the questionnaireList where compilationTime is greater than SMALLER_COMPILATION_TIME
        defaultQuestionnaireShouldBeFound("compilationTime.greaterThan=" + SMALLER_COMPILATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByForcedTerminationTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where forcedTerminationTime equals to DEFAULT_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldBeFound("forcedTerminationTime.equals=" + DEFAULT_FORCED_TERMINATION_TIME);

        // Get all the questionnaireList where forcedTerminationTime equals to UPDATED_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldNotBeFound("forcedTerminationTime.equals=" + UPDATED_FORCED_TERMINATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByForcedTerminationTimeIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where forcedTerminationTime in DEFAULT_FORCED_TERMINATION_TIME or UPDATED_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldBeFound(
            "forcedTerminationTime.in=" + DEFAULT_FORCED_TERMINATION_TIME + "," + UPDATED_FORCED_TERMINATION_TIME
        );

        // Get all the questionnaireList where forcedTerminationTime equals to UPDATED_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldNotBeFound("forcedTerminationTime.in=" + UPDATED_FORCED_TERMINATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByForcedTerminationTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where forcedTerminationTime is not null
        defaultQuestionnaireShouldBeFound("forcedTerminationTime.specified=true");

        // Get all the questionnaireList where forcedTerminationTime is null
        defaultQuestionnaireShouldNotBeFound("forcedTerminationTime.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByForcedTerminationTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where forcedTerminationTime is greater than or equal to DEFAULT_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldBeFound("forcedTerminationTime.greaterThanOrEqual=" + DEFAULT_FORCED_TERMINATION_TIME);

        // Get all the questionnaireList where forcedTerminationTime is greater than or equal to UPDATED_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldNotBeFound("forcedTerminationTime.greaterThanOrEqual=" + UPDATED_FORCED_TERMINATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByForcedTerminationTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where forcedTerminationTime is less than or equal to DEFAULT_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldBeFound("forcedTerminationTime.lessThanOrEqual=" + DEFAULT_FORCED_TERMINATION_TIME);

        // Get all the questionnaireList where forcedTerminationTime is less than or equal to SMALLER_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldNotBeFound("forcedTerminationTime.lessThanOrEqual=" + SMALLER_FORCED_TERMINATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByForcedTerminationTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where forcedTerminationTime is less than DEFAULT_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldNotBeFound("forcedTerminationTime.lessThan=" + DEFAULT_FORCED_TERMINATION_TIME);

        // Get all the questionnaireList where forcedTerminationTime is less than UPDATED_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldBeFound("forcedTerminationTime.lessThan=" + UPDATED_FORCED_TERMINATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByForcedTerminationTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where forcedTerminationTime is greater than DEFAULT_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldNotBeFound("forcedTerminationTime.greaterThan=" + DEFAULT_FORCED_TERMINATION_TIME);

        // Get all the questionnaireList where forcedTerminationTime is greater than SMALLER_FORCED_TERMINATION_TIME
        defaultQuestionnaireShouldBeFound("forcedTerminationTime.greaterThan=" + SMALLER_FORCED_TERMINATION_TIME);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByUsedSecondsIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where usedSeconds equals to DEFAULT_USED_SECONDS
        defaultQuestionnaireShouldBeFound("usedSeconds.equals=" + DEFAULT_USED_SECONDS);

        // Get all the questionnaireList where usedSeconds equals to UPDATED_USED_SECONDS
        defaultQuestionnaireShouldNotBeFound("usedSeconds.equals=" + UPDATED_USED_SECONDS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByUsedSecondsIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where usedSeconds in DEFAULT_USED_SECONDS or UPDATED_USED_SECONDS
        defaultQuestionnaireShouldBeFound("usedSeconds.in=" + DEFAULT_USED_SECONDS + "," + UPDATED_USED_SECONDS);

        // Get all the questionnaireList where usedSeconds equals to UPDATED_USED_SECONDS
        defaultQuestionnaireShouldNotBeFound("usedSeconds.in=" + UPDATED_USED_SECONDS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByUsedSecondsIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where usedSeconds is not null
        defaultQuestionnaireShouldBeFound("usedSeconds.specified=true");

        // Get all the questionnaireList where usedSeconds is null
        defaultQuestionnaireShouldNotBeFound("usedSeconds.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByUsedSecondsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where usedSeconds is greater than or equal to DEFAULT_USED_SECONDS
        defaultQuestionnaireShouldBeFound("usedSeconds.greaterThanOrEqual=" + DEFAULT_USED_SECONDS);

        // Get all the questionnaireList where usedSeconds is greater than or equal to UPDATED_USED_SECONDS
        defaultQuestionnaireShouldNotBeFound("usedSeconds.greaterThanOrEqual=" + UPDATED_USED_SECONDS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByUsedSecondsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where usedSeconds is less than or equal to DEFAULT_USED_SECONDS
        defaultQuestionnaireShouldBeFound("usedSeconds.lessThanOrEqual=" + DEFAULT_USED_SECONDS);

        // Get all the questionnaireList where usedSeconds is less than or equal to SMALLER_USED_SECONDS
        defaultQuestionnaireShouldNotBeFound("usedSeconds.lessThanOrEqual=" + SMALLER_USED_SECONDS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByUsedSecondsIsLessThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where usedSeconds is less than DEFAULT_USED_SECONDS
        defaultQuestionnaireShouldNotBeFound("usedSeconds.lessThan=" + DEFAULT_USED_SECONDS);

        // Get all the questionnaireList where usedSeconds is less than UPDATED_USED_SECONDS
        defaultQuestionnaireShouldBeFound("usedSeconds.lessThan=" + UPDATED_USED_SECONDS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByUsedSecondsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where usedSeconds is greater than DEFAULT_USED_SECONDS
        defaultQuestionnaireShouldNotBeFound("usedSeconds.greaterThan=" + DEFAULT_USED_SECONDS);

        // Get all the questionnaireList where usedSeconds is greater than SMALLER_USED_SECONDS
        defaultQuestionnaireShouldBeFound("usedSeconds.greaterThan=" + SMALLER_USED_SECONDS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where status equals to DEFAULT_STATUS
        defaultQuestionnaireShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the questionnaireList where status equals to UPDATED_STATUS
        defaultQuestionnaireShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultQuestionnaireShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the questionnaireList where status equals to UPDATED_STATUS
        defaultQuestionnaireShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where status is not null
        defaultQuestionnaireShouldBeFound("status.specified=true");

        // Get all the questionnaireList where status is null
        defaultQuestionnaireShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where status is greater than or equal to DEFAULT_STATUS
        defaultQuestionnaireShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the questionnaireList where status is greater than or equal to UPDATED_STATUS
        defaultQuestionnaireShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where status is less than or equal to DEFAULT_STATUS
        defaultQuestionnaireShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the questionnaireList where status is less than or equal to SMALLER_STATUS
        defaultQuestionnaireShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where status is less than DEFAULT_STATUS
        defaultQuestionnaireShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the questionnaireList where status is less than UPDATED_STATUS
        defaultQuestionnaireShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where status is greater than DEFAULT_STATUS
        defaultQuestionnaireShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the questionnaireList where status is greater than SMALLER_STATUS
        defaultQuestionnaireShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByXmlIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where xml equals to DEFAULT_XML
        defaultQuestionnaireShouldBeFound("xml.equals=" + DEFAULT_XML);

        // Get all the questionnaireList where xml equals to UPDATED_XML
        defaultQuestionnaireShouldNotBeFound("xml.equals=" + UPDATED_XML);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByXmlIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where xml in DEFAULT_XML or UPDATED_XML
        defaultQuestionnaireShouldBeFound("xml.in=" + DEFAULT_XML + "," + UPDATED_XML);

        // Get all the questionnaireList where xml equals to UPDATED_XML
        defaultQuestionnaireShouldNotBeFound("xml.in=" + UPDATED_XML);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByXmlIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where xml is not null
        defaultQuestionnaireShouldBeFound("xml.specified=true");

        // Get all the questionnaireList where xml is null
        defaultQuestionnaireShouldNotBeFound("xml.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByXmlContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where xml contains DEFAULT_XML
        defaultQuestionnaireShouldBeFound("xml.contains=" + DEFAULT_XML);

        // Get all the questionnaireList where xml contains UPDATED_XML
        defaultQuestionnaireShouldNotBeFound("xml.contains=" + UPDATED_XML);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByXmlNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where xml does not contain DEFAULT_XML
        defaultQuestionnaireShouldNotBeFound("xml.doesNotContain=" + DEFAULT_XML);

        // Get all the questionnaireList where xml does not contain UPDATED_XML
        defaultQuestionnaireShouldBeFound("xml.doesNotContain=" + UPDATED_XML);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByJsonIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where json equals to DEFAULT_JSON
        defaultQuestionnaireShouldBeFound("json.equals=" + DEFAULT_JSON);

        // Get all the questionnaireList where json equals to UPDATED_JSON
        defaultQuestionnaireShouldNotBeFound("json.equals=" + UPDATED_JSON);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByJsonIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where json in DEFAULT_JSON or UPDATED_JSON
        defaultQuestionnaireShouldBeFound("json.in=" + DEFAULT_JSON + "," + UPDATED_JSON);

        // Get all the questionnaireList where json equals to UPDATED_JSON
        defaultQuestionnaireShouldNotBeFound("json.in=" + UPDATED_JSON);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByJsonIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where json is not null
        defaultQuestionnaireShouldBeFound("json.specified=true");

        // Get all the questionnaireList where json is null
        defaultQuestionnaireShouldNotBeFound("json.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByJsonContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where json contains DEFAULT_JSON
        defaultQuestionnaireShouldBeFound("json.contains=" + DEFAULT_JSON);

        // Get all the questionnaireList where json contains UPDATED_JSON
        defaultQuestionnaireShouldNotBeFound("json.contains=" + UPDATED_JSON);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByJsonNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where json does not contain DEFAULT_JSON
        defaultQuestionnaireShouldNotBeFound("json.doesNotContain=" + DEFAULT_JSON);

        // Get all the questionnaireList where json does not contain UPDATED_JSON
        defaultQuestionnaireShouldBeFound("json.doesNotContain=" + UPDATED_JSON);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySaveTextIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where saveText equals to DEFAULT_SAVE_TEXT
        defaultQuestionnaireShouldBeFound("saveText.equals=" + DEFAULT_SAVE_TEXT);

        // Get all the questionnaireList where saveText equals to UPDATED_SAVE_TEXT
        defaultQuestionnaireShouldNotBeFound("saveText.equals=" + UPDATED_SAVE_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySaveTextIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where saveText in DEFAULT_SAVE_TEXT or UPDATED_SAVE_TEXT
        defaultQuestionnaireShouldBeFound("saveText.in=" + DEFAULT_SAVE_TEXT + "," + UPDATED_SAVE_TEXT);

        // Get all the questionnaireList where saveText equals to UPDATED_SAVE_TEXT
        defaultQuestionnaireShouldNotBeFound("saveText.in=" + UPDATED_SAVE_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySaveTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where saveText is not null
        defaultQuestionnaireShouldBeFound("saveText.specified=true");

        // Get all the questionnaireList where saveText is null
        defaultQuestionnaireShouldNotBeFound("saveText.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySaveTextContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where saveText contains DEFAULT_SAVE_TEXT
        defaultQuestionnaireShouldBeFound("saveText.contains=" + DEFAULT_SAVE_TEXT);

        // Get all the questionnaireList where saveText contains UPDATED_SAVE_TEXT
        defaultQuestionnaireShouldNotBeFound("saveText.contains=" + UPDATED_SAVE_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySaveTextNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where saveText does not contain DEFAULT_SAVE_TEXT
        defaultQuestionnaireShouldNotBeFound("saveText.doesNotContain=" + DEFAULT_SAVE_TEXT);

        // Get all the questionnaireList where saveText does not contain UPDATED_SAVE_TEXT
        defaultQuestionnaireShouldBeFound("saveText.doesNotContain=" + UPDATED_SAVE_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySearchTextIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where searchText equals to DEFAULT_SEARCH_TEXT
        defaultQuestionnaireShouldBeFound("searchText.equals=" + DEFAULT_SEARCH_TEXT);

        // Get all the questionnaireList where searchText equals to UPDATED_SEARCH_TEXT
        defaultQuestionnaireShouldNotBeFound("searchText.equals=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySearchTextIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where searchText in DEFAULT_SEARCH_TEXT or UPDATED_SEARCH_TEXT
        defaultQuestionnaireShouldBeFound("searchText.in=" + DEFAULT_SEARCH_TEXT + "," + UPDATED_SEARCH_TEXT);

        // Get all the questionnaireList where searchText equals to UPDATED_SEARCH_TEXT
        defaultQuestionnaireShouldNotBeFound("searchText.in=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySearchTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where searchText is not null
        defaultQuestionnaireShouldBeFound("searchText.specified=true");

        // Get all the questionnaireList where searchText is null
        defaultQuestionnaireShouldNotBeFound("searchText.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySearchTextContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where searchText contains DEFAULT_SEARCH_TEXT
        defaultQuestionnaireShouldBeFound("searchText.contains=" + DEFAULT_SEARCH_TEXT);

        // Get all the questionnaireList where searchText contains UPDATED_SEARCH_TEXT
        defaultQuestionnaireShouldNotBeFound("searchText.contains=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySearchTextNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where searchText does not contain DEFAULT_SEARCH_TEXT
        defaultQuestionnaireShouldNotBeFound("searchText.doesNotContain=" + DEFAULT_SEARCH_TEXT);

        // Get all the questionnaireList where searchText does not contain UPDATED_SEARCH_TEXT
        defaultQuestionnaireShouldBeFound("searchText.doesNotContain=" + UPDATED_SEARCH_TEXT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubjectToEvaluationIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subjectToEvaluation equals to DEFAULT_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldBeFound("subjectToEvaluation.equals=" + DEFAULT_SUBJECT_TO_EVALUATION);

        // Get all the questionnaireList where subjectToEvaluation equals to UPDATED_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldNotBeFound("subjectToEvaluation.equals=" + UPDATED_SUBJECT_TO_EVALUATION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubjectToEvaluationIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subjectToEvaluation in DEFAULT_SUBJECT_TO_EVALUATION or UPDATED_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldBeFound("subjectToEvaluation.in=" + DEFAULT_SUBJECT_TO_EVALUATION + "," + UPDATED_SUBJECT_TO_EVALUATION);

        // Get all the questionnaireList where subjectToEvaluation equals to UPDATED_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldNotBeFound("subjectToEvaluation.in=" + UPDATED_SUBJECT_TO_EVALUATION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubjectToEvaluationIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subjectToEvaluation is not null
        defaultQuestionnaireShouldBeFound("subjectToEvaluation.specified=true");

        // Get all the questionnaireList where subjectToEvaluation is null
        defaultQuestionnaireShouldNotBeFound("subjectToEvaluation.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubjectToEvaluationContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subjectToEvaluation contains DEFAULT_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldBeFound("subjectToEvaluation.contains=" + DEFAULT_SUBJECT_TO_EVALUATION);

        // Get all the questionnaireList where subjectToEvaluation contains UPDATED_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldNotBeFound("subjectToEvaluation.contains=" + UPDATED_SUBJECT_TO_EVALUATION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesBySubjectToEvaluationNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subjectToEvaluation does not contain DEFAULT_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldNotBeFound("subjectToEvaluation.doesNotContain=" + DEFAULT_SUBJECT_TO_EVALUATION);

        // Get all the questionnaireList where subjectToEvaluation does not contain UPDATED_SUBJECT_TO_EVALUATION
        defaultQuestionnaireShouldBeFound("subjectToEvaluation.doesNotContain=" + UPDATED_SUBJECT_TO_EVALUATION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByQuestionnaireTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where questionnaireType equals to DEFAULT_QUESTIONNAIRE_TYPE
        defaultQuestionnaireShouldBeFound("questionnaireType.equals=" + DEFAULT_QUESTIONNAIRE_TYPE);

        // Get all the questionnaireList where questionnaireType equals to UPDATED_QUESTIONNAIRE_TYPE
        defaultQuestionnaireShouldNotBeFound("questionnaireType.equals=" + UPDATED_QUESTIONNAIRE_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByQuestionnaireTypeIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where questionnaireType in DEFAULT_QUESTIONNAIRE_TYPE or UPDATED_QUESTIONNAIRE_TYPE
        defaultQuestionnaireShouldBeFound("questionnaireType.in=" + DEFAULT_QUESTIONNAIRE_TYPE + "," + UPDATED_QUESTIONNAIRE_TYPE);

        // Get all the questionnaireList where questionnaireType equals to UPDATED_QUESTIONNAIRE_TYPE
        defaultQuestionnaireShouldNotBeFound("questionnaireType.in=" + UPDATED_QUESTIONNAIRE_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByQuestionnaireTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where questionnaireType is not null
        defaultQuestionnaireShouldBeFound("questionnaireType.specified=true");

        // Get all the questionnaireList where questionnaireType is null
        defaultQuestionnaireShouldNotBeFound("questionnaireType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAttachmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where attachments equals to DEFAULT_ATTACHMENTS
        defaultQuestionnaireShouldBeFound("attachments.equals=" + DEFAULT_ATTACHMENTS);

        // Get all the questionnaireList where attachments equals to UPDATED_ATTACHMENTS
        defaultQuestionnaireShouldNotBeFound("attachments.equals=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAttachmentsIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where attachments in DEFAULT_ATTACHMENTS or UPDATED_ATTACHMENTS
        defaultQuestionnaireShouldBeFound("attachments.in=" + DEFAULT_ATTACHMENTS + "," + UPDATED_ATTACHMENTS);

        // Get all the questionnaireList where attachments equals to UPDATED_ATTACHMENTS
        defaultQuestionnaireShouldNotBeFound("attachments.in=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAttachmentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where attachments is not null
        defaultQuestionnaireShouldBeFound("attachments.specified=true");

        // Get all the questionnaireList where attachments is null
        defaultQuestionnaireShouldNotBeFound("attachments.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAttachmentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where attachments is greater than or equal to DEFAULT_ATTACHMENTS
        defaultQuestionnaireShouldBeFound("attachments.greaterThanOrEqual=" + DEFAULT_ATTACHMENTS);

        // Get all the questionnaireList where attachments is greater than or equal to UPDATED_ATTACHMENTS
        defaultQuestionnaireShouldNotBeFound("attachments.greaterThanOrEqual=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAttachmentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where attachments is less than or equal to DEFAULT_ATTACHMENTS
        defaultQuestionnaireShouldBeFound("attachments.lessThanOrEqual=" + DEFAULT_ATTACHMENTS);

        // Get all the questionnaireList where attachments is less than or equal to SMALLER_ATTACHMENTS
        defaultQuestionnaireShouldNotBeFound("attachments.lessThanOrEqual=" + SMALLER_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAttachmentsIsLessThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where attachments is less than DEFAULT_ATTACHMENTS
        defaultQuestionnaireShouldNotBeFound("attachments.lessThan=" + DEFAULT_ATTACHMENTS);

        // Get all the questionnaireList where attachments is less than UPDATED_ATTACHMENTS
        defaultQuestionnaireShouldBeFound("attachments.lessThan=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAttachmentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where attachments is greater than DEFAULT_ATTACHMENTS
        defaultQuestionnaireShouldNotBeFound("attachments.greaterThan=" + DEFAULT_ATTACHMENTS);

        // Get all the questionnaireList where attachments is greater than SMALLER_ATTACHMENTS
        defaultQuestionnaireShouldBeFound("attachments.greaterThan=" + SMALLER_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByQuestionnaireGroupIsEqualToSomething() throws Exception {
        QuestionnaireGroup questionnaireGroup;
        if (TestUtil.findAll(em, QuestionnaireGroup.class).isEmpty()) {
            questionnaireRepository.saveAndFlush(questionnaire);
            questionnaireGroup = QuestionnaireGroupResourceIT.createEntity(em);
        } else {
            questionnaireGroup = TestUtil.findAll(em, QuestionnaireGroup.class).get(0);
        }
        em.persist(questionnaireGroup);
        em.flush();
        questionnaire.setQuestionnaireGroup(questionnaireGroup);
        questionnaireRepository.saveAndFlush(questionnaire);
        Long questionnaireGroupId = questionnaireGroup.getId();

        // Get all the questionnaireList where questionnaireGroup equals to questionnaireGroupId
        defaultQuestionnaireShouldBeFound("questionnaireGroupId.equals=" + questionnaireGroupId);

        // Get all the questionnaireList where questionnaireGroup equals to (questionnaireGroupId + 1)
        defaultQuestionnaireShouldNotBeFound("questionnaireGroupId.equals=" + (questionnaireGroupId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionnairesByQuestionnaireProfileIsEqualToSomething() throws Exception {
        QuestionnaireProfile questionnaireProfile;
        if (TestUtil.findAll(em, QuestionnaireProfile.class).isEmpty()) {
            questionnaireRepository.saveAndFlush(questionnaire);
            questionnaireProfile = QuestionnaireProfileResourceIT.createEntity(em);
        } else {
            questionnaireProfile = TestUtil.findAll(em, QuestionnaireProfile.class).get(0);
        }
        em.persist(questionnaireProfile);
        em.flush();
        questionnaire.setQuestionnaireProfile(questionnaireProfile);
        questionnaireRepository.saveAndFlush(questionnaire);
        Long questionnaireProfileId = questionnaireProfile.getId();

        // Get all the questionnaireList where questionnaireProfile equals to questionnaireProfileId
        defaultQuestionnaireShouldBeFound("questionnaireProfileId.equals=" + questionnaireProfileId);

        // Get all the questionnaireList where questionnaireProfile equals to (questionnaireProfileId + 1)
        defaultQuestionnaireShouldNotBeFound("questionnaireProfileId.equals=" + (questionnaireProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionnaireShouldBeFound(String filter) throws Exception {
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].imageAlt").value(hasItem(DEFAULT_IMAGE_ALT)))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].compilationTime").value(hasItem(DEFAULT_COMPILATION_TIME)))
            .andExpect(jsonPath("$.[*].forcedTerminationTime").value(hasItem(DEFAULT_FORCED_TERMINATION_TIME)))
            .andExpect(jsonPath("$.[*].usedSeconds").value(hasItem(DEFAULT_USED_SECONDS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].xml").value(hasItem(DEFAULT_XML)))
            .andExpect(jsonPath("$.[*].json").value(hasItem(DEFAULT_JSON)))
            .andExpect(jsonPath("$.[*].saveText").value(hasItem(DEFAULT_SAVE_TEXT)))
            .andExpect(jsonPath("$.[*].searchText").value(hasItem(DEFAULT_SEARCH_TEXT)))
            .andExpect(jsonPath("$.[*].subjectToEvaluation").value(hasItem(DEFAULT_SUBJECT_TO_EVALUATION)))
            .andExpect(jsonPath("$.[*].questionnaireType").value(hasItem(DEFAULT_QUESTIONNAIRE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS)));

        // Check, that the count call also returns 1
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionnaireShouldNotBeFound(String filter) throws Exception {
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionnaire() throws Exception {
        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire
        Questionnaire updatedQuestionnaire = questionnaireRepository.findById(questionnaire.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionnaire are not directly saved in db
        em.detach(updatedQuestionnaire);
        updatedQuestionnaire
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .notes(UPDATED_NOTES)
            .image(UPDATED_IMAGE)
            .imageAlt(UPDATED_IMAGE_ALT)
            .instructions(UPDATED_INSTRUCTIONS)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .forcedTerminationTime(UPDATED_FORCED_TERMINATION_TIME)
            .usedSeconds(UPDATED_USED_SECONDS)
            .status(UPDATED_STATUS)
            .xml(UPDATED_XML)
            .json(UPDATED_JSON)
            .saveText(UPDATED_SAVE_TEXT)
            .searchText(UPDATED_SEARCH_TEXT)
            .subjectToEvaluation(UPDATED_SUBJECT_TO_EVALUATION)
            .questionnaireType(UPDATED_QUESTIONNAIRE_TYPE)
            .attachments(UPDATED_ATTACHMENTS);
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(updatedQuestionnaire);

        restQuestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionnaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionnaire.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testQuestionnaire.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuestionnaire.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testQuestionnaire.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testQuestionnaire.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testQuestionnaire.getImageAlt()).isEqualTo(UPDATED_IMAGE_ALT);
        assertThat(testQuestionnaire.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
        assertThat(testQuestionnaire.getCompilationTime()).isEqualTo(UPDATED_COMPILATION_TIME);
        assertThat(testQuestionnaire.getForcedTerminationTime()).isEqualTo(UPDATED_FORCED_TERMINATION_TIME);
        assertThat(testQuestionnaire.getUsedSeconds()).isEqualTo(UPDATED_USED_SECONDS);
        assertThat(testQuestionnaire.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuestionnaire.getXml()).isEqualTo(UPDATED_XML);
        assertThat(testQuestionnaire.getJson()).isEqualTo(UPDATED_JSON);
        assertThat(testQuestionnaire.getSaveText()).isEqualTo(UPDATED_SAVE_TEXT);
        assertThat(testQuestionnaire.getSearchText()).isEqualTo(UPDATED_SEARCH_TEXT);
        assertThat(testQuestionnaire.getSubjectToEvaluation()).isEqualTo(UPDATED_SUBJECT_TO_EVALUATION);
        assertThat(testQuestionnaire.getQuestionnaireType()).isEqualTo(UPDATED_QUESTIONNAIRE_TYPE);
        assertThat(testQuestionnaire.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void putNonExistingQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionnaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionnaireWithPatch() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire using partial update
        Questionnaire partialUpdatedQuestionnaire = new Questionnaire();
        partialUpdatedQuestionnaire.setId(questionnaire.getId());

        partialUpdatedQuestionnaire
            .version(UPDATED_VERSION)
            .subTitle(UPDATED_SUB_TITLE)
            .image(UPDATED_IMAGE)
            .imageAlt(UPDATED_IMAGE_ALT)
            .forcedTerminationTime(UPDATED_FORCED_TERMINATION_TIME)
            .status(UPDATED_STATUS)
            .json(UPDATED_JSON)
            .questionnaireType(UPDATED_QUESTIONNAIRE_TYPE)
            .attachments(UPDATED_ATTACHMENTS);

        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionnaire.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testQuestionnaire.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuestionnaire.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testQuestionnaire.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testQuestionnaire.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testQuestionnaire.getImageAlt()).isEqualTo(UPDATED_IMAGE_ALT);
        assertThat(testQuestionnaire.getInstructions()).isEqualTo(DEFAULT_INSTRUCTIONS);
        assertThat(testQuestionnaire.getCompilationTime()).isEqualTo(DEFAULT_COMPILATION_TIME);
        assertThat(testQuestionnaire.getForcedTerminationTime()).isEqualTo(UPDATED_FORCED_TERMINATION_TIME);
        assertThat(testQuestionnaire.getUsedSeconds()).isEqualTo(DEFAULT_USED_SECONDS);
        assertThat(testQuestionnaire.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuestionnaire.getXml()).isEqualTo(DEFAULT_XML);
        assertThat(testQuestionnaire.getJson()).isEqualTo(UPDATED_JSON);
        assertThat(testQuestionnaire.getSaveText()).isEqualTo(DEFAULT_SAVE_TEXT);
        assertThat(testQuestionnaire.getSearchText()).isEqualTo(DEFAULT_SEARCH_TEXT);
        assertThat(testQuestionnaire.getSubjectToEvaluation()).isEqualTo(DEFAULT_SUBJECT_TO_EVALUATION);
        assertThat(testQuestionnaire.getQuestionnaireType()).isEqualTo(UPDATED_QUESTIONNAIRE_TYPE);
        assertThat(testQuestionnaire.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void fullUpdateQuestionnaireWithPatch() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire using partial update
        Questionnaire partialUpdatedQuestionnaire = new Questionnaire();
        partialUpdatedQuestionnaire.setId(questionnaire.getId());

        partialUpdatedQuestionnaire
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .notes(UPDATED_NOTES)
            .image(UPDATED_IMAGE)
            .imageAlt(UPDATED_IMAGE_ALT)
            .instructions(UPDATED_INSTRUCTIONS)
            .compilationTime(UPDATED_COMPILATION_TIME)
            .forcedTerminationTime(UPDATED_FORCED_TERMINATION_TIME)
            .usedSeconds(UPDATED_USED_SECONDS)
            .status(UPDATED_STATUS)
            .xml(UPDATED_XML)
            .json(UPDATED_JSON)
            .saveText(UPDATED_SAVE_TEXT)
            .searchText(UPDATED_SEARCH_TEXT)
            .subjectToEvaluation(UPDATED_SUBJECT_TO_EVALUATION)
            .questionnaireType(UPDATED_QUESTIONNAIRE_TYPE)
            .attachments(UPDATED_ATTACHMENTS);

        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionnaire.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testQuestionnaire.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuestionnaire.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testQuestionnaire.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testQuestionnaire.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testQuestionnaire.getImageAlt()).isEqualTo(UPDATED_IMAGE_ALT);
        assertThat(testQuestionnaire.getInstructions()).isEqualTo(UPDATED_INSTRUCTIONS);
        assertThat(testQuestionnaire.getCompilationTime()).isEqualTo(UPDATED_COMPILATION_TIME);
        assertThat(testQuestionnaire.getForcedTerminationTime()).isEqualTo(UPDATED_FORCED_TERMINATION_TIME);
        assertThat(testQuestionnaire.getUsedSeconds()).isEqualTo(UPDATED_USED_SECONDS);
        assertThat(testQuestionnaire.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuestionnaire.getXml()).isEqualTo(UPDATED_XML);
        assertThat(testQuestionnaire.getJson()).isEqualTo(UPDATED_JSON);
        assertThat(testQuestionnaire.getSaveText()).isEqualTo(UPDATED_SAVE_TEXT);
        assertThat(testQuestionnaire.getSearchText()).isEqualTo(UPDATED_SEARCH_TEXT);
        assertThat(testQuestionnaire.getSubjectToEvaluation()).isEqualTo(UPDATED_SUBJECT_TO_EVALUATION);
        assertThat(testQuestionnaire.getQuestionnaireType()).isEqualTo(UPDATED_QUESTIONNAIRE_TYPE);
        assertThat(testQuestionnaire.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionnaireDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeDelete = questionnaireRepository.findAll().size();

        // Delete the questionnaire
        restQuestionnaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionnaire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
