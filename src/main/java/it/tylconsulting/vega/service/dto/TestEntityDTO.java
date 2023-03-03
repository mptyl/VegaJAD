package it.tylconsulting.vega.service.dto;

import it.tylconsulting.vega.domain.enumeration.ReplyType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;

/**
 * A DTO for the {@link it.tylconsulting.vega.domain.TestEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestEntityDTO implements Serializable {

    private Long id;

    private String stringField;

    private Integer integerField;

    private Long longField;

    private BigDecimal bigDecimalField;

    private Float floatField;

    private Double doubleField;

    private ReplyType enumField;

    private Boolean booleanField;

    private LocalDate localDateField;

    private ZonedDateTime zonedDateField;

    private Instant instantField;

    private Duration durationField;

    private UUID uuidField;

    @Lob
    private byte[] blobField;

    private String blobFieldContentType;

    @Lob
    private byte[] anyBlobField;

    private String anyBlobFieldContentType;

    @Lob
    private byte[] imageBlobField;

    private String imageBlobFieldContentType;

    @Lob
    private String textBlobField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public Integer getIntegerField() {
        return integerField;
    }

    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    public Long getLongField() {
        return longField;
    }

    public void setLongField(Long longField) {
        this.longField = longField;
    }

    public BigDecimal getBigDecimalField() {
        return bigDecimalField;
    }

    public void setBigDecimalField(BigDecimal bigDecimalField) {
        this.bigDecimalField = bigDecimalField;
    }

    public Float getFloatField() {
        return floatField;
    }

    public void setFloatField(Float floatField) {
        this.floatField = floatField;
    }

    public Double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }

    public ReplyType getEnumField() {
        return enumField;
    }

    public void setEnumField(ReplyType enumField) {
        this.enumField = enumField;
    }

    public Boolean getBooleanField() {
        return booleanField;
    }

    public void setBooleanField(Boolean booleanField) {
        this.booleanField = booleanField;
    }

    public LocalDate getLocalDateField() {
        return localDateField;
    }

    public void setLocalDateField(LocalDate localDateField) {
        this.localDateField = localDateField;
    }

    public ZonedDateTime getZonedDateField() {
        return zonedDateField;
    }

    public void setZonedDateField(ZonedDateTime zonedDateField) {
        this.zonedDateField = zonedDateField;
    }

    public Instant getInstantField() {
        return instantField;
    }

    public void setInstantField(Instant instantField) {
        this.instantField = instantField;
    }

    public Duration getDurationField() {
        return durationField;
    }

    public void setDurationField(Duration durationField) {
        this.durationField = durationField;
    }

    public UUID getUuidField() {
        return uuidField;
    }

    public void setUuidField(UUID uuidField) {
        this.uuidField = uuidField;
    }

    public byte[] getBlobField() {
        return blobField;
    }

    public void setBlobField(byte[] blobField) {
        this.blobField = blobField;
    }

    public String getBlobFieldContentType() {
        return blobFieldContentType;
    }

    public void setBlobFieldContentType(String blobFieldContentType) {
        this.blobFieldContentType = blobFieldContentType;
    }

    public byte[] getAnyBlobField() {
        return anyBlobField;
    }

    public void setAnyBlobField(byte[] anyBlobField) {
        this.anyBlobField = anyBlobField;
    }

    public String getAnyBlobFieldContentType() {
        return anyBlobFieldContentType;
    }

    public void setAnyBlobFieldContentType(String anyBlobFieldContentType) {
        this.anyBlobFieldContentType = anyBlobFieldContentType;
    }

    public byte[] getImageBlobField() {
        return imageBlobField;
    }

    public void setImageBlobField(byte[] imageBlobField) {
        this.imageBlobField = imageBlobField;
    }

    public String getImageBlobFieldContentType() {
        return imageBlobFieldContentType;
    }

    public void setImageBlobFieldContentType(String imageBlobFieldContentType) {
        this.imageBlobFieldContentType = imageBlobFieldContentType;
    }

    public String getTextBlobField() {
        return textBlobField;
    }

    public void setTextBlobField(String textBlobField) {
        this.textBlobField = textBlobField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestEntityDTO)) {
            return false;
        }

        TestEntityDTO testEntityDTO = (TestEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestEntityDTO{" +
            "id=" + getId() +
            ", stringField='" + getStringField() + "'" +
            ", integerField=" + getIntegerField() +
            ", longField=" + getLongField() +
            ", bigDecimalField=" + getBigDecimalField() +
            ", floatField=" + getFloatField() +
            ", doubleField=" + getDoubleField() +
            ", enumField='" + getEnumField() + "'" +
            ", booleanField='" + getBooleanField() + "'" +
            ", localDateField='" + getLocalDateField() + "'" +
            ", zonedDateField='" + getZonedDateField() + "'" +
            ", instantField='" + getInstantField() + "'" +
            ", durationField='" + getDurationField() + "'" +
            ", uuidField='" + getUuidField() + "'" +
            ", blobField='" + getBlobField() + "'" +
            ", anyBlobField='" + getAnyBlobField() + "'" +
            ", imageBlobField='" + getImageBlobField() + "'" +
            ", textBlobField='" + getTextBlobField() + "'" +
            "}";
    }
}
