package it.tylconsulting.vega.service;

import it.tylconsulting.vega.domain.TestEntity;
import it.tylconsulting.vega.repository.TestEntityRepository;
import it.tylconsulting.vega.service.dto.TestEntityDTO;
import it.tylconsulting.vega.service.mapper.TestEntityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestEntity}.
 */
@Service
@Transactional
public class TestEntityService {

    private final Logger log = LoggerFactory.getLogger(TestEntityService.class);

    private final TestEntityRepository testEntityRepository;

    private final TestEntityMapper testEntityMapper;

    public TestEntityService(TestEntityRepository testEntityRepository, TestEntityMapper testEntityMapper) {
        this.testEntityRepository = testEntityRepository;
        this.testEntityMapper = testEntityMapper;
    }

    /**
     * Save a testEntity.
     *
     * @param testEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public TestEntityDTO save(TestEntityDTO testEntityDTO) {
        log.debug("Request to save TestEntity : {}", testEntityDTO);
        TestEntity testEntity = testEntityMapper.toEntity(testEntityDTO);
        testEntity = testEntityRepository.save(testEntity);
        return testEntityMapper.toDto(testEntity);
    }

    /**
     * Update a testEntity.
     *
     * @param testEntityDTO the entity to save.
     * @return the persisted entity.
     */
    public TestEntityDTO update(TestEntityDTO testEntityDTO) {
        log.debug("Request to update TestEntity : {}", testEntityDTO);
        TestEntity testEntity = testEntityMapper.toEntity(testEntityDTO);
        testEntity = testEntityRepository.save(testEntity);
        return testEntityMapper.toDto(testEntity);
    }

    /**
     * Partially update a testEntity.
     *
     * @param testEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestEntityDTO> partialUpdate(TestEntityDTO testEntityDTO) {
        log.debug("Request to partially update TestEntity : {}", testEntityDTO);

        return testEntityRepository
            .findById(testEntityDTO.getId())
            .map(existingTestEntity -> {
                testEntityMapper.partialUpdate(existingTestEntity, testEntityDTO);

                return existingTestEntity;
            })
            .map(testEntityRepository::save)
            .map(testEntityMapper::toDto);
    }

    /**
     * Get all the testEntities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TestEntityDTO> findAll() {
        log.debug("Request to get all TestEntities");
        return testEntityRepository.findAll().stream().map(testEntityMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one testEntity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestEntityDTO> findOne(Long id) {
        log.debug("Request to get TestEntity : {}", id);
        return testEntityRepository.findById(id).map(testEntityMapper::toDto);
    }

    /**
     * Delete the testEntity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestEntity : {}", id);
        testEntityRepository.deleteById(id);
    }
}
