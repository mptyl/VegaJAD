package it.tylconsulting.vega.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.tylconsulting.vega.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionnaireProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionnaireProfileDTO.class);
        QuestionnaireProfileDTO questionnaireProfileDTO1 = new QuestionnaireProfileDTO();
        questionnaireProfileDTO1.setId(1L);
        QuestionnaireProfileDTO questionnaireProfileDTO2 = new QuestionnaireProfileDTO();
        assertThat(questionnaireProfileDTO1).isNotEqualTo(questionnaireProfileDTO2);
        questionnaireProfileDTO2.setId(questionnaireProfileDTO1.getId());
        assertThat(questionnaireProfileDTO1).isEqualTo(questionnaireProfileDTO2);
        questionnaireProfileDTO2.setId(2L);
        assertThat(questionnaireProfileDTO1).isNotEqualTo(questionnaireProfileDTO2);
        questionnaireProfileDTO1.setId(null);
        assertThat(questionnaireProfileDTO1).isNotEqualTo(questionnaireProfileDTO2);
    }
}
