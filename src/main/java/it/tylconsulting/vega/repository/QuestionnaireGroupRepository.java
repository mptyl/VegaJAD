package it.tylconsulting.vega.repository;

import it.tylconsulting.vega.domain.QuestionnaireGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuestionnaireGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionnaireGroupRepository extends JpaRepository<QuestionnaireGroup, Long> {}
