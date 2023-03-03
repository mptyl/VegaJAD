package it.tylconsulting.vega.service;

import it.tylconsulting.vega.domain.*; // for static metamodels
import it.tylconsulting.vega.domain.Questionnaire;
import it.tylconsulting.vega.repository.QuestionnaireRepository;
import it.tylconsulting.vega.service.criteria.QuestionnaireCriteria;
import it.tylconsulting.vega.service.dto.QuestionnaireDTO;
import it.tylconsulting.vega.service.mapper.QuestionnaireMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Questionnaire} entities in the database.
 * The main input is a {@link QuestionnaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionnaireDTO} or a {@link Page} of {@link QuestionnaireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionnaireQueryService extends QueryService<Questionnaire> {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireQueryService.class);

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionnaireMapper questionnaireMapper;

    public QuestionnaireQueryService(QuestionnaireRepository questionnaireRepository, QuestionnaireMapper questionnaireMapper) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireMapper = questionnaireMapper;
    }

    /**
     * Return a {@link List} of {@link QuestionnaireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionnaireDTO> findByCriteria(QuestionnaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Questionnaire> specification = createSpecification(criteria);
        return questionnaireMapper.toDto(questionnaireRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestionnaireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionnaireDTO> findByCriteria(QuestionnaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Questionnaire> specification = createSpecification(criteria);
        return questionnaireRepository.findAll(specification, page).map(questionnaireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionnaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Questionnaire> specification = createSpecification(criteria);
        return questionnaireRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionnaireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Questionnaire> createSpecification(QuestionnaireCriteria criteria) {
        Specification<Questionnaire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Questionnaire_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Questionnaire_.name));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVersion(), Questionnaire_.version));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Questionnaire_.title));
            }
            if (criteria.getSubTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubTitle(), Questionnaire_.subTitle));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Questionnaire_.notes));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), Questionnaire_.image));
            }
            if (criteria.getImageAlt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageAlt(), Questionnaire_.imageAlt));
            }
            if (criteria.getInstructions() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInstructions(), Questionnaire_.instructions));
            }
            if (criteria.getCompilationTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompilationTime(), Questionnaire_.compilationTime));
            }
            if (criteria.getForcedTerminationTime() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getForcedTerminationTime(), Questionnaire_.forcedTerminationTime));
            }
            if (criteria.getUsedSeconds() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsedSeconds(), Questionnaire_.usedSeconds));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Questionnaire_.status));
            }
            if (criteria.getXml() != null) {
                specification = specification.and(buildStringSpecification(criteria.getXml(), Questionnaire_.xml));
            }
            if (criteria.getJson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJson(), Questionnaire_.json));
            }
            if (criteria.getSaveText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSaveText(), Questionnaire_.saveText));
            }
            if (criteria.getSearchText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSearchText(), Questionnaire_.searchText));
            }
            if (criteria.getSubjectToEvaluation() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubjectToEvaluation(), Questionnaire_.subjectToEvaluation));
            }
            if (criteria.getQuestionnaireType() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionnaireType(), Questionnaire_.questionnaireType));
            }
            if (criteria.getAttachments() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAttachments(), Questionnaire_.attachments));
            }
            if (criteria.getQuestionnaireGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getQuestionnaireGroupId(),
                            root -> root.join(Questionnaire_.questionnaireGroup, JoinType.LEFT).get(QuestionnaireGroup_.id)
                        )
                    );
            }
            if (criteria.getQuestionnaireProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getQuestionnaireProfileId(),
                            root -> root.join(Questionnaire_.questionnaireProfile, JoinType.LEFT).get(QuestionnaireProfile_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
