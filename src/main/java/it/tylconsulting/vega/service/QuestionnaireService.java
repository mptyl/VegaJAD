package it.tylconsulting.vega.service;

import it.tylconsulting.vega.domain.Questionnaire;
import it.tylconsulting.vega.repository.QuestionnaireRepository;
import it.tylconsulting.vega.service.dto.QuestionnaireDTO;
import it.tylconsulting.vega.service.mapper.QuestionnaireMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Questionnaire}.
 */
@Service
@Transactional
public class QuestionnaireService {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireService.class);

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionnaireMapper questionnaireMapper;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository, QuestionnaireMapper questionnaireMapper) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireMapper = questionnaireMapper;
    }

    /**
     * Save a questionnaire.
     *
     * @param questionnaireDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionnaireDTO save(QuestionnaireDTO questionnaireDTO) {
        log.debug("Request to save Questionnaire : {}", questionnaireDTO);
        Questionnaire questionnaire = questionnaireMapper.toEntity(questionnaireDTO);
        questionnaire = questionnaireRepository.save(questionnaire);
        return questionnaireMapper.toDto(questionnaire);
    }

    /**
     * Update a questionnaire.
     *
     * @param questionnaireDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionnaireDTO update(QuestionnaireDTO questionnaireDTO) {
        log.debug("Request to update Questionnaire : {}", questionnaireDTO);
        Questionnaire questionnaire = questionnaireMapper.toEntity(questionnaireDTO);
        questionnaire = questionnaireRepository.save(questionnaire);
        return questionnaireMapper.toDto(questionnaire);
    }

    /**
     * Partially update a questionnaire.
     *
     * @param questionnaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionnaireDTO> partialUpdate(QuestionnaireDTO questionnaireDTO) {
        log.debug("Request to partially update Questionnaire : {}", questionnaireDTO);

        return questionnaireRepository
            .findById(questionnaireDTO.getId())
            .map(existingQuestionnaire -> {
                questionnaireMapper.partialUpdate(existingQuestionnaire, questionnaireDTO);

                return existingQuestionnaire;
            })
            .map(questionnaireRepository::save)
            .map(questionnaireMapper::toDto);
    }

    /**
     * Get all the questionnaires.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionnaireDTO> findAll() {
        log.debug("Request to get all Questionnaires");
        return questionnaireRepository.findAll().stream().map(questionnaireMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one questionnaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionnaireDTO> findOne(Long id) {
        log.debug("Request to get Questionnaire : {}", id);
        return questionnaireRepository.findById(id).map(questionnaireMapper::toDto);
    }

    /**
     * Delete the questionnaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Questionnaire : {}", id);
        questionnaireRepository.deleteById(id);
    }
}
