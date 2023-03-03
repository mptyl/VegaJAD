package it.tylconsulting.vega.web.rest;

import static it.tylconsulting.vega.web.rest.TestUtil.sameInstant;
import static it.tylconsulting.vega.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.tylconsulting.vega.IntegrationTest;
import it.tylconsulting.vega.domain.TestEntity;
import it.tylconsulting.vega.domain.enumeration.ReplyType;
import it.tylconsulting.vega.repository.TestEntityRepository;
import it.tylconsulting.vega.service.criteria.TestEntityCriteria;
import it.tylconsulting.vega.service.dto.TestEntityDTO;
import it.tylconsulting.vega.service.mapper.TestEntityMapper;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TestEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestEntityResourceIT {

    private static final String DEFAULT_STRING_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_STRING_FIELD = "BBBBBBBBBB";

    private static final Integer DEFAULT_INTEGER_FIELD = 1;
    private static final Integer UPDATED_INTEGER_FIELD = 2;
    private static final Integer SMALLER_INTEGER_FIELD = 1 - 1;

    private static final Long DEFAULT_LONG_FIELD = 1L;
    private static final Long UPDATED_LONG_FIELD = 2L;
    private static final Long SMALLER_LONG_FIELD = 1L - 1L;

    private static final BigDecimal DEFAULT_BIG_DECIMAL_FIELD = new BigDecimal(1);
    private static final BigDecimal UPDATED_BIG_DECIMAL_FIELD = new BigDecimal(2);
    private static final BigDecimal SMALLER_BIG_DECIMAL_FIELD = new BigDecimal(1 - 1);

    private static final Float DEFAULT_FLOAT_FIELD = 1F;
    private static final Float UPDATED_FLOAT_FIELD = 2F;
    private static final Float SMALLER_FLOAT_FIELD = 1F - 1F;

    private static final Double DEFAULT_DOUBLE_FIELD = 1D;
    private static final Double UPDATED_DOUBLE_FIELD = 2D;
    private static final Double SMALLER_DOUBLE_FIELD = 1D - 1D;

    private static final ReplyType DEFAULT_ENUM_FIELD = ReplyType.TEXT;
    private static final ReplyType UPDATED_ENUM_FIELD = ReplyType.CHECKBOX;

    private static final Boolean DEFAULT_BOOLEAN_FIELD = false;
    private static final Boolean UPDATED_BOOLEAN_FIELD = true;

    private static final LocalDate DEFAULT_LOCAL_DATE_FIELD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOCAL_DATE_FIELD = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LOCAL_DATE_FIELD = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_ZONED_DATE_FIELD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ZONED_DATE_FIELD = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ZONED_DATE_FIELD = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Instant DEFAULT_INSTANT_FIELD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANT_FIELD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Duration DEFAULT_DURATION_FIELD = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_FIELD = Duration.ofHours(12);
    private static final Duration SMALLER_DURATION_FIELD = Duration.ofHours(5);

    private static final UUID DEFAULT_UUID_FIELD = UUID.randomUUID();
    private static final UUID UPDATED_UUID_FIELD = UUID.randomUUID();

    private static final byte[] DEFAULT_BLOB_FIELD = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BLOB_FIELD = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BLOB_FIELD_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BLOB_FIELD_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_ANY_BLOB_FIELD = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANY_BLOB_FIELD = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANY_BLOB_FIELD_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANY_BLOB_FIELD_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_BLOB_FIELD = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_BLOB_FIELD = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_BLOB_FIELD_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TEXT_BLOB_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_BLOB_FIELD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestEntityRepository testEntityRepository;

    @Autowired
    private TestEntityMapper testEntityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestEntityMockMvc;

    private TestEntity testEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestEntity createEntity(EntityManager em) {
        TestEntity testEntity = new TestEntity()
            .stringField(DEFAULT_STRING_FIELD)
            .integerField(DEFAULT_INTEGER_FIELD)
            .longField(DEFAULT_LONG_FIELD)
            .bigDecimalField(DEFAULT_BIG_DECIMAL_FIELD)
            .floatField(DEFAULT_FLOAT_FIELD)
            .doubleField(DEFAULT_DOUBLE_FIELD)
            .enumField(DEFAULT_ENUM_FIELD)
            .booleanField(DEFAULT_BOOLEAN_FIELD)
            .localDateField(DEFAULT_LOCAL_DATE_FIELD)
            .zonedDateField(DEFAULT_ZONED_DATE_FIELD)
            .instantField(DEFAULT_INSTANT_FIELD)
            .durationField(DEFAULT_DURATION_FIELD)
            .uuidField(DEFAULT_UUID_FIELD)
            .blobField(DEFAULT_BLOB_FIELD)
            .blobFieldContentType(DEFAULT_BLOB_FIELD_CONTENT_TYPE)
            .anyBlobField(DEFAULT_ANY_BLOB_FIELD)
            .anyBlobFieldContentType(DEFAULT_ANY_BLOB_FIELD_CONTENT_TYPE)
            .imageBlobField(DEFAULT_IMAGE_BLOB_FIELD)
            .imageBlobFieldContentType(DEFAULT_IMAGE_BLOB_FIELD_CONTENT_TYPE)
            .textBlobField(DEFAULT_TEXT_BLOB_FIELD);
        return testEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestEntity createUpdatedEntity(EntityManager em) {
        TestEntity testEntity = new TestEntity()
            .stringField(UPDATED_STRING_FIELD)
            .integerField(UPDATED_INTEGER_FIELD)
            .longField(UPDATED_LONG_FIELD)
            .bigDecimalField(UPDATED_BIG_DECIMAL_FIELD)
            .floatField(UPDATED_FLOAT_FIELD)
            .doubleField(UPDATED_DOUBLE_FIELD)
            .enumField(UPDATED_ENUM_FIELD)
            .booleanField(UPDATED_BOOLEAN_FIELD)
            .localDateField(UPDATED_LOCAL_DATE_FIELD)
            .zonedDateField(UPDATED_ZONED_DATE_FIELD)
            .instantField(UPDATED_INSTANT_FIELD)
            .durationField(UPDATED_DURATION_FIELD)
            .uuidField(UPDATED_UUID_FIELD)
            .blobField(UPDATED_BLOB_FIELD)
            .blobFieldContentType(UPDATED_BLOB_FIELD_CONTENT_TYPE)
            .anyBlobField(UPDATED_ANY_BLOB_FIELD)
            .anyBlobFieldContentType(UPDATED_ANY_BLOB_FIELD_CONTENT_TYPE)
            .imageBlobField(UPDATED_IMAGE_BLOB_FIELD)
            .imageBlobFieldContentType(UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE)
            .textBlobField(UPDATED_TEXT_BLOB_FIELD);
        return testEntity;
    }

    @BeforeEach
    public void initTest() {
        testEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createTestEntity() throws Exception {
        int databaseSizeBeforeCreate = testEntityRepository.findAll().size();
        // Create the TestEntity
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);
        restTestEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeCreate + 1);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getStringField()).isEqualTo(DEFAULT_STRING_FIELD);
        assertThat(testTestEntity.getIntegerField()).isEqualTo(DEFAULT_INTEGER_FIELD);
        assertThat(testTestEntity.getLongField()).isEqualTo(DEFAULT_LONG_FIELD);
        assertThat(testTestEntity.getBigDecimalField()).isEqualByComparingTo(DEFAULT_BIG_DECIMAL_FIELD);
        assertThat(testTestEntity.getFloatField()).isEqualTo(DEFAULT_FLOAT_FIELD);
        assertThat(testTestEntity.getDoubleField()).isEqualTo(DEFAULT_DOUBLE_FIELD);
        assertThat(testTestEntity.getEnumField()).isEqualTo(DEFAULT_ENUM_FIELD);
        assertThat(testTestEntity.getBooleanField()).isEqualTo(DEFAULT_BOOLEAN_FIELD);
        assertThat(testTestEntity.getLocalDateField()).isEqualTo(DEFAULT_LOCAL_DATE_FIELD);
        assertThat(testTestEntity.getZonedDateField()).isEqualTo(DEFAULT_ZONED_DATE_FIELD);
        assertThat(testTestEntity.getInstantField()).isEqualTo(DEFAULT_INSTANT_FIELD);
        assertThat(testTestEntity.getDurationField()).isEqualTo(DEFAULT_DURATION_FIELD);
        assertThat(testTestEntity.getUuidField()).isEqualTo(DEFAULT_UUID_FIELD);
        assertThat(testTestEntity.getBlobField()).isEqualTo(DEFAULT_BLOB_FIELD);
        assertThat(testTestEntity.getBlobFieldContentType()).isEqualTo(DEFAULT_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getAnyBlobField()).isEqualTo(DEFAULT_ANY_BLOB_FIELD);
        assertThat(testTestEntity.getAnyBlobFieldContentType()).isEqualTo(DEFAULT_ANY_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getImageBlobField()).isEqualTo(DEFAULT_IMAGE_BLOB_FIELD);
        assertThat(testTestEntity.getImageBlobFieldContentType()).isEqualTo(DEFAULT_IMAGE_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getTextBlobField()).isEqualTo(DEFAULT_TEXT_BLOB_FIELD);
    }

    @Test
    @Transactional
    void createTestEntityWithExistingId() throws Exception {
        // Create the TestEntity with an existing ID
        testEntity.setId(1L);
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);

        int databaseSizeBeforeCreate = testEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestEntities() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].stringField").value(hasItem(DEFAULT_STRING_FIELD)))
            .andExpect(jsonPath("$.[*].integerField").value(hasItem(DEFAULT_INTEGER_FIELD)))
            .andExpect(jsonPath("$.[*].longField").value(hasItem(DEFAULT_LONG_FIELD.intValue())))
            .andExpect(jsonPath("$.[*].bigDecimalField").value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_FIELD))))
            .andExpect(jsonPath("$.[*].floatField").value(hasItem(DEFAULT_FLOAT_FIELD.doubleValue())))
            .andExpect(jsonPath("$.[*].doubleField").value(hasItem(DEFAULT_DOUBLE_FIELD.doubleValue())))
            .andExpect(jsonPath("$.[*].enumField").value(hasItem(DEFAULT_ENUM_FIELD.toString())))
            .andExpect(jsonPath("$.[*].booleanField").value(hasItem(DEFAULT_BOOLEAN_FIELD.booleanValue())))
            .andExpect(jsonPath("$.[*].localDateField").value(hasItem(DEFAULT_LOCAL_DATE_FIELD.toString())))
            .andExpect(jsonPath("$.[*].zonedDateField").value(hasItem(sameInstant(DEFAULT_ZONED_DATE_FIELD))))
            .andExpect(jsonPath("$.[*].instantField").value(hasItem(DEFAULT_INSTANT_FIELD.toString())))
            .andExpect(jsonPath("$.[*].durationField").value(hasItem(DEFAULT_DURATION_FIELD.toString())))
            .andExpect(jsonPath("$.[*].uuidField").value(hasItem(DEFAULT_UUID_FIELD.toString())))
            .andExpect(jsonPath("$.[*].blobFieldContentType").value(hasItem(DEFAULT_BLOB_FIELD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].blobField").value(hasItem(Base64Utils.encodeToString(DEFAULT_BLOB_FIELD))))
            .andExpect(jsonPath("$.[*].anyBlobFieldContentType").value(hasItem(DEFAULT_ANY_BLOB_FIELD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anyBlobField").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANY_BLOB_FIELD))))
            .andExpect(jsonPath("$.[*].imageBlobFieldContentType").value(hasItem(DEFAULT_IMAGE_BLOB_FIELD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageBlobField").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB_FIELD))))
            .andExpect(jsonPath("$.[*].textBlobField").value(hasItem(DEFAULT_TEXT_BLOB_FIELD.toString())));
    }

    @Test
    @Transactional
    void getTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get the testEntity
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, testEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testEntity.getId().intValue()))
            .andExpect(jsonPath("$.stringField").value(DEFAULT_STRING_FIELD))
            .andExpect(jsonPath("$.integerField").value(DEFAULT_INTEGER_FIELD))
            .andExpect(jsonPath("$.longField").value(DEFAULT_LONG_FIELD.intValue()))
            .andExpect(jsonPath("$.bigDecimalField").value(sameNumber(DEFAULT_BIG_DECIMAL_FIELD)))
            .andExpect(jsonPath("$.floatField").value(DEFAULT_FLOAT_FIELD.doubleValue()))
            .andExpect(jsonPath("$.doubleField").value(DEFAULT_DOUBLE_FIELD.doubleValue()))
            .andExpect(jsonPath("$.enumField").value(DEFAULT_ENUM_FIELD.toString()))
            .andExpect(jsonPath("$.booleanField").value(DEFAULT_BOOLEAN_FIELD.booleanValue()))
            .andExpect(jsonPath("$.localDateField").value(DEFAULT_LOCAL_DATE_FIELD.toString()))
            .andExpect(jsonPath("$.zonedDateField").value(sameInstant(DEFAULT_ZONED_DATE_FIELD)))
            .andExpect(jsonPath("$.instantField").value(DEFAULT_INSTANT_FIELD.toString()))
            .andExpect(jsonPath("$.durationField").value(DEFAULT_DURATION_FIELD.toString()))
            .andExpect(jsonPath("$.uuidField").value(DEFAULT_UUID_FIELD.toString()))
            .andExpect(jsonPath("$.blobFieldContentType").value(DEFAULT_BLOB_FIELD_CONTENT_TYPE))
            .andExpect(jsonPath("$.blobField").value(Base64Utils.encodeToString(DEFAULT_BLOB_FIELD)))
            .andExpect(jsonPath("$.anyBlobFieldContentType").value(DEFAULT_ANY_BLOB_FIELD_CONTENT_TYPE))
            .andExpect(jsonPath("$.anyBlobField").value(Base64Utils.encodeToString(DEFAULT_ANY_BLOB_FIELD)))
            .andExpect(jsonPath("$.imageBlobFieldContentType").value(DEFAULT_IMAGE_BLOB_FIELD_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageBlobField").value(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB_FIELD)))
            .andExpect(jsonPath("$.textBlobField").value(DEFAULT_TEXT_BLOB_FIELD.toString()));
    }

    @Test
    @Transactional
    void getTestEntitiesByIdFiltering() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        Long id = testEntity.getId();

        defaultTestEntityShouldBeFound("id.equals=" + id);
        defaultTestEntityShouldNotBeFound("id.notEquals=" + id);

        defaultTestEntityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestEntityShouldNotBeFound("id.greaterThan=" + id);

        defaultTestEntityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestEntityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByStringFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where stringField equals to DEFAULT_STRING_FIELD
        defaultTestEntityShouldBeFound("stringField.equals=" + DEFAULT_STRING_FIELD);

        // Get all the testEntityList where stringField equals to UPDATED_STRING_FIELD
        defaultTestEntityShouldNotBeFound("stringField.equals=" + UPDATED_STRING_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByStringFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where stringField in DEFAULT_STRING_FIELD or UPDATED_STRING_FIELD
        defaultTestEntityShouldBeFound("stringField.in=" + DEFAULT_STRING_FIELD + "," + UPDATED_STRING_FIELD);

        // Get all the testEntityList where stringField equals to UPDATED_STRING_FIELD
        defaultTestEntityShouldNotBeFound("stringField.in=" + UPDATED_STRING_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByStringFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where stringField is not null
        defaultTestEntityShouldBeFound("stringField.specified=true");

        // Get all the testEntityList where stringField is null
        defaultTestEntityShouldNotBeFound("stringField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByStringFieldContainsSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where stringField contains DEFAULT_STRING_FIELD
        defaultTestEntityShouldBeFound("stringField.contains=" + DEFAULT_STRING_FIELD);

        // Get all the testEntityList where stringField contains UPDATED_STRING_FIELD
        defaultTestEntityShouldNotBeFound("stringField.contains=" + UPDATED_STRING_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByStringFieldNotContainsSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where stringField does not contain DEFAULT_STRING_FIELD
        defaultTestEntityShouldNotBeFound("stringField.doesNotContain=" + DEFAULT_STRING_FIELD);

        // Get all the testEntityList where stringField does not contain UPDATED_STRING_FIELD
        defaultTestEntityShouldBeFound("stringField.doesNotContain=" + UPDATED_STRING_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByIntegerFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where integerField equals to DEFAULT_INTEGER_FIELD
        defaultTestEntityShouldBeFound("integerField.equals=" + DEFAULT_INTEGER_FIELD);

        // Get all the testEntityList where integerField equals to UPDATED_INTEGER_FIELD
        defaultTestEntityShouldNotBeFound("integerField.equals=" + UPDATED_INTEGER_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByIntegerFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where integerField in DEFAULT_INTEGER_FIELD or UPDATED_INTEGER_FIELD
        defaultTestEntityShouldBeFound("integerField.in=" + DEFAULT_INTEGER_FIELD + "," + UPDATED_INTEGER_FIELD);

        // Get all the testEntityList where integerField equals to UPDATED_INTEGER_FIELD
        defaultTestEntityShouldNotBeFound("integerField.in=" + UPDATED_INTEGER_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByIntegerFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where integerField is not null
        defaultTestEntityShouldBeFound("integerField.specified=true");

        // Get all the testEntityList where integerField is null
        defaultTestEntityShouldNotBeFound("integerField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByIntegerFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where integerField is greater than or equal to DEFAULT_INTEGER_FIELD
        defaultTestEntityShouldBeFound("integerField.greaterThanOrEqual=" + DEFAULT_INTEGER_FIELD);

        // Get all the testEntityList where integerField is greater than or equal to UPDATED_INTEGER_FIELD
        defaultTestEntityShouldNotBeFound("integerField.greaterThanOrEqual=" + UPDATED_INTEGER_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByIntegerFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where integerField is less than or equal to DEFAULT_INTEGER_FIELD
        defaultTestEntityShouldBeFound("integerField.lessThanOrEqual=" + DEFAULT_INTEGER_FIELD);

        // Get all the testEntityList where integerField is less than or equal to SMALLER_INTEGER_FIELD
        defaultTestEntityShouldNotBeFound("integerField.lessThanOrEqual=" + SMALLER_INTEGER_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByIntegerFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where integerField is less than DEFAULT_INTEGER_FIELD
        defaultTestEntityShouldNotBeFound("integerField.lessThan=" + DEFAULT_INTEGER_FIELD);

        // Get all the testEntityList where integerField is less than UPDATED_INTEGER_FIELD
        defaultTestEntityShouldBeFound("integerField.lessThan=" + UPDATED_INTEGER_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByIntegerFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where integerField is greater than DEFAULT_INTEGER_FIELD
        defaultTestEntityShouldNotBeFound("integerField.greaterThan=" + DEFAULT_INTEGER_FIELD);

        // Get all the testEntityList where integerField is greater than SMALLER_INTEGER_FIELD
        defaultTestEntityShouldBeFound("integerField.greaterThan=" + SMALLER_INTEGER_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLongFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where longField equals to DEFAULT_LONG_FIELD
        defaultTestEntityShouldBeFound("longField.equals=" + DEFAULT_LONG_FIELD);

        // Get all the testEntityList where longField equals to UPDATED_LONG_FIELD
        defaultTestEntityShouldNotBeFound("longField.equals=" + UPDATED_LONG_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLongFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where longField in DEFAULT_LONG_FIELD or UPDATED_LONG_FIELD
        defaultTestEntityShouldBeFound("longField.in=" + DEFAULT_LONG_FIELD + "," + UPDATED_LONG_FIELD);

        // Get all the testEntityList where longField equals to UPDATED_LONG_FIELD
        defaultTestEntityShouldNotBeFound("longField.in=" + UPDATED_LONG_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLongFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where longField is not null
        defaultTestEntityShouldBeFound("longField.specified=true");

        // Get all the testEntityList where longField is null
        defaultTestEntityShouldNotBeFound("longField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLongFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where longField is greater than or equal to DEFAULT_LONG_FIELD
        defaultTestEntityShouldBeFound("longField.greaterThanOrEqual=" + DEFAULT_LONG_FIELD);

        // Get all the testEntityList where longField is greater than or equal to UPDATED_LONG_FIELD
        defaultTestEntityShouldNotBeFound("longField.greaterThanOrEqual=" + UPDATED_LONG_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLongFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where longField is less than or equal to DEFAULT_LONG_FIELD
        defaultTestEntityShouldBeFound("longField.lessThanOrEqual=" + DEFAULT_LONG_FIELD);

        // Get all the testEntityList where longField is less than or equal to SMALLER_LONG_FIELD
        defaultTestEntityShouldNotBeFound("longField.lessThanOrEqual=" + SMALLER_LONG_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLongFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where longField is less than DEFAULT_LONG_FIELD
        defaultTestEntityShouldNotBeFound("longField.lessThan=" + DEFAULT_LONG_FIELD);

        // Get all the testEntityList where longField is less than UPDATED_LONG_FIELD
        defaultTestEntityShouldBeFound("longField.lessThan=" + UPDATED_LONG_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLongFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where longField is greater than DEFAULT_LONG_FIELD
        defaultTestEntityShouldNotBeFound("longField.greaterThan=" + DEFAULT_LONG_FIELD);

        // Get all the testEntityList where longField is greater than SMALLER_LONG_FIELD
        defaultTestEntityShouldBeFound("longField.greaterThan=" + SMALLER_LONG_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBigDecimalFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where bigDecimalField equals to DEFAULT_BIG_DECIMAL_FIELD
        defaultTestEntityShouldBeFound("bigDecimalField.equals=" + DEFAULT_BIG_DECIMAL_FIELD);

        // Get all the testEntityList where bigDecimalField equals to UPDATED_BIG_DECIMAL_FIELD
        defaultTestEntityShouldNotBeFound("bigDecimalField.equals=" + UPDATED_BIG_DECIMAL_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBigDecimalFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where bigDecimalField in DEFAULT_BIG_DECIMAL_FIELD or UPDATED_BIG_DECIMAL_FIELD
        defaultTestEntityShouldBeFound("bigDecimalField.in=" + DEFAULT_BIG_DECIMAL_FIELD + "," + UPDATED_BIG_DECIMAL_FIELD);

        // Get all the testEntityList where bigDecimalField equals to UPDATED_BIG_DECIMAL_FIELD
        defaultTestEntityShouldNotBeFound("bigDecimalField.in=" + UPDATED_BIG_DECIMAL_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBigDecimalFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where bigDecimalField is not null
        defaultTestEntityShouldBeFound("bigDecimalField.specified=true");

        // Get all the testEntityList where bigDecimalField is null
        defaultTestEntityShouldNotBeFound("bigDecimalField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBigDecimalFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where bigDecimalField is greater than or equal to DEFAULT_BIG_DECIMAL_FIELD
        defaultTestEntityShouldBeFound("bigDecimalField.greaterThanOrEqual=" + DEFAULT_BIG_DECIMAL_FIELD);

        // Get all the testEntityList where bigDecimalField is greater than or equal to UPDATED_BIG_DECIMAL_FIELD
        defaultTestEntityShouldNotBeFound("bigDecimalField.greaterThanOrEqual=" + UPDATED_BIG_DECIMAL_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBigDecimalFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where bigDecimalField is less than or equal to DEFAULT_BIG_DECIMAL_FIELD
        defaultTestEntityShouldBeFound("bigDecimalField.lessThanOrEqual=" + DEFAULT_BIG_DECIMAL_FIELD);

        // Get all the testEntityList where bigDecimalField is less than or equal to SMALLER_BIG_DECIMAL_FIELD
        defaultTestEntityShouldNotBeFound("bigDecimalField.lessThanOrEqual=" + SMALLER_BIG_DECIMAL_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBigDecimalFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where bigDecimalField is less than DEFAULT_BIG_DECIMAL_FIELD
        defaultTestEntityShouldNotBeFound("bigDecimalField.lessThan=" + DEFAULT_BIG_DECIMAL_FIELD);

        // Get all the testEntityList where bigDecimalField is less than UPDATED_BIG_DECIMAL_FIELD
        defaultTestEntityShouldBeFound("bigDecimalField.lessThan=" + UPDATED_BIG_DECIMAL_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBigDecimalFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where bigDecimalField is greater than DEFAULT_BIG_DECIMAL_FIELD
        defaultTestEntityShouldNotBeFound("bigDecimalField.greaterThan=" + DEFAULT_BIG_DECIMAL_FIELD);

        // Get all the testEntityList where bigDecimalField is greater than SMALLER_BIG_DECIMAL_FIELD
        defaultTestEntityShouldBeFound("bigDecimalField.greaterThan=" + SMALLER_BIG_DECIMAL_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByFloatFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where floatField equals to DEFAULT_FLOAT_FIELD
        defaultTestEntityShouldBeFound("floatField.equals=" + DEFAULT_FLOAT_FIELD);

        // Get all the testEntityList where floatField equals to UPDATED_FLOAT_FIELD
        defaultTestEntityShouldNotBeFound("floatField.equals=" + UPDATED_FLOAT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByFloatFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where floatField in DEFAULT_FLOAT_FIELD or UPDATED_FLOAT_FIELD
        defaultTestEntityShouldBeFound("floatField.in=" + DEFAULT_FLOAT_FIELD + "," + UPDATED_FLOAT_FIELD);

        // Get all the testEntityList where floatField equals to UPDATED_FLOAT_FIELD
        defaultTestEntityShouldNotBeFound("floatField.in=" + UPDATED_FLOAT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByFloatFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where floatField is not null
        defaultTestEntityShouldBeFound("floatField.specified=true");

        // Get all the testEntityList where floatField is null
        defaultTestEntityShouldNotBeFound("floatField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByFloatFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where floatField is greater than or equal to DEFAULT_FLOAT_FIELD
        defaultTestEntityShouldBeFound("floatField.greaterThanOrEqual=" + DEFAULT_FLOAT_FIELD);

        // Get all the testEntityList where floatField is greater than or equal to UPDATED_FLOAT_FIELD
        defaultTestEntityShouldNotBeFound("floatField.greaterThanOrEqual=" + UPDATED_FLOAT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByFloatFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where floatField is less than or equal to DEFAULT_FLOAT_FIELD
        defaultTestEntityShouldBeFound("floatField.lessThanOrEqual=" + DEFAULT_FLOAT_FIELD);

        // Get all the testEntityList where floatField is less than or equal to SMALLER_FLOAT_FIELD
        defaultTestEntityShouldNotBeFound("floatField.lessThanOrEqual=" + SMALLER_FLOAT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByFloatFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where floatField is less than DEFAULT_FLOAT_FIELD
        defaultTestEntityShouldNotBeFound("floatField.lessThan=" + DEFAULT_FLOAT_FIELD);

        // Get all the testEntityList where floatField is less than UPDATED_FLOAT_FIELD
        defaultTestEntityShouldBeFound("floatField.lessThan=" + UPDATED_FLOAT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByFloatFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where floatField is greater than DEFAULT_FLOAT_FIELD
        defaultTestEntityShouldNotBeFound("floatField.greaterThan=" + DEFAULT_FLOAT_FIELD);

        // Get all the testEntityList where floatField is greater than SMALLER_FLOAT_FIELD
        defaultTestEntityShouldBeFound("floatField.greaterThan=" + SMALLER_FLOAT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDoubleFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where doubleField equals to DEFAULT_DOUBLE_FIELD
        defaultTestEntityShouldBeFound("doubleField.equals=" + DEFAULT_DOUBLE_FIELD);

        // Get all the testEntityList where doubleField equals to UPDATED_DOUBLE_FIELD
        defaultTestEntityShouldNotBeFound("doubleField.equals=" + UPDATED_DOUBLE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDoubleFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where doubleField in DEFAULT_DOUBLE_FIELD or UPDATED_DOUBLE_FIELD
        defaultTestEntityShouldBeFound("doubleField.in=" + DEFAULT_DOUBLE_FIELD + "," + UPDATED_DOUBLE_FIELD);

        // Get all the testEntityList where doubleField equals to UPDATED_DOUBLE_FIELD
        defaultTestEntityShouldNotBeFound("doubleField.in=" + UPDATED_DOUBLE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDoubleFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where doubleField is not null
        defaultTestEntityShouldBeFound("doubleField.specified=true");

        // Get all the testEntityList where doubleField is null
        defaultTestEntityShouldNotBeFound("doubleField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDoubleFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where doubleField is greater than or equal to DEFAULT_DOUBLE_FIELD
        defaultTestEntityShouldBeFound("doubleField.greaterThanOrEqual=" + DEFAULT_DOUBLE_FIELD);

        // Get all the testEntityList where doubleField is greater than or equal to UPDATED_DOUBLE_FIELD
        defaultTestEntityShouldNotBeFound("doubleField.greaterThanOrEqual=" + UPDATED_DOUBLE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDoubleFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where doubleField is less than or equal to DEFAULT_DOUBLE_FIELD
        defaultTestEntityShouldBeFound("doubleField.lessThanOrEqual=" + DEFAULT_DOUBLE_FIELD);

        // Get all the testEntityList where doubleField is less than or equal to SMALLER_DOUBLE_FIELD
        defaultTestEntityShouldNotBeFound("doubleField.lessThanOrEqual=" + SMALLER_DOUBLE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDoubleFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where doubleField is less than DEFAULT_DOUBLE_FIELD
        defaultTestEntityShouldNotBeFound("doubleField.lessThan=" + DEFAULT_DOUBLE_FIELD);

        // Get all the testEntityList where doubleField is less than UPDATED_DOUBLE_FIELD
        defaultTestEntityShouldBeFound("doubleField.lessThan=" + UPDATED_DOUBLE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDoubleFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where doubleField is greater than DEFAULT_DOUBLE_FIELD
        defaultTestEntityShouldNotBeFound("doubleField.greaterThan=" + DEFAULT_DOUBLE_FIELD);

        // Get all the testEntityList where doubleField is greater than SMALLER_DOUBLE_FIELD
        defaultTestEntityShouldBeFound("doubleField.greaterThan=" + SMALLER_DOUBLE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByEnumFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where enumField equals to DEFAULT_ENUM_FIELD
        defaultTestEntityShouldBeFound("enumField.equals=" + DEFAULT_ENUM_FIELD);

        // Get all the testEntityList where enumField equals to UPDATED_ENUM_FIELD
        defaultTestEntityShouldNotBeFound("enumField.equals=" + UPDATED_ENUM_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByEnumFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where enumField in DEFAULT_ENUM_FIELD or UPDATED_ENUM_FIELD
        defaultTestEntityShouldBeFound("enumField.in=" + DEFAULT_ENUM_FIELD + "," + UPDATED_ENUM_FIELD);

        // Get all the testEntityList where enumField equals to UPDATED_ENUM_FIELD
        defaultTestEntityShouldNotBeFound("enumField.in=" + UPDATED_ENUM_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByEnumFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where enumField is not null
        defaultTestEntityShouldBeFound("enumField.specified=true");

        // Get all the testEntityList where enumField is null
        defaultTestEntityShouldNotBeFound("enumField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBooleanFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where booleanField equals to DEFAULT_BOOLEAN_FIELD
        defaultTestEntityShouldBeFound("booleanField.equals=" + DEFAULT_BOOLEAN_FIELD);

        // Get all the testEntityList where booleanField equals to UPDATED_BOOLEAN_FIELD
        defaultTestEntityShouldNotBeFound("booleanField.equals=" + UPDATED_BOOLEAN_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBooleanFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where booleanField in DEFAULT_BOOLEAN_FIELD or UPDATED_BOOLEAN_FIELD
        defaultTestEntityShouldBeFound("booleanField.in=" + DEFAULT_BOOLEAN_FIELD + "," + UPDATED_BOOLEAN_FIELD);

        // Get all the testEntityList where booleanField equals to UPDATED_BOOLEAN_FIELD
        defaultTestEntityShouldNotBeFound("booleanField.in=" + UPDATED_BOOLEAN_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByBooleanFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where booleanField is not null
        defaultTestEntityShouldBeFound("booleanField.specified=true");

        // Get all the testEntityList where booleanField is null
        defaultTestEntityShouldNotBeFound("booleanField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLocalDateFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where localDateField equals to DEFAULT_LOCAL_DATE_FIELD
        defaultTestEntityShouldBeFound("localDateField.equals=" + DEFAULT_LOCAL_DATE_FIELD);

        // Get all the testEntityList where localDateField equals to UPDATED_LOCAL_DATE_FIELD
        defaultTestEntityShouldNotBeFound("localDateField.equals=" + UPDATED_LOCAL_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLocalDateFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where localDateField in DEFAULT_LOCAL_DATE_FIELD or UPDATED_LOCAL_DATE_FIELD
        defaultTestEntityShouldBeFound("localDateField.in=" + DEFAULT_LOCAL_DATE_FIELD + "," + UPDATED_LOCAL_DATE_FIELD);

        // Get all the testEntityList where localDateField equals to UPDATED_LOCAL_DATE_FIELD
        defaultTestEntityShouldNotBeFound("localDateField.in=" + UPDATED_LOCAL_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLocalDateFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where localDateField is not null
        defaultTestEntityShouldBeFound("localDateField.specified=true");

        // Get all the testEntityList where localDateField is null
        defaultTestEntityShouldNotBeFound("localDateField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLocalDateFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where localDateField is greater than or equal to DEFAULT_LOCAL_DATE_FIELD
        defaultTestEntityShouldBeFound("localDateField.greaterThanOrEqual=" + DEFAULT_LOCAL_DATE_FIELD);

        // Get all the testEntityList where localDateField is greater than or equal to UPDATED_LOCAL_DATE_FIELD
        defaultTestEntityShouldNotBeFound("localDateField.greaterThanOrEqual=" + UPDATED_LOCAL_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLocalDateFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where localDateField is less than or equal to DEFAULT_LOCAL_DATE_FIELD
        defaultTestEntityShouldBeFound("localDateField.lessThanOrEqual=" + DEFAULT_LOCAL_DATE_FIELD);

        // Get all the testEntityList where localDateField is less than or equal to SMALLER_LOCAL_DATE_FIELD
        defaultTestEntityShouldNotBeFound("localDateField.lessThanOrEqual=" + SMALLER_LOCAL_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLocalDateFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where localDateField is less than DEFAULT_LOCAL_DATE_FIELD
        defaultTestEntityShouldNotBeFound("localDateField.lessThan=" + DEFAULT_LOCAL_DATE_FIELD);

        // Get all the testEntityList where localDateField is less than UPDATED_LOCAL_DATE_FIELD
        defaultTestEntityShouldBeFound("localDateField.lessThan=" + UPDATED_LOCAL_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByLocalDateFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where localDateField is greater than DEFAULT_LOCAL_DATE_FIELD
        defaultTestEntityShouldNotBeFound("localDateField.greaterThan=" + DEFAULT_LOCAL_DATE_FIELD);

        // Get all the testEntityList where localDateField is greater than SMALLER_LOCAL_DATE_FIELD
        defaultTestEntityShouldBeFound("localDateField.greaterThan=" + SMALLER_LOCAL_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByZonedDateFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where zonedDateField equals to DEFAULT_ZONED_DATE_FIELD
        defaultTestEntityShouldBeFound("zonedDateField.equals=" + DEFAULT_ZONED_DATE_FIELD);

        // Get all the testEntityList where zonedDateField equals to UPDATED_ZONED_DATE_FIELD
        defaultTestEntityShouldNotBeFound("zonedDateField.equals=" + UPDATED_ZONED_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByZonedDateFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where zonedDateField in DEFAULT_ZONED_DATE_FIELD or UPDATED_ZONED_DATE_FIELD
        defaultTestEntityShouldBeFound("zonedDateField.in=" + DEFAULT_ZONED_DATE_FIELD + "," + UPDATED_ZONED_DATE_FIELD);

        // Get all the testEntityList where zonedDateField equals to UPDATED_ZONED_DATE_FIELD
        defaultTestEntityShouldNotBeFound("zonedDateField.in=" + UPDATED_ZONED_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByZonedDateFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where zonedDateField is not null
        defaultTestEntityShouldBeFound("zonedDateField.specified=true");

        // Get all the testEntityList where zonedDateField is null
        defaultTestEntityShouldNotBeFound("zonedDateField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByZonedDateFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where zonedDateField is greater than or equal to DEFAULT_ZONED_DATE_FIELD
        defaultTestEntityShouldBeFound("zonedDateField.greaterThanOrEqual=" + DEFAULT_ZONED_DATE_FIELD);

        // Get all the testEntityList where zonedDateField is greater than or equal to UPDATED_ZONED_DATE_FIELD
        defaultTestEntityShouldNotBeFound("zonedDateField.greaterThanOrEqual=" + UPDATED_ZONED_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByZonedDateFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where zonedDateField is less than or equal to DEFAULT_ZONED_DATE_FIELD
        defaultTestEntityShouldBeFound("zonedDateField.lessThanOrEqual=" + DEFAULT_ZONED_DATE_FIELD);

        // Get all the testEntityList where zonedDateField is less than or equal to SMALLER_ZONED_DATE_FIELD
        defaultTestEntityShouldNotBeFound("zonedDateField.lessThanOrEqual=" + SMALLER_ZONED_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByZonedDateFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where zonedDateField is less than DEFAULT_ZONED_DATE_FIELD
        defaultTestEntityShouldNotBeFound("zonedDateField.lessThan=" + DEFAULT_ZONED_DATE_FIELD);

        // Get all the testEntityList where zonedDateField is less than UPDATED_ZONED_DATE_FIELD
        defaultTestEntityShouldBeFound("zonedDateField.lessThan=" + UPDATED_ZONED_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByZonedDateFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where zonedDateField is greater than DEFAULT_ZONED_DATE_FIELD
        defaultTestEntityShouldNotBeFound("zonedDateField.greaterThan=" + DEFAULT_ZONED_DATE_FIELD);

        // Get all the testEntityList where zonedDateField is greater than SMALLER_ZONED_DATE_FIELD
        defaultTestEntityShouldBeFound("zonedDateField.greaterThan=" + SMALLER_ZONED_DATE_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByInstantFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where instantField equals to DEFAULT_INSTANT_FIELD
        defaultTestEntityShouldBeFound("instantField.equals=" + DEFAULT_INSTANT_FIELD);

        // Get all the testEntityList where instantField equals to UPDATED_INSTANT_FIELD
        defaultTestEntityShouldNotBeFound("instantField.equals=" + UPDATED_INSTANT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByInstantFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where instantField in DEFAULT_INSTANT_FIELD or UPDATED_INSTANT_FIELD
        defaultTestEntityShouldBeFound("instantField.in=" + DEFAULT_INSTANT_FIELD + "," + UPDATED_INSTANT_FIELD);

        // Get all the testEntityList where instantField equals to UPDATED_INSTANT_FIELD
        defaultTestEntityShouldNotBeFound("instantField.in=" + UPDATED_INSTANT_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByInstantFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where instantField is not null
        defaultTestEntityShouldBeFound("instantField.specified=true");

        // Get all the testEntityList where instantField is null
        defaultTestEntityShouldNotBeFound("instantField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDurationFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where durationField equals to DEFAULT_DURATION_FIELD
        defaultTestEntityShouldBeFound("durationField.equals=" + DEFAULT_DURATION_FIELD);

        // Get all the testEntityList where durationField equals to UPDATED_DURATION_FIELD
        defaultTestEntityShouldNotBeFound("durationField.equals=" + UPDATED_DURATION_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDurationFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where durationField in DEFAULT_DURATION_FIELD or UPDATED_DURATION_FIELD
        defaultTestEntityShouldBeFound("durationField.in=" + DEFAULT_DURATION_FIELD + "," + UPDATED_DURATION_FIELD);

        // Get all the testEntityList where durationField equals to UPDATED_DURATION_FIELD
        defaultTestEntityShouldNotBeFound("durationField.in=" + UPDATED_DURATION_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDurationFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where durationField is not null
        defaultTestEntityShouldBeFound("durationField.specified=true");

        // Get all the testEntityList where durationField is null
        defaultTestEntityShouldNotBeFound("durationField.specified=false");
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDurationFieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where durationField is greater than or equal to DEFAULT_DURATION_FIELD
        defaultTestEntityShouldBeFound("durationField.greaterThanOrEqual=" + DEFAULT_DURATION_FIELD);

        // Get all the testEntityList where durationField is greater than or equal to UPDATED_DURATION_FIELD
        defaultTestEntityShouldNotBeFound("durationField.greaterThanOrEqual=" + UPDATED_DURATION_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDurationFieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where durationField is less than or equal to DEFAULT_DURATION_FIELD
        defaultTestEntityShouldBeFound("durationField.lessThanOrEqual=" + DEFAULT_DURATION_FIELD);

        // Get all the testEntityList where durationField is less than or equal to SMALLER_DURATION_FIELD
        defaultTestEntityShouldNotBeFound("durationField.lessThanOrEqual=" + SMALLER_DURATION_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDurationFieldIsLessThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where durationField is less than DEFAULT_DURATION_FIELD
        defaultTestEntityShouldNotBeFound("durationField.lessThan=" + DEFAULT_DURATION_FIELD);

        // Get all the testEntityList where durationField is less than UPDATED_DURATION_FIELD
        defaultTestEntityShouldBeFound("durationField.lessThan=" + UPDATED_DURATION_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByDurationFieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where durationField is greater than DEFAULT_DURATION_FIELD
        defaultTestEntityShouldNotBeFound("durationField.greaterThan=" + DEFAULT_DURATION_FIELD);

        // Get all the testEntityList where durationField is greater than SMALLER_DURATION_FIELD
        defaultTestEntityShouldBeFound("durationField.greaterThan=" + SMALLER_DURATION_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByUuidFieldIsEqualToSomething() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where uuidField equals to DEFAULT_UUID_FIELD
        defaultTestEntityShouldBeFound("uuidField.equals=" + DEFAULT_UUID_FIELD);

        // Get all the testEntityList where uuidField equals to UPDATED_UUID_FIELD
        defaultTestEntityShouldNotBeFound("uuidField.equals=" + UPDATED_UUID_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByUuidFieldIsInShouldWork() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where uuidField in DEFAULT_UUID_FIELD or UPDATED_UUID_FIELD
        defaultTestEntityShouldBeFound("uuidField.in=" + DEFAULT_UUID_FIELD + "," + UPDATED_UUID_FIELD);

        // Get all the testEntityList where uuidField equals to UPDATED_UUID_FIELD
        defaultTestEntityShouldNotBeFound("uuidField.in=" + UPDATED_UUID_FIELD);
    }

    @Test
    @Transactional
    void getAllTestEntitiesByUuidFieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntityList where uuidField is not null
        defaultTestEntityShouldBeFound("uuidField.specified=true");

        // Get all the testEntityList where uuidField is null
        defaultTestEntityShouldNotBeFound("uuidField.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestEntityShouldBeFound(String filter) throws Exception {
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].stringField").value(hasItem(DEFAULT_STRING_FIELD)))
            .andExpect(jsonPath("$.[*].integerField").value(hasItem(DEFAULT_INTEGER_FIELD)))
            .andExpect(jsonPath("$.[*].longField").value(hasItem(DEFAULT_LONG_FIELD.intValue())))
            .andExpect(jsonPath("$.[*].bigDecimalField").value(hasItem(sameNumber(DEFAULT_BIG_DECIMAL_FIELD))))
            .andExpect(jsonPath("$.[*].floatField").value(hasItem(DEFAULT_FLOAT_FIELD.doubleValue())))
            .andExpect(jsonPath("$.[*].doubleField").value(hasItem(DEFAULT_DOUBLE_FIELD.doubleValue())))
            .andExpect(jsonPath("$.[*].enumField").value(hasItem(DEFAULT_ENUM_FIELD.toString())))
            .andExpect(jsonPath("$.[*].booleanField").value(hasItem(DEFAULT_BOOLEAN_FIELD.booleanValue())))
            .andExpect(jsonPath("$.[*].localDateField").value(hasItem(DEFAULT_LOCAL_DATE_FIELD.toString())))
            .andExpect(jsonPath("$.[*].zonedDateField").value(hasItem(sameInstant(DEFAULT_ZONED_DATE_FIELD))))
            .andExpect(jsonPath("$.[*].instantField").value(hasItem(DEFAULT_INSTANT_FIELD.toString())))
            .andExpect(jsonPath("$.[*].durationField").value(hasItem(DEFAULT_DURATION_FIELD.toString())))
            .andExpect(jsonPath("$.[*].uuidField").value(hasItem(DEFAULT_UUID_FIELD.toString())))
            .andExpect(jsonPath("$.[*].blobFieldContentType").value(hasItem(DEFAULT_BLOB_FIELD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].blobField").value(hasItem(Base64Utils.encodeToString(DEFAULT_BLOB_FIELD))))
            .andExpect(jsonPath("$.[*].anyBlobFieldContentType").value(hasItem(DEFAULT_ANY_BLOB_FIELD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anyBlobField").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANY_BLOB_FIELD))))
            .andExpect(jsonPath("$.[*].imageBlobFieldContentType").value(hasItem(DEFAULT_IMAGE_BLOB_FIELD_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageBlobField").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_BLOB_FIELD))))
            .andExpect(jsonPath("$.[*].textBlobField").value(hasItem(DEFAULT_TEXT_BLOB_FIELD.toString())));

        // Check, that the count call also returns 1
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestEntityShouldNotBeFound(String filter) throws Exception {
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestEntity() throws Exception {
        // Get the testEntity
        restTestEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();

        // Update the testEntity
        TestEntity updatedTestEntity = testEntityRepository.findById(testEntity.getId()).get();
        // Disconnect from session so that the updates on updatedTestEntity are not directly saved in db
        em.detach(updatedTestEntity);
        updatedTestEntity
            .stringField(UPDATED_STRING_FIELD)
            .integerField(UPDATED_INTEGER_FIELD)
            .longField(UPDATED_LONG_FIELD)
            .bigDecimalField(UPDATED_BIG_DECIMAL_FIELD)
            .floatField(UPDATED_FLOAT_FIELD)
            .doubleField(UPDATED_DOUBLE_FIELD)
            .enumField(UPDATED_ENUM_FIELD)
            .booleanField(UPDATED_BOOLEAN_FIELD)
            .localDateField(UPDATED_LOCAL_DATE_FIELD)
            .zonedDateField(UPDATED_ZONED_DATE_FIELD)
            .instantField(UPDATED_INSTANT_FIELD)
            .durationField(UPDATED_DURATION_FIELD)
            .uuidField(UPDATED_UUID_FIELD)
            .blobField(UPDATED_BLOB_FIELD)
            .blobFieldContentType(UPDATED_BLOB_FIELD_CONTENT_TYPE)
            .anyBlobField(UPDATED_ANY_BLOB_FIELD)
            .anyBlobFieldContentType(UPDATED_ANY_BLOB_FIELD_CONTENT_TYPE)
            .imageBlobField(UPDATED_IMAGE_BLOB_FIELD)
            .imageBlobFieldContentType(UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE)
            .textBlobField(UPDATED_TEXT_BLOB_FIELD);
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(updatedTestEntity);

        restTestEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testEntityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getStringField()).isEqualTo(UPDATED_STRING_FIELD);
        assertThat(testTestEntity.getIntegerField()).isEqualTo(UPDATED_INTEGER_FIELD);
        assertThat(testTestEntity.getLongField()).isEqualTo(UPDATED_LONG_FIELD);
        assertThat(testTestEntity.getBigDecimalField()).isEqualByComparingTo(UPDATED_BIG_DECIMAL_FIELD);
        assertThat(testTestEntity.getFloatField()).isEqualTo(UPDATED_FLOAT_FIELD);
        assertThat(testTestEntity.getDoubleField()).isEqualTo(UPDATED_DOUBLE_FIELD);
        assertThat(testTestEntity.getEnumField()).isEqualTo(UPDATED_ENUM_FIELD);
        assertThat(testTestEntity.getBooleanField()).isEqualTo(UPDATED_BOOLEAN_FIELD);
        assertThat(testTestEntity.getLocalDateField()).isEqualTo(UPDATED_LOCAL_DATE_FIELD);
        assertThat(testTestEntity.getZonedDateField()).isEqualTo(UPDATED_ZONED_DATE_FIELD);
        assertThat(testTestEntity.getInstantField()).isEqualTo(UPDATED_INSTANT_FIELD);
        assertThat(testTestEntity.getDurationField()).isEqualTo(UPDATED_DURATION_FIELD);
        assertThat(testTestEntity.getUuidField()).isEqualTo(UPDATED_UUID_FIELD);
        assertThat(testTestEntity.getBlobField()).isEqualTo(UPDATED_BLOB_FIELD);
        assertThat(testTestEntity.getBlobFieldContentType()).isEqualTo(UPDATED_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getAnyBlobField()).isEqualTo(UPDATED_ANY_BLOB_FIELD);
        assertThat(testTestEntity.getAnyBlobFieldContentType()).isEqualTo(UPDATED_ANY_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getImageBlobField()).isEqualTo(UPDATED_IMAGE_BLOB_FIELD);
        assertThat(testTestEntity.getImageBlobFieldContentType()).isEqualTo(UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getTextBlobField()).isEqualTo(UPDATED_TEXT_BLOB_FIELD);
    }

    @Test
    @Transactional
    void putNonExistingTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(count.incrementAndGet());

        // Create the TestEntity
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testEntityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(count.incrementAndGet());

        // Create the TestEntity
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(count.incrementAndGet());

        // Create the TestEntity
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestEntityWithPatch() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();

        // Update the testEntity using partial update
        TestEntity partialUpdatedTestEntity = new TestEntity();
        partialUpdatedTestEntity.setId(testEntity.getId());

        partialUpdatedTestEntity
            .stringField(UPDATED_STRING_FIELD)
            .integerField(UPDATED_INTEGER_FIELD)
            .longField(UPDATED_LONG_FIELD)
            .enumField(UPDATED_ENUM_FIELD)
            .localDateField(UPDATED_LOCAL_DATE_FIELD)
            .blobField(UPDATED_BLOB_FIELD)
            .blobFieldContentType(UPDATED_BLOB_FIELD_CONTENT_TYPE)
            .imageBlobField(UPDATED_IMAGE_BLOB_FIELD)
            .imageBlobFieldContentType(UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE)
            .textBlobField(UPDATED_TEXT_BLOB_FIELD);

        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestEntity))
            )
            .andExpect(status().isOk());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getStringField()).isEqualTo(UPDATED_STRING_FIELD);
        assertThat(testTestEntity.getIntegerField()).isEqualTo(UPDATED_INTEGER_FIELD);
        assertThat(testTestEntity.getLongField()).isEqualTo(UPDATED_LONG_FIELD);
        assertThat(testTestEntity.getBigDecimalField()).isEqualByComparingTo(DEFAULT_BIG_DECIMAL_FIELD);
        assertThat(testTestEntity.getFloatField()).isEqualTo(DEFAULT_FLOAT_FIELD);
        assertThat(testTestEntity.getDoubleField()).isEqualTo(DEFAULT_DOUBLE_FIELD);
        assertThat(testTestEntity.getEnumField()).isEqualTo(UPDATED_ENUM_FIELD);
        assertThat(testTestEntity.getBooleanField()).isEqualTo(DEFAULT_BOOLEAN_FIELD);
        assertThat(testTestEntity.getLocalDateField()).isEqualTo(UPDATED_LOCAL_DATE_FIELD);
        assertThat(testTestEntity.getZonedDateField()).isEqualTo(DEFAULT_ZONED_DATE_FIELD);
        assertThat(testTestEntity.getInstantField()).isEqualTo(DEFAULT_INSTANT_FIELD);
        assertThat(testTestEntity.getDurationField()).isEqualTo(DEFAULT_DURATION_FIELD);
        assertThat(testTestEntity.getUuidField()).isEqualTo(DEFAULT_UUID_FIELD);
        assertThat(testTestEntity.getBlobField()).isEqualTo(UPDATED_BLOB_FIELD);
        assertThat(testTestEntity.getBlobFieldContentType()).isEqualTo(UPDATED_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getAnyBlobField()).isEqualTo(DEFAULT_ANY_BLOB_FIELD);
        assertThat(testTestEntity.getAnyBlobFieldContentType()).isEqualTo(DEFAULT_ANY_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getImageBlobField()).isEqualTo(UPDATED_IMAGE_BLOB_FIELD);
        assertThat(testTestEntity.getImageBlobFieldContentType()).isEqualTo(UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getTextBlobField()).isEqualTo(UPDATED_TEXT_BLOB_FIELD);
    }

    @Test
    @Transactional
    void fullUpdateTestEntityWithPatch() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();

        // Update the testEntity using partial update
        TestEntity partialUpdatedTestEntity = new TestEntity();
        partialUpdatedTestEntity.setId(testEntity.getId());

        partialUpdatedTestEntity
            .stringField(UPDATED_STRING_FIELD)
            .integerField(UPDATED_INTEGER_FIELD)
            .longField(UPDATED_LONG_FIELD)
            .bigDecimalField(UPDATED_BIG_DECIMAL_FIELD)
            .floatField(UPDATED_FLOAT_FIELD)
            .doubleField(UPDATED_DOUBLE_FIELD)
            .enumField(UPDATED_ENUM_FIELD)
            .booleanField(UPDATED_BOOLEAN_FIELD)
            .localDateField(UPDATED_LOCAL_DATE_FIELD)
            .zonedDateField(UPDATED_ZONED_DATE_FIELD)
            .instantField(UPDATED_INSTANT_FIELD)
            .durationField(UPDATED_DURATION_FIELD)
            .uuidField(UPDATED_UUID_FIELD)
            .blobField(UPDATED_BLOB_FIELD)
            .blobFieldContentType(UPDATED_BLOB_FIELD_CONTENT_TYPE)
            .anyBlobField(UPDATED_ANY_BLOB_FIELD)
            .anyBlobFieldContentType(UPDATED_ANY_BLOB_FIELD_CONTENT_TYPE)
            .imageBlobField(UPDATED_IMAGE_BLOB_FIELD)
            .imageBlobFieldContentType(UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE)
            .textBlobField(UPDATED_TEXT_BLOB_FIELD);

        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestEntity))
            )
            .andExpect(status().isOk());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
        TestEntity testTestEntity = testEntityList.get(testEntityList.size() - 1);
        assertThat(testTestEntity.getStringField()).isEqualTo(UPDATED_STRING_FIELD);
        assertThat(testTestEntity.getIntegerField()).isEqualTo(UPDATED_INTEGER_FIELD);
        assertThat(testTestEntity.getLongField()).isEqualTo(UPDATED_LONG_FIELD);
        assertThat(testTestEntity.getBigDecimalField()).isEqualByComparingTo(UPDATED_BIG_DECIMAL_FIELD);
        assertThat(testTestEntity.getFloatField()).isEqualTo(UPDATED_FLOAT_FIELD);
        assertThat(testTestEntity.getDoubleField()).isEqualTo(UPDATED_DOUBLE_FIELD);
        assertThat(testTestEntity.getEnumField()).isEqualTo(UPDATED_ENUM_FIELD);
        assertThat(testTestEntity.getBooleanField()).isEqualTo(UPDATED_BOOLEAN_FIELD);
        assertThat(testTestEntity.getLocalDateField()).isEqualTo(UPDATED_LOCAL_DATE_FIELD);
        assertThat(testTestEntity.getZonedDateField()).isEqualTo(UPDATED_ZONED_DATE_FIELD);
        assertThat(testTestEntity.getInstantField()).isEqualTo(UPDATED_INSTANT_FIELD);
        assertThat(testTestEntity.getDurationField()).isEqualTo(UPDATED_DURATION_FIELD);
        assertThat(testTestEntity.getUuidField()).isEqualTo(UPDATED_UUID_FIELD);
        assertThat(testTestEntity.getBlobField()).isEqualTo(UPDATED_BLOB_FIELD);
        assertThat(testTestEntity.getBlobFieldContentType()).isEqualTo(UPDATED_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getAnyBlobField()).isEqualTo(UPDATED_ANY_BLOB_FIELD);
        assertThat(testTestEntity.getAnyBlobFieldContentType()).isEqualTo(UPDATED_ANY_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getImageBlobField()).isEqualTo(UPDATED_IMAGE_BLOB_FIELD);
        assertThat(testTestEntity.getImageBlobFieldContentType()).isEqualTo(UPDATED_IMAGE_BLOB_FIELD_CONTENT_TYPE);
        assertThat(testTestEntity.getTextBlobField()).isEqualTo(UPDATED_TEXT_BLOB_FIELD);
    }

    @Test
    @Transactional
    void patchNonExistingTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(count.incrementAndGet());

        // Create the TestEntity
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testEntityDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(count.incrementAndGet());

        // Create the TestEntity
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestEntity() throws Exception {
        int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();
        testEntity.setId(count.incrementAndGet());

        // Create the TestEntity
        TestEntityDTO testEntityDTO = testEntityMapper.toDto(testEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestEntity in the database
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        int databaseSizeBeforeDelete = testEntityRepository.findAll().size();

        // Delete the testEntity
        restTestEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, testEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestEntity> testEntityList = testEntityRepository.findAll();
        assertThat(testEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
