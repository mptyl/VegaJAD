package it.tylconsulting.vega.service.mapper;

import it.tylconsulting.vega.domain.QuestionnaireGroup;
import it.tylconsulting.vega.service.dto.QuestionnaireGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionnaireGroup} and its DTO {@link QuestionnaireGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionnaireGroupMapper extends EntityMapper<QuestionnaireGroupDTO, QuestionnaireGroup> {}
