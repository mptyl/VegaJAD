package it.tylconsulting.vega.web.rest;

import it.tylconsulting.vega.repository.QuestionnaireGroupRepository;
import it.tylconsulting.vega.service.QuestionnaireGroupService;
import it.tylconsulting.vega.service.dto.QuestionnaireGroupDTO;
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
 * REST controller for managing {@link it.tylconsulting.vega.domain.QuestionnaireGroup}.
 */
@RestController
@RequestMapping("/api")
public class QuestionnaireGroupResource {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireGroupResource.class);

    private static final String ENTITY_NAME = "questionnaireGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionnaireGroupService questionnaireGroupService;

    private final QuestionnaireGroupRepository questionnaireGroupRepository;

    public QuestionnaireGroupResource(
        QuestionnaireGroupService questionnaireGroupService,
        QuestionnaireGroupRepository questionnaireGroupRepository
    ) {
        this.questionnaireGroupService = questionnaireGroupService;
        this.questionnaireGroupRepository = questionnaireGroupRepository;
    }

    /**
     * {@code POST  /questionnaire-groups} : Create a new questionnaireGroup.
     *
     * @param questionnaireGroupDTO the questionnaireGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionnaireGroupDTO, or with status {@code 400 (Bad Request)} if the questionnaireGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questionnaire-groups")
    public ResponseEntity<QuestionnaireGroupDTO> createQuestionnaireGroup(@RequestBody QuestionnaireGroupDTO questionnaireGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionnaireGroup : {}", questionnaireGroupDTO);
        if (questionnaireGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionnaireGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionnaireGroupDTO result = questionnaireGroupService.save(questionnaireGroupDTO);
        return ResponseEntity
            .created(new URI("/api/questionnaire-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questionnaire-groups/:id} : Updates an existing questionnaireGroup.
     *
     * @param id the id of the questionnaireGroupDTO to save.
     * @param questionnaireGroupDTO the questionnaireGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionnaireGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionnaireGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionnaireGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questionnaire-groups/{id}")
    public ResponseEntity<QuestionnaireGroupDTO> updateQuestionnaireGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionnaireGroupDTO questionnaireGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionnaireGroup : {}, {}", id, questionnaireGroupDTO);
        if (questionnaireGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionnaireGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionnaireGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionnaireGroupDTO result = questionnaireGroupService.update(questionnaireGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionnaireGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questionnaire-groups/:id} : Partial updates given fields of an existing questionnaireGroup, field will ignore if it is null
     *
     * @param id the id of the questionnaireGroupDTO to save.
     * @param questionnaireGroupDTO the questionnaireGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionnaireGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionnaireGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionnaireGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionnaireGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questionnaire-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionnaireGroupDTO> partialUpdateQuestionnaireGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionnaireGroupDTO questionnaireGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionnaireGroup partially : {}, {}", id, questionnaireGroupDTO);
        if (questionnaireGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionnaireGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionnaireGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionnaireGroupDTO> result = questionnaireGroupService.partialUpdate(questionnaireGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionnaireGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questionnaire-groups} : get all the questionnaireGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionnaireGroups in body.
     */
    @GetMapping("/questionnaire-groups")
    public List<QuestionnaireGroupDTO> getAllQuestionnaireGroups() {
        log.debug("REST request to get all QuestionnaireGroups");
        return questionnaireGroupService.findAll();
    }

    /**
     * {@code GET  /questionnaire-groups/:id} : get the "id" questionnaireGroup.
     *
     * @param id the id of the questionnaireGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionnaireGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questionnaire-groups/{id}")
    public ResponseEntity<QuestionnaireGroupDTO> getQuestionnaireGroup(@PathVariable Long id) {
        log.debug("REST request to get QuestionnaireGroup : {}", id);
        Optional<QuestionnaireGroupDTO> questionnaireGroupDTO = questionnaireGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionnaireGroupDTO);
    }

    /**
     * {@code DELETE  /questionnaire-groups/:id} : delete the "id" questionnaireGroup.
     *
     * @param id the id of the questionnaireGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questionnaire-groups/{id}")
    public ResponseEntity<Void> deleteQuestionnaireGroup(@PathVariable Long id) {
        log.debug("REST request to delete QuestionnaireGroup : {}", id);
        questionnaireGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
