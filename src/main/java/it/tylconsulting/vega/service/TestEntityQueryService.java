package it.tylconsulting.vega.service;

import it.tylconsulting.vega.domain.*; // for static metamodels
import it.tylconsulting.vega.domain.TestEntity;
import it.tylconsulting.vega.repository.TestEntityRepository;
import it.tylconsulting.vega.service.criteria.TestEntityCriteria;
import it.tylconsulting.vega.service.dto.TestEntityDTO;
import it.tylconsulting.vega.service.mapper.TestEntityMapper;
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
 * Service for executing complex queries for {@link TestEntity} entities in the database.
 * The main input is a {@link TestEntityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestEntityDTO} or a {@link Page} of {@link TestEntityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestEntityQueryService extends QueryService<TestEntity> {

    private final Logger log = LoggerFactory.getLogger(TestEntityQueryService.class);

    private final TestEntityRepository testEntityRepository;

    private final TestEntityMapper testEntityMapper;

    public TestEntityQueryService(TestEntityRepository testEntityRepository, TestEntityMapper testEntityMapper) {
        this.testEntityRepository = testEntityRepository;
        this.testEntityMapper = testEntityMapper;
    }

    /**
     * Return a {@link List} of {@link TestEntityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestEntityDTO> findByCriteria(TestEntityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestEntity> specification = createSpecification(criteria);
        return testEntityMapper.toDto(testEntityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TestEntityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestEntityDTO> findByCriteria(TestEntityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestEntity> specification = createSpecification(criteria);
        return testEntityRepository.findAll(specification, page).map(testEntityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestEntityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestEntity> specification = createSpecification(criteria);
        return testEntityRepository.count(specification);
    }

    /**
     * Function to convert {@link TestEntityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestEntity> createSpecification(TestEntityCriteria criteria) {
        Specification<TestEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestEntity_.id));
            }
            if (criteria.getStringField() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStringField(), TestEntity_.stringField));
            }
            if (criteria.getIntegerField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIntegerField(), TestEntity_.integerField));
            }
            if (criteria.getLongField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongField(), TestEntity_.longField));
            }
            if (criteria.getBigDecimalField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBigDecimalField(), TestEntity_.bigDecimalField));
            }
            if (criteria.getFloatField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFloatField(), TestEntity_.floatField));
            }
            if (criteria.getDoubleField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDoubleField(), TestEntity_.doubleField));
            }
            if (criteria.getEnumField() != null) {
                specification = specification.and(buildSpecification(criteria.getEnumField(), TestEntity_.enumField));
            }
            if (criteria.getBooleanField() != null) {
                specification = specification.and(buildSpecification(criteria.getBooleanField(), TestEntity_.booleanField));
            }
            if (criteria.getLocalDateField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalDateField(), TestEntity_.localDateField));
            }
            if (criteria.getZonedDateField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getZonedDateField(), TestEntity_.zonedDateField));
            }
            if (criteria.getInstantField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInstantField(), TestEntity_.instantField));
            }
            if (criteria.getDurationField() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationField(), TestEntity_.durationField));
            }
            if (criteria.getUuidField() != null) {
                specification = specification.and(buildSpecification(criteria.getUuidField(), TestEntity_.uuidField));
            }
        }
        return specification;
    }
}
