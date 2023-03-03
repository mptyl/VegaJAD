package it.tylconsulting.vega.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionnaireGroupMapperTest {

    private QuestionnaireGroupMapper questionnaireGroupMapper;

    @BeforeEach
    public void setUp() {
        questionnaireGroupMapper = new QuestionnaireGroupMapperImpl();
    }
}
