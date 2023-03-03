package it.tylconsulting.vega.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionnaireProfileMapperTest {

    private QuestionnaireProfileMapper questionnaireProfileMapper;

    @BeforeEach
    public void setUp() {
        questionnaireProfileMapper = new QuestionnaireProfileMapperImpl();
    }
}
