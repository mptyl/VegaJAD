package it.tylconsulting.vega.service;

import it.tylconsulting.vega.domain.QuestionnaireProfile;
import it.tylconsulting.vega.repository.QuestionnaireProfileRepository;
import it.tylconsulting.vega.service.dto.QuestionnaireProfileDTO;
import it.tylconsulting.vega.service.mapper.QuestionnaireProfileMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionnaireProfile}.
 */
@Service
@Transactional
public class QuestionnaireProfileService {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireProfileService.class);

    private final QuestionnaireProfileRepository questionnaireProfileRepository;

    private final QuestionnaireProfileMapper questionnaireProfileMapper;

    public QuestionnaireProfileService(
        QuestionnaireProfileRepository questionnaireProfileRepository,
        QuestionnaireProfileMapper questionnaireProfileMapper
    ) {
        this.questionnaireProfileRepository = questionnaireProfileRepository;
        this.questionnaireProfileMapper = questionnaireProfileMapper;
    }

    /**
     * Save a questionnaireProfile.
     *
     * @param questionnaireProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionnaireProfileDTO save(QuestionnaireProfileDTO questionnaireProfileDTO) {
        log.debug("Request to save QuestionnaireProfile : {}", questionnaireProfileDTO);
        QuestionnaireProfile questionnaireProfile = questionnaireProfileMapper.toEntity(questionnaireProfileDTO);
        questionnaireProfile = questionnaireProfileRepository.save(questionnaireProfile);
        return questionnaireProfileMapper.toDto(questionnaireProfile);
    }

    /**
     * Update a questionnaireProfile.
     *
     * @param questionnaireProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionnaireProfileDTO update(QuestionnaireProfileDTO questionnaireProfileDTO) {
        log.debug("Request to update QuestionnaireProfile : {}", questionnaireProfileDTO);
        QuestionnaireProfile questionnaireProfile = questionnaireProfileMapper.toEntity(questionnaireProfileDTO);
        questionnaireProfile = questionnaireProfileRepository.save(questionnaireProfile);
        return questionnaireProfileMapper.toDto(questionnaireProfile);
    }

    /**
     * Partially update a questionnaireProfile.
     *
     * @param questionnaireProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionnaireProfileDTO> partialUpdate(QuestionnaireProfileDTO questionnaireProfileDTO) {
        log.debug("Request to partially update QuestionnaireProfile : {}", questionnaireProfileDTO);

        return questionnaireProfileRepository
            .findById(questionnaireProfileDTO.getId())
            .map(existingQuestionnaireProfile -> {
                questionnaireProfileMapper.partialUpdate(existingQuestionnaireProfile, questionnaireProfileDTO);

                return existingQuestionnaireProfile;
            })
            .map(questionnaireProfileRepository::save)
            .map(questionnaireProfileMapper::toDto);
    }

    /**
     * Get all the questionnaireProfiles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionnaireProfileDTO> findAll() {
        log.debug("Request to get all QuestionnaireProfiles");
        return questionnaireProfileRepository
            .findAll()
            .stream()
            .map(questionnaireProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one questionnaireProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionnaireProfileDTO> findOne(Long id) {
        log.debug("Request to get QuestionnaireProfile : {}", id);
        return questionnaireProfileRepository.findById(id).map(questionnaireProfileMapper::toDto);
    }

    /**
     * Delete the questionnaireProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionnaireProfile : {}", id);
        questionnaireProfileRepository.deleteById(id);
    }
}
