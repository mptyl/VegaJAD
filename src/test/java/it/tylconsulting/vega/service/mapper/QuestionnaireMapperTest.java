package it.tylconsulting.vega.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionnaireMapperTest {

    private QuestionnaireMapper questionnaireMapper;

    @BeforeEach
    public void setUp() {
        questionnaireMapper = new QuestionnaireMapperImpl();
    }
}
