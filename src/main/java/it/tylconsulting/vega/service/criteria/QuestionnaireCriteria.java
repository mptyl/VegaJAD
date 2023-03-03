package it.tylconsulting.vega.service.criteria;

import it.tylconsulting.vega.domain.enumeration.QuestionnaireType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link it.tylconsulting.vega.domain.Questionnaire} entity. This class is used
 * in {@link it.tylconsulting.vega.web.rest.QuestionnaireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questionnaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionnaireCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QuestionnaireType
     */
    public static class QuestionnaireTypeFilter extends Filter<QuestionnaireType> {

        public QuestionnaireTypeFilter() {}

        public QuestionnaireTypeFilter(QuestionnaireTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestionnaireTypeFilter copy() {
            return new QuestionnaireTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter version;

    private StringFilter title;

    private StringFilter subTitle;

    private StringFilter notes;

    private StringFilter image;

    private StringFilter imageAlt;

    private StringFilter instructions;

    private IntegerFilter compilationTime;

    private IntegerFilter forcedTerminationTime;

    private IntegerFilter usedSeconds;

    private IntegerFilter status;

    private StringFilter xml;

    private StringFilter json;

    private StringFilter saveText;

    private StringFilter searchText;

    private StringFilter subjectToEvaluation;

    private QuestionnaireTypeFilter questionnaireType;

    private IntegerFilter attachments;

    private LongFilter questionnaireGroupId;

    private LongFilter questionnaireProfileId;

    private Boolean distinct;

    public QuestionnaireCriteria() {}

    public QuestionnaireCriteria(QuestionnaireCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.version = other.version == null ? null : other.version.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.subTitle = other.subTitle == null ? null : other.subTitle.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.image = other.image == null ? null : other.image.copy();
        this.imageAlt = other.imageAlt == null ? null : other.imageAlt.copy();
        this.instructions = other.instructions == null ? null : other.instructions.copy();
        this.compilationTime = other.compilationTime == null ? null : other.compilationTime.copy();
        this.forcedTerminationTime = other.forcedTerminationTime == null ? null : other.forcedTerminationTime.copy();
        this.usedSeconds = other.usedSeconds == null ? null : other.usedSeconds.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.xml = other.xml == null ? null : other.xml.copy();
        this.json = other.json == null ? null : other.json.copy();
        this.saveText = other.saveText == null ? null : other.saveText.copy();
        this.searchText = other.searchText == null ? null : other.searchText.copy();
        this.subjectToEvaluation = other.subjectToEvaluation == null ? null : other.subjectToEvaluation.copy();
        this.questionnaireType = other.questionnaireType == null ? null : other.questionnaireType.copy();
        this.attachments = other.attachments == null ? null : other.attachments.copy();
        this.questionnaireGroupId = other.questionnaireGroupId == null ? null : other.questionnaireGroupId.copy();
        this.questionnaireProfileId = other.questionnaireProfileId == null ? null : other.questionnaireProfileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionnaireCriteria copy() {
        return new QuestionnaireCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getVersion() {
        return version;
    }

    public StringFilter version() {
        if (version == null) {
            version = new StringFilter();
        }
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSubTitle() {
        return subTitle;
    }

    public StringFilter subTitle() {
        if (subTitle == null) {
            subTitle = new StringFilter();
        }
        return subTitle;
    }

    public void setSubTitle(StringFilter subTitle) {
        this.subTitle = subTitle;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public StringFilter getImage() {
        return image;
    }

    public StringFilter image() {
        if (image == null) {
            image = new StringFilter();
        }
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public StringFilter getImageAlt() {
        return imageAlt;
    }

    public StringFilter imageAlt() {
        if (imageAlt == null) {
            imageAlt = new StringFilter();
        }
        return imageAlt;
    }

    public void setImageAlt(StringFilter imageAlt) {
        this.imageAlt = imageAlt;
    }

    public StringFilter getInstructions() {
        return instructions;
    }

    public StringFilter instructions() {
        if (instructions == null) {
            instructions = new StringFilter();
        }
        return instructions;
    }

    public void setInstructions(StringFilter instructions) {
        this.instructions = instructions;
    }

    public IntegerFilter getCompilationTime() {
        return compilationTime;
    }

    public IntegerFilter compilationTime() {
        if (compilationTime == null) {
            compilationTime = new IntegerFilter();
        }
        return compilationTime;
    }

    public void setCompilationTime(IntegerFilter compilationTime) {
        this.compilationTime = compilationTime;
    }

    public IntegerFilter getForcedTerminationTime() {
        return forcedTerminationTime;
    }

    public IntegerFilter forcedTerminationTime() {
        if (forcedTerminationTime == null) {
            forcedTerminationTime = new IntegerFilter();
        }
        return forcedTerminationTime;
    }

    public void setForcedTerminationTime(IntegerFilter forcedTerminationTime) {
        this.forcedTerminationTime = forcedTerminationTime;
    }

    public IntegerFilter getUsedSeconds() {
        return usedSeconds;
    }

    public IntegerFilter usedSeconds() {
        if (usedSeconds == null) {
            usedSeconds = new IntegerFilter();
        }
        return usedSeconds;
    }

    public void setUsedSeconds(IntegerFilter usedSeconds) {
        this.usedSeconds = usedSeconds;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public IntegerFilter status() {
        if (status == null) {
            status = new IntegerFilter();
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public StringFilter getXml() {
        return xml;
    }

    public StringFilter xml() {
        if (xml == null) {
            xml = new StringFilter();
        }
        return xml;
    }

    public void setXml(StringFilter xml) {
        this.xml = xml;
    }

    public StringFilter getJson() {
        return json;
    }

    public StringFilter json() {
        if (json == null) {
            json = new StringFilter();
        }
        return json;
    }

    public void setJson(StringFilter json) {
        this.json = json;
    }

    public StringFilter getSaveText() {
        return saveText;
    }

    public StringFilter saveText() {
        if (saveText == null) {
            saveText = new StringFilter();
        }
        return saveText;
    }

    public void setSaveText(StringFilter saveText) {
        this.saveText = saveText;
    }

    public StringFilter getSearchText() {
        return searchText;
    }

    public StringFilter searchText() {
        if (searchText == null) {
            searchText = new StringFilter();
        }
        return searchText;
    }

    public void setSearchText(StringFilter searchText) {
        this.searchText = searchText;
    }

    public StringFilter getSubjectToEvaluation() {
        return subjectToEvaluation;
    }

    public StringFilter subjectToEvaluation() {
        if (subjectToEvaluation == null) {
            subjectToEvaluation = new StringFilter();
        }
        return subjectToEvaluation;
    }

    public void setSubjectToEvaluation(StringFilter subjectToEvaluation) {
        this.subjectToEvaluation = subjectToEvaluation;
    }

    public QuestionnaireTypeFilter getQuestionnaireType() {
        return questionnaireType;
    }

    public QuestionnaireTypeFilter questionnaireType() {
        if (questionnaireType == null) {
            questionnaireType = new QuestionnaireTypeFilter();
        }
        return questionnaireType;
    }

    public void setQuestionnaireType(QuestionnaireTypeFilter questionnaireType) {
        this.questionnaireType = questionnaireType;
    }

    public IntegerFilter getAttachments() {
        return attachments;
    }

    public IntegerFilter attachments() {
        if (attachments == null) {
            attachments = new IntegerFilter();
        }
        return attachments;
    }

    public void setAttachments(IntegerFilter attachments) {
        this.attachments = attachments;
    }

    public LongFilter getQuestionnaireGroupId() {
        return questionnaireGroupId;
    }

    public LongFilter questionnaireGroupId() {
        if (questionnaireGroupId == null) {
            questionnaireGroupId = new LongFilter();
        }
        return questionnaireGroupId;
    }

    public void setQuestionnaireGroupId(LongFilter questionnaireGroupId) {
        this.questionnaireGroupId = questionnaireGroupId;
    }

    public LongFilter getQuestionnaireProfileId() {
        return questionnaireProfileId;
    }

    public LongFilter questionnaireProfileId() {
        if (questionnaireProfileId == null) {
            questionnaireProfileId = new LongFilter();
        }
        return questionnaireProfileId;
    }

    public void setQuestionnaireProfileId(LongFilter questionnaireProfileId) {
        this.questionnaireProfileId = questionnaireProfileId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestionnaireCriteria that = (QuestionnaireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(version, that.version) &&
            Objects.equals(title, that.title) &&
            Objects.equals(subTitle, that.subTitle) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(image, that.image) &&
            Objects.equals(imageAlt, that.imageAlt) &&
            Objects.equals(instructions, that.instructions) &&
            Objects.equals(compilationTime, that.compilationTime) &&
            Objects.equals(forcedTerminationTime, that.forcedTerminationTime) &&
            Objects.equals(usedSeconds, that.usedSeconds) &&
            Objects.equals(status, that.status) &&
            Objects.equals(xml, that.xml) &&
            Objects.equals(json, that.json) &&
            Objects.equals(saveText, that.saveText) &&
            Objects.equals(searchText, that.searchText) &&
            Objects.equals(subjectToEvaluation, that.subjectToEvaluation) &&
            Objects.equals(questionnaireType, that.questionnaireType) &&
            Objects.equals(attachments, that.attachments) &&
            Objects.equals(questionnaireGroupId, that.questionnaireGroupId) &&
            Objects.equals(questionnaireProfileId, that.questionnaireProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            version,
            title,
            subTitle,
            notes,
            image,
            imageAlt,
            instructions,
            compilationTime,
            forcedTerminationTime,
            usedSeconds,
            status,
            xml,
            json,
            saveText,
            searchText,
            subjectToEvaluation,
            questionnaireType,
            attachments,
            questionnaireGroupId,
            questionnaireProfileId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionnaireCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (version != null ? "version=" + version + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (subTitle != null ? "subTitle=" + subTitle + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (image != null ? "image=" + image + ", " : "") +
            (imageAlt != null ? "imageAlt=" + imageAlt + ", " : "") +
            (instructions != null ? "instructions=" + instructions + ", " : "") +
            (compilationTime != null ? "compilationTime=" + compilationTime + ", " : "") +
            (forcedTerminationTime != null ? "forcedTerminationTime=" + forcedTerminationTime + ", " : "") +
            (usedSeconds != null ? "usedSeconds=" + usedSeconds + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (xml != null ? "xml=" + xml + ", " : "") +
            (json != null ? "json=" + json + ", " : "") +
            (saveText != null ? "saveText=" + saveText + ", " : "") +
            (searchText != null ? "searchText=" + searchText + ", " : "") +
            (subjectToEvaluation != null ? "subjectToEvaluation=" + subjectToEvaluation + ", " : "") +
            (questionnaireType != null ? "questionnaireType=" + questionnaireType + ", " : "") +
            (attachments != null ? "attachments=" + attachments + ", " : "") +
            (questionnaireGroupId != null ? "questionnaireGroupId=" + questionnaireGroupId + ", " : "") +
            (questionnaireProfileId != null ? "questionnaireProfileId=" + questionnaireProfileId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
