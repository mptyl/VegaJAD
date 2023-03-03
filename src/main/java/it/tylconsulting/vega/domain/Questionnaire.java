package it.tylconsulting.vega.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.tylconsulting.vega.domain.enumeration.QuestionnaireType;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Questionnaire.
 */
@Entity
@Table(name = "questionnaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    @Column(name = "title")
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "notes")
    private String notes;

    @Column(name = "image")
    private String image;

    @Column(name = "image_alt")
    private String imageAlt;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "compilation_time")
    private Integer compilationTime;

    @Column(name = "forced_termination_time")
    private Integer forcedTerminationTime;

    @Column(name = "used_seconds")
    private Integer usedSeconds;

    @Column(name = "status")
    private Integer status;

    @Column(name = "xml")
    private String xml;

    @Column(name = "json")
    private String json;

    @Column(name = "save_text")
    private String saveText;

    /**
     * Campo utilizzato nella ricerca
     */
    @Column(name = "search_text")
    private String searchText;

    @Column(name = "subject_to_evaluation")
    private String subjectToEvaluation;

    @Enumerated(EnumType.STRING)
    @Column(name = "questionnaire_type")
    private QuestionnaireType questionnaireType;

    @Column(name = "attachments")
    private Integer attachments;

    @ManyToOne
    @JsonIgnoreProperties(value = { "questionnaires" }, allowSetters = true)
    private QuestionnaireGroup questionnaireGroup;

    @ManyToOne
    @JsonIgnoreProperties(value = { "questionnaires" }, allowSetters = true)
    private QuestionnaireProfile questionnaireProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Questionnaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Questionnaire name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public Questionnaire version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return this.title;
    }

    public Questionnaire title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public Questionnaire subTitle(String subTitle) {
        this.setSubTitle(subTitle);
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNotes() {
        return this.notes;
    }

    public Questionnaire notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImage() {
        return this.image;
    }

    public Questionnaire image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageAlt() {
        return this.imageAlt;
    }

    public Questionnaire imageAlt(String imageAlt) {
        this.setImageAlt(imageAlt);
        return this;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public Questionnaire instructions(String instructions) {
        this.setInstructions(instructions);
        return this;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getCompilationTime() {
        return this.compilationTime;
    }

    public Questionnaire compilationTime(Integer compilationTime) {
        this.setCompilationTime(compilationTime);
        return this;
    }

    public void setCompilationTime(Integer compilationTime) {
        this.compilationTime = compilationTime;
    }

    public Integer getForcedTerminationTime() {
        return this.forcedTerminationTime;
    }

    public Questionnaire forcedTerminationTime(Integer forcedTerminationTime) {
        this.setForcedTerminationTime(forcedTerminationTime);
        return this;
    }

    public void setForcedTerminationTime(Integer forcedTerminationTime) {
        this.forcedTerminationTime = forcedTerminationTime;
    }

    public Integer getUsedSeconds() {
        return this.usedSeconds;
    }

    public Questionnaire usedSeconds(Integer usedSeconds) {
        this.setUsedSeconds(usedSeconds);
        return this;
    }

    public void setUsedSeconds(Integer usedSeconds) {
        this.usedSeconds = usedSeconds;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Questionnaire status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getXml() {
        return this.xml;
    }

    public Questionnaire xml(String xml) {
        this.setXml(xml);
        return this;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getJson() {
        return this.json;
    }

    public Questionnaire json(String json) {
        this.setJson(json);
        return this;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getSaveText() {
        return this.saveText;
    }

    public Questionnaire saveText(String saveText) {
        this.setSaveText(saveText);
        return this;
    }

    public void setSaveText(String saveText) {
        this.saveText = saveText;
    }

    public String getSearchText() {
        return this.searchText;
    }

    public Questionnaire searchText(String searchText) {
        this.setSearchText(searchText);
        return this;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSubjectToEvaluation() {
        return this.subjectToEvaluation;
    }

    public Questionnaire subjectToEvaluation(String subjectToEvaluation) {
        this.setSubjectToEvaluation(subjectToEvaluation);
        return this;
    }

    public void setSubjectToEvaluation(String subjectToEvaluation) {
        this.subjectToEvaluation = subjectToEvaluation;
    }

    public QuestionnaireType getQuestionnaireType() {
        return this.questionnaireType;
    }

    public Questionnaire questionnaireType(QuestionnaireType questionnaireType) {
        this.setQuestionnaireType(questionnaireType);
        return this;
    }

    public void setQuestionnaireType(QuestionnaireType questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public Integer getAttachments() {
        return this.attachments;
    }

    public Questionnaire attachments(Integer attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(Integer attachments) {
        this.attachments = attachments;
    }

    public QuestionnaireGroup getQuestionnaireGroup() {
        return this.questionnaireGroup;
    }

    public void setQuestionnaireGroup(QuestionnaireGroup questionnaireGroup) {
        this.questionnaireGroup = questionnaireGroup;
    }

    public Questionnaire questionnaireGroup(QuestionnaireGroup questionnaireGroup) {
        this.setQuestionnaireGroup(questionnaireGroup);
        return this;
    }

    public QuestionnaireProfile getQuestionnaireProfile() {
        return this.questionnaireProfile;
    }

    public void setQuestionnaireProfile(QuestionnaireProfile questionnaireProfile) {
        this.questionnaireProfile = questionnaireProfile;
    }

    public Questionnaire questionnaireProfile(QuestionnaireProfile questionnaireProfile) {
        this.setQuestionnaireProfile(questionnaireProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questionnaire)) {
            return false;
        }
        return id != null && id.equals(((Questionnaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questionnaire{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", version='" + getVersion() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", notes='" + getNotes() + "'" +
            ", image='" + getImage() + "'" +
            ", imageAlt='" + getImageAlt() + "'" +
            ", instructions='" + getInstructions() + "'" +
            ", compilationTime=" + getCompilationTime() +
            ", forcedTerminationTime=" + getForcedTerminationTime() +
            ", usedSeconds=" + getUsedSeconds() +
            ", status=" + getStatus() +
            ", xml='" + getXml() + "'" +
            ", json='" + getJson() + "'" +
            ", saveText='" + getSaveText() + "'" +
            ", searchText='" + getSearchText() + "'" +
            ", subjectToEvaluation='" + getSubjectToEvaluation() + "'" +
            ", questionnaireType='" + getQuestionnaireType() + "'" +
            ", attachments=" + getAttachments() +
            "}";
    }
}
