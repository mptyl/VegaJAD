package it.tylconsulting.vega.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.tylconsulting.vega.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionnaireGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionnaireGroup.class);
        QuestionnaireGroup questionnaireGroup1 = new QuestionnaireGroup();
        questionnaireGroup1.setId(1L);
        QuestionnaireGroup questionnaireGroup2 = new QuestionnaireGroup();
        questionnaireGroup2.setId(questionnaireGroup1.getId());
        assertThat(questionnaireGroup1).isEqualTo(questionnaireGroup2);
        questionnaireGroup2.setId(2L);
        assertThat(questionnaireGroup1).isNotEqualTo(questionnaireGroup2);
        questionnaireGroup1.setId(null);
        assertThat(questionnaireGroup1).isNotEqualTo(questionnaireGroup2);
    }
}
