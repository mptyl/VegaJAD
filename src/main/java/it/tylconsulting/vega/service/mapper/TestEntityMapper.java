package it.tylconsulting.vega.service.mapper;

import it.tylconsulting.vega.domain.TestEntity;
import it.tylconsulting.vega.service.dto.TestEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestEntity} and its DTO {@link TestEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestEntityMapper extends EntityMapper<TestEntityDTO, TestEntity> {}
