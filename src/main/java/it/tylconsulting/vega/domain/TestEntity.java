package it.tylconsulting.vega.domain;

import it.tylconsulting.vega.domain.enumeration.ReplyType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A TestEntity.
 */
@Entity
@Table(name = "test_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "string_field")
    private String stringField;

    @Column(name = "integer_field")
    private Integer integerField;

    @Column(name = "long_field")
    private Long longField;

    @Column(name = "big_decimal_field", precision = 21, scale = 2)
    private BigDecimal bigDecimalField;

    @Column(name = "float_field")
    private Float floatField;

    @Column(name = "double_field")
    private Double doubleField;

    @Enumerated(EnumType.STRING)
    @Column(name = "enum_field")
    private ReplyType enumField;

    @Column(name = "boolean_field")
    private Boolean booleanField;

    @Column(name = "local_date_field")
    private LocalDate localDateField;

    @Column(name = "zoned_date_field")
    private ZonedDateTime zonedDateField;

    @Column(name = "instant_field")
    private Instant instantField;

    @Column(name = "duration_field")
    private Duration durationField;

    @Column(name = "uuid_field")
    private UUID uuidField;

    @Lob
    @Column(name = "blob_field")
    private byte[] blobField;

    @Column(name = "blob_field_content_type")
    private String blobFieldContentType;

    @Lob
    @Column(name = "any_blob_field")
    private byte[] anyBlobField;

    @Column(name = "any_blob_field_content_type")
    private String anyBlobFieldContentType;

    @Lob
    @Column(name = "image_blob_field")
    private byte[] imageBlobField;

    @Column(name = "image_blob_field_content_type")
    private String imageBlobFieldContentType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "text_blob_field")
    private String textBlobField;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringField() {
        return this.stringField;
    }

    public TestEntity stringField(String stringField) {
        this.setStringField(stringField);
        return this;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public Integer getIntegerField() {
        return this.integerField;
    }

    public TestEntity integerField(Integer integerField) {
        this.setIntegerField(integerField);
        return this;
    }

    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    public Long getLongField() {
        return this.longField;
    }

    public TestEntity longField(Long longField) {
        this.setLongField(longField);
        return this;
    }

    public void setLongField(Long longField) {
        this.longField = longField;
    }

    public BigDecimal getBigDecimalField() {
        return this.bigDecimalField;
    }

    public TestEntity bigDecimalField(BigDecimal bigDecimalField) {
        this.setBigDecimalField(bigDecimalField);
        return this;
    }

    public void setBigDecimalField(BigDecimal bigDecimalField) {
        this.bigDecimalField = bigDecimalField;
    }

    public Float getFloatField() {
        return this.floatField;
    }

    public TestEntity floatField(Float floatField) {
        this.setFloatField(floatField);
        return this;
    }

    public void setFloatField(Float floatField) {
        this.floatField = floatField;
    }

    public Double getDoubleField() {
        return this.doubleField;
    }

    public TestEntity doubleField(Double doubleField) {
        this.setDoubleField(doubleField);
        return this;
    }

    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }

    public ReplyType getEnumField() {
        return this.enumField;
    }

    public TestEntity enumField(ReplyType enumField) {
        this.setEnumField(enumField);
        return this;
    }

    public void setEnumField(ReplyType enumField) {
        this.enumField = enumField;
    }

    public Boolean getBooleanField() {
        return this.booleanField;
    }

    public TestEntity booleanField(Boolean booleanField) {
        this.setBooleanField(booleanField);
        return this;
    }

    public void setBooleanField(Boolean booleanField) {
        this.booleanField = booleanField;
    }

    public LocalDate getLocalDateField() {
        return this.localDateField;
    }

    public TestEntity localDateField(LocalDate localDateField) {
        this.setLocalDateField(localDateField);
        return this;
    }

    public void setLocalDateField(LocalDate localDateField) {
        this.localDateField = localDateField;
    }

    public ZonedDateTime getZonedDateField() {
        return this.zonedDateField;
    }

    public TestEntity zonedDateField(ZonedDateTime zonedDateField) {
        this.setZonedDateField(zonedDateField);
        return this;
    }

    public void setZonedDateField(ZonedDateTime zonedDateField) {
        this.zonedDateField = zonedDateField;
    }

    public Instant getInstantField() {
        return this.instantField;
    }

    public TestEntity instantField(Instant instantField) {
        this.setInstantField(instantField);
        return this;
    }

    public void setInstantField(Instant instantField) {
        this.instantField = instantField;
    }

    public Duration getDurationField() {
        return this.durationField;
    }

    public TestEntity durationField(Duration durationField) {
        this.setDurationField(durationField);
        return this;
    }

    public void setDurationField(Duration durationField) {
        this.durationField = durationField;
    }

    public UUID getUuidField() {
        return this.uuidField;
    }

    public TestEntity uuidField(UUID uuidField) {
        this.setUuidField(uuidField);
        return this;
    }

    public void setUuidField(UUID uuidField) {
        this.uuidField = uuidField;
    }

    public byte[] getBlobField() {
        return this.blobField;
    }

    public TestEntity blobField(byte[] blobField) {
        this.setBlobField(blobField);
        return this;
    }

    public void setBlobField(byte[] blobField) {
        this.blobField = blobField;
    }

    public String getBlobFieldContentType() {
        return this.blobFieldContentType;
    }

    public TestEntity blobFieldContentType(String blobFieldContentType) {
        this.blobFieldContentType = blobFieldContentType;
        return this;
    }

    public void setBlobFieldContentType(String blobFieldContentType) {
        this.blobFieldContentType = blobFieldContentType;
    }

    public byte[] getAnyBlobField() {
        return this.anyBlobField;
    }

    public TestEntity anyBlobField(byte[] anyBlobField) {
        this.setAnyBlobField(anyBlobField);
        return this;
    }

    public void setAnyBlobField(byte[] anyBlobField) {
        this.anyBlobField = anyBlobField;
    }

    public String getAnyBlobFieldContentType() {
        return this.anyBlobFieldContentType;
    }

    public TestEntity anyBlobFieldContentType(String anyBlobFieldContentType) {
        this.anyBlobFieldContentType = anyBlobFieldContentType;
        return this;
    }

    public void setAnyBlobFieldContentType(String anyBlobFieldContentType) {
        this.anyBlobFieldContentType = anyBlobFieldContentType;
    }

    public byte[] getImageBlobField() {
        return this.imageBlobField;
    }

    public TestEntity imageBlobField(byte[] imageBlobField) {
        this.setImageBlobField(imageBlobField);
        return this;
    }

    public void setImageBlobField(byte[] imageBlobField) {
        this.imageBlobField = imageBlobField;
    }

    public String getImageBlobFieldContentType() {
        return this.imageBlobFieldContentType;
    }

    public TestEntity imageBlobFieldContentType(String imageBlobFieldContentType) {
        this.imageBlobFieldContentType = imageBlobFieldContentType;
        return this;
    }

    public void setImageBlobFieldContentType(String imageBlobFieldContentType) {
        this.imageBlobFieldContentType = imageBlobFieldContentType;
    }

    public String getTextBlobField() {
        return this.textBlobField;
    }

    public TestEntity textBlobField(String textBlobField) {
        this.setTextBlobField(textBlobField);
        return this;
    }

    public void setTextBlobField(String textBlobField) {
        this.textBlobField = textBlobField;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestEntity)) {
            return false;
        }
        return id != null && id.equals(((TestEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestEntity{" +
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
            ", blobFieldContentType='" + getBlobFieldContentType() + "'" +
            ", anyBlobField='" + getAnyBlobField() + "'" +
            ", anyBlobFieldContentType='" + getAnyBlobFieldContentType() + "'" +
            ", imageBlobField='" + getImageBlobField() + "'" +
            ", imageBlobFieldContentType='" + getImageBlobFieldContentType() + "'" +
            ", textBlobField='" + getTextBlobField() + "'" +
            "}";
    }
}
