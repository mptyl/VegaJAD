package it.tylconsulting.vega.service;

import it.tylconsulting.vega.domain.QuestionnaireGroup;
import it.tylconsulting.vega.repository.QuestionnaireGroupRepository;
import it.tylconsulting.vega.service.dto.QuestionnaireGroupDTO;
import it.tylconsulting.vega.service.mapper.QuestionnaireGroupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionnaireGroup}.
 */
@Service
@Transactional
public class QuestionnaireGroupService {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireGroupService.class);

    private final QuestionnaireGroupRepository questionnaireGroupRepository;

    private final QuestionnaireGroupMapper questionnaireGroupMapper;

    public QuestionnaireGroupService(
        QuestionnaireGroupRepository questionnaireGroupRepository,
        QuestionnaireGroupMapper questionnaireGroupMapper
    ) {
        this.questionnaireGroupRepository = questionnaireGroupRepository;
        this.questionnaireGroupMapper = questionnaireGroupMapper;
    }

    /**
     * Save a questionnaireGroup.
     *
     * @param questionnaireGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionnaireGroupDTO save(QuestionnaireGroupDTO questionnaireGroupDTO) {
        log.debug("Request to save QuestionnaireGroup : {}", questionnaireGroupDTO);
        QuestionnaireGroup questionnaireGroup = questionnaireGroupMapper.toEntity(questionnaireGroupDTO);
        questionnaireGroup = questionnaireGroupRepository.save(questionnaireGroup);
        return questionnaireGroupMapper.toDto(questionnaireGroup);
    }

    /**
     * Update a questionnaireGroup.
     *
     * @param questionnaireGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionnaireGroupDTO update(QuestionnaireGroupDTO questionnaireGroupDTO) {
        log.debug("Request to update QuestionnaireGroup : {}", questionnaireGroupDTO);
        QuestionnaireGroup questionnaireGroup = questionnaireGroupMapper.toEntity(questionnaireGroupDTO);
        questionnaireGroup = questionnaireGroupRepository.save(questionnaireGroup);
        return questionnaireGroupMapper.toDto(questionnaireGroup);
    }

    /**
     * Partially update a questionnaireGroup.
     *
     * @param questionnaireGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionnaireGroupDTO> partialUpdate(QuestionnaireGroupDTO questionnaireGroupDTO) {
        log.debug("Request to partially update QuestionnaireGroup : {}", questionnaireGroupDTO);

        return questionnaireGroupRepository
            .findById(questionnaireGroupDTO.getId())
            .map(existingQuestionnaireGroup -> {
                questionnaireGroupMapper.partialUpdate(existingQuestionnaireGroup, questionnaireGroupDTO);

                return existingQuestionnaireGroup;
            })
            .map(questionnaireGroupRepository::save)
            .map(questionnaireGroupMapper::toDto);
    }

    /**
     * Get all the questionnaireGroups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionnaireGroupDTO> findAll() {
        log.debug("Request to get all QuestionnaireGroups");
        return questionnaireGroupRepository
            .findAll()
            .stream()
            .map(questionnaireGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one questionnaireGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionnaireGroupDTO> findOne(Long id) {
        log.debug("Request to get QuestionnaireGroup : {}", id);
        return questionnaireGroupRepository.findById(id).map(questionnaireGroupMapper::toDto);
    }

    /**
     * Delete the questionnaireGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionnaireGroup : {}", id);
        questionnaireGroupRepository.deleteById(id);
    }
}
