package it.tylconsulting.vega.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.tylconsulting.vega.domain.QuestionnaireGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionnaireGroupDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionnaireGroupDTO)) {
            return false;
        }

        QuestionnaireGroupDTO questionnaireGroupDTO = (QuestionnaireGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionnaireGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionnaireGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
