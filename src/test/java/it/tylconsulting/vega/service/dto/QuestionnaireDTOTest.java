package it.tylconsulting.vega.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.tylconsulting.vega.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionnaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionnaireDTO.class);
        QuestionnaireDTO questionnaireDTO1 = new QuestionnaireDTO();
        questionnaireDTO1.setId(1L);
        QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO();
        assertThat(questionnaireDTO1).isNotEqualTo(questionnaireDTO2);
        questionnaireDTO2.setId(questionnaireDTO1.getId());
        assertThat(questionnaireDTO1).isEqualTo(questionnaireDTO2);
        questionnaireDTO2.setId(2L);
        assertThat(questionnaireDTO1).isNotEqualTo(questionnaireDTO2);
        questionnaireDTO1.setId(null);
        assertThat(questionnaireDTO1).isNotEqualTo(questionnaireDTO2);
    }
}
