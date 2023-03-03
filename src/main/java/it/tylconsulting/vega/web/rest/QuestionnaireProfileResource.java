package it.tylconsulting.vega.web.rest;

import it.tylconsulting.vega.repository.QuestionnaireProfileRepository;
import it.tylconsulting.vega.service.QuestionnaireProfileService;
import it.tylconsulting.vega.service.dto.QuestionnaireProfileDTO;
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
 * REST controller for managing {@link it.tylconsulting.vega.domain.QuestionnaireProfile}.
 */
@RestController
@RequestMapping("/api")
public class QuestionnaireProfileResource {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireProfileResource.class);

    private static final String ENTITY_NAME = "questionnaireProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionnaireProfileService questionnaireProfileService;

    private final QuestionnaireProfileRepository questionnaireProfileRepository;

    public QuestionnaireProfileResource(
        QuestionnaireProfileService questionnaireProfileService,
        QuestionnaireProfileRepository questionnaireProfileRepository
    ) {
        this.questionnaireProfileService = questionnaireProfileService;
        this.questionnaireProfileRepository = questionnaireProfileRepository;
    }

    /**
     * {@code POST  /questionnaire-profiles} : Create a new questionnaireProfile.
     *
     * @param questionnaireProfileDTO the questionnaireProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionnaireProfileDTO, or with status {@code 400 (Bad Request)} if the questionnaireProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questionnaire-profiles")
    public ResponseEntity<QuestionnaireProfileDTO> createQuestionnaireProfile(@RequestBody QuestionnaireProfileDTO questionnaireProfileDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionnaireProfile : {}", questionnaireProfileDTO);
        if (questionnaireProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionnaireProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionnaireProfileDTO result = questionnaireProfileService.save(questionnaireProfileDTO);
        return ResponseEntity
            .created(new URI("/api/questionnaire-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questionnaire-profiles/:id} : Updates an existing questionnaireProfile.
     *
     * @param id the id of the questionnaireProfileDTO to save.
     * @param questionnaireProfileDTO the questionnaireProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionnaireProfileDTO,
     * or with status {@code 400 (Bad Request)} if the questionnaireProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionnaireProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questionnaire-profiles/{id}")
    public ResponseEntity<QuestionnaireProfileDTO> updateQuestionnaireProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionnaireProfileDTO questionnaireProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionnaireProfile : {}, {}", id, questionnaireProfileDTO);
        if (questionnaireProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionnaireProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionnaireProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionnaireProfileDTO result = questionnaireProfileService.update(questionnaireProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionnaireProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questionnaire-profiles/:id} : Partial updates given fields of an existing questionnaireProfile, field will ignore if it is null
     *
     * @param id the id of the questionnaireProfileDTO to save.
     * @param questionnaireProfileDTO the questionnaireProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionnaireProfileDTO,
     * or with status {@code 400 (Bad Request)} if the questionnaireProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionnaireProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionnaireProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questionnaire-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionnaireProfileDTO> partialUpdateQuestionnaireProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionnaireProfileDTO questionnaireProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionnaireProfile partially : {}, {}", id, questionnaireProfileDTO);
        if (questionnaireProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionnaireProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionnaireProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionnaireProfileDTO> result = questionnaireProfileService.partialUpdate(questionnaireProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionnaireProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questionnaire-profiles} : get all the questionnaireProfiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionnaireProfiles in body.
     */
    @GetMapping("/questionnaire-profiles")
    public List<QuestionnaireProfileDTO> getAllQuestionnaireProfiles() {
        log.debug("REST request to get all QuestionnaireProfiles");
        return questionnaireProfileService.findAll();
    }

    /**
     * {@code GET  /questionnaire-profiles/:id} : get the "id" questionnaireProfile.
     *
     * @param id the id of the questionnaireProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionnaireProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questionnaire-profiles/{id}")
    public ResponseEntity<QuestionnaireProfileDTO> getQuestionnaireProfile(@PathVariable Long id) {
        log.debug("REST request to get QuestionnaireProfile : {}", id);
        Optional<QuestionnaireProfileDTO> questionnaireProfileDTO = questionnaireProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionnaireProfileDTO);
    }

    /**
     * {@code DELETE  /questionnaire-profiles/:id} : delete the "id" questionnaireProfile.
     *
     * @param id the id of the questionnaireProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questionnaire-profiles/{id}")
    public ResponseEntity<Void> deleteQuestionnaireProfile(@PathVariable Long id) {
        log.debug("REST request to delete QuestionnaireProfile : {}", id);
        questionnaireProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
