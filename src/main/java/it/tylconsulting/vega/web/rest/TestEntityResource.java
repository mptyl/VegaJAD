package it.tylconsulting.vega.web.rest;

import it.tylconsulting.vega.repository.TestEntityRepository;
import it.tylconsulting.vega.service.TestEntityQueryService;
import it.tylconsulting.vega.service.TestEntityService;
import it.tylconsulting.vega.service.criteria.TestEntityCriteria;
import it.tylconsulting.vega.service.dto.TestEntityDTO;
import it.tylconsulting.vega.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.tylconsulting.vega.domain.TestEntity}.
 */
@RestController
@RequestMapping("/api")
public class TestEntityResource {

    private final Logger log = LoggerFactory.getLogger(TestEntityResource.class);

    private static final String ENTITY_NAME = "testEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestEntityService testEntityService;

    private final TestEntityRepository testEntityRepository;

    private final TestEntityQueryService testEntityQueryService;

    public TestEntityResource(
        TestEntityService testEntityService,
        TestEntityRepository testEntityRepository,
        TestEntityQueryService testEntityQueryService
    ) {
        this.testEntityService = testEntityService;
        this.testEntityRepository = testEntityRepository;
        this.testEntityQueryService = testEntityQueryService;
    }

    /**
     * {@code POST  /test-entities} : Create a new testEntity.
     *
     * @param testEntityDTO the testEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testEntityDTO, or with status {@code 400 (Bad Request)} if the testEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-entities")
    public ResponseEntity<TestEntityDTO> createTestEntity(@RequestBody TestEntityDTO testEntityDTO) throws URISyntaxException {
        log.debug("REST request to save TestEntity : {}", testEntityDTO);
        if (testEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new testEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestEntityDTO result = testEntityService.save(testEntityDTO);
        return ResponseEntity
            .created(new URI("/api/test-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-entities/:id} : Updates an existing testEntity.
     *
     * @param id the id of the testEntityDTO to save.
     * @param testEntityDTO the testEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testEntityDTO,
     * or with status {@code 400 (Bad Request)} if the testEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-entities/{id}")
    public ResponseEntity<TestEntityDTO> updateTestEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestEntityDTO testEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TestEntity : {}, {}", id, testEntityDTO);
        if (testEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestEntityDTO result = testEntityService.update(testEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testEntityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-entities/:id} : Partial updates given fields of an existing testEntity, field will ignore if it is null
     *
     * @param id the id of the testEntityDTO to save.
     * @param testEntityDTO the testEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testEntityDTO,
     * or with status {@code 400 (Bad Request)} if the testEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestEntityDTO> partialUpdateTestEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestEntityDTO testEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestEntity partially : {}, {}", id, testEntityDTO);
        if (testEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestEntityDTO> result = testEntityService.partialUpdate(testEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testEntityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-entities} : get all the testEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testEntities in body.
     */
    @GetMapping("/test-entities")
    public ResponseEntity<List<TestEntityDTO>> getAllTestEntities(TestEntityCriteria criteria) {
        log.debug("REST request to get TestEntities by criteria: {}", criteria);
        List<TestEntityDTO> entityList = testEntityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /test-entities/count} : count all the testEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-entities/count")
    public ResponseEntity<Long> countTestEntities(TestEntityCriteria criteria) {
        log.debug("REST request to count TestEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(testEntityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-entities/:id} : get the "id" testEntity.
     *
     * @param id the id of the testEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-entities/{id}")
    public ResponseEntity<TestEntityDTO> getTestEntity(@PathVariable Long id) {
        log.debug("REST request to get TestEntity : {}", id);
        Optional<TestEntityDTO> testEntityDTO = testEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testEntityDTO);
    }

    /**
     * {@code DELETE  /test-entities/:id} : delete the "id" testEntity.
     *
     * @param id the id of the testEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-entities/{id}")
    public ResponseEntity<Void> deleteTestEntity(@PathVariable Long id) {
        log.debug("REST request to delete TestEntity : {}", id);
        testEntityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
