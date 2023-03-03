package it.tylconsulting.vega.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.tylconsulting.vega.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionnaireGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionnaireGroupDTO.class);
        QuestionnaireGroupDTO questionnaireGroupDTO1 = new QuestionnaireGroupDTO();
        questionnaireGroupDTO1.setId(1L);
        QuestionnaireGroupDTO questionnaireGroupDTO2 = new QuestionnaireGroupDTO();
        assertThat(questionnaireGroupDTO1).isNotEqualTo(questionnaireGroupDTO2);
        questionnaireGroupDTO2.setId(questionnaireGroupDTO1.getId());
        assertThat(questionnaireGroupDTO1).isEqualTo(questionnaireGroupDTO2);
        questionnaireGroupDTO2.setId(2L);
        assertThat(questionnaireGroupDTO1).isNotEqualTo(questionnaireGroupDTO2);
        questionnaireGroupDTO1.setId(null);
        assertThat(questionnaireGroupDTO1).isNotEqualTo(questionnaireGroupDTO2);
    }
}
