package it.tylconsulting.vega.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.tylconsulting.vega.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionnaireProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionnaireProfile.class);
        QuestionnaireProfile questionnaireProfile1 = new QuestionnaireProfile();
        questionnaireProfile1.setId(1L);
        QuestionnaireProfile questionnaireProfile2 = new QuestionnaireProfile();
        questionnaireProfile2.setId(questionnaireProfile1.getId());
        assertThat(questionnaireProfile1).isEqualTo(questionnaireProfile2);
        questionnaireProfile2.setId(2L);
        assertThat(questionnaireProfile1).isNotEqualTo(questionnaireProfile2);
        questionnaireProfile1.setId(null);
        assertThat(questionnaireProfile1).isNotEqualTo(questionnaireProfile2);
    }
}
