package it.tylconsulting.vega.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QuestionnaireProfile.
 */
@Entity
@Table(name = "questionnaire_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionnaireProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "questionnaireProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questionnaireGroup", "questionnaireProfile" }, allowSetters = true)
    private Set<Questionnaire> questionnaires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuestionnaireProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public QuestionnaireProfile description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Questionnaire> getQuestionnaires() {
        return this.questionnaires;
    }

    public void setQuestionnaires(Set<Questionnaire> questionnaires) {
        if (this.questionnaires != null) {
            this.questionnaires.forEach(i -> i.setQuestionnaireProfile(null));
        }
        if (questionnaires != null) {
            questionnaires.forEach(i -> i.setQuestionnaireProfile(this));
        }
        this.questionnaires = questionnaires;
    }

    public QuestionnaireProfile questionnaires(Set<Questionnaire> questionnaires) {
        this.setQuestionnaires(questionnaires);
        return this;
    }

    public QuestionnaireProfile addQuestionnaire(Questionnaire questionnaire) {
        this.questionnaires.add(questionnaire);
        questionnaire.setQuestionnaireProfile(this);
        return this;
    }

    public QuestionnaireProfile removeQuestionnaire(Questionnaire questionnaire) {
        this.questionnaires.remove(questionnaire);
        questionnaire.setQuestionnaireProfile(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionnaireProfile)) {
            return false;
        }
        return id != null && id.equals(((QuestionnaireProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionnaireProfile{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
