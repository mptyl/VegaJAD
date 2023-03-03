package it.tylconsulting.vega.service.mapper;

import it.tylconsulting.vega.domain.QuestionnaireProfile;
import it.tylconsulting.vega.service.dto.QuestionnaireProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionnaireProfile} and its DTO {@link QuestionnaireProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionnaireProfileMapper extends EntityMapper<QuestionnaireProfileDTO, QuestionnaireProfile> {}
