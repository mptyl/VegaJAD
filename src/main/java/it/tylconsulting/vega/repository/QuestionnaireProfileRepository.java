package it.tylconsulting.vega.repository;

import it.tylconsulting.vega.domain.QuestionnaireProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuestionnaireProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionnaireProfileRepository extends JpaRepository<QuestionnaireProfile, Long> {}
