package it.tylconsulting.vega.service.criteria;

import it.tylconsulting.vega.domain.enumeration.ReplyType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link it.tylconsulting.vega.domain.TestEntity} entity. This class is used
 * in {@link it.tylconsulting.vega.web.rest.TestEntityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-entities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestEntityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReplyType
     */
    public static class ReplyTypeFilter extends Filter<ReplyType> {

        public ReplyTypeFilter() {}

        public ReplyTypeFilter(ReplyTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReplyTypeFilter copy() {
            return new ReplyTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter stringField;

    private IntegerFilter integerField;

    private LongFilter longField;

    private BigDecimalFilter bigDecimalField;

    private FloatFilter floatField;

    private DoubleFilter doubleField;

    private ReplyTypeFilter enumField;

    private BooleanFilter booleanField;

    private LocalDateFilter localDateField;

    private ZonedDateTimeFilter zonedDateField;

    private InstantFilter instantField;

    private DurationFilter durationField;

    private UUIDFilter uuidField;

    private Boolean distinct;

    public TestEntityCriteria() {}

    public TestEntityCriteria(TestEntityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.stringField = other.stringField == null ? null : other.stringField.copy();
        this.integerField = other.integerField == null ? null : other.integerField.copy();
        this.longField = other.longField == null ? null : other.longField.copy();
        this.bigDecimalField = other.bigDecimalField == null ? null : other.bigDecimalField.copy();
        this.floatField = other.floatField == null ? null : other.floatField.copy();
        this.doubleField = other.doubleField == null ? null : other.doubleField.copy();
        this.enumField = other.enumField == null ? null : other.enumField.copy();
        this.booleanField = other.booleanField == null ? null : other.booleanField.copy();
        this.localDateField = other.localDateField == null ? null : other.localDateField.copy();
        this.zonedDateField = other.zonedDateField == null ? null : other.zonedDateField.copy();
        this.instantField = other.instantField == null ? null : other.instantField.copy();
        this.durationField = other.durationField == null ? null : other.durationField.copy();
        this.uuidField = other.uuidField == null ? null : other.uuidField.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestEntityCriteria copy() {
        return new TestEntityCriteria(this);
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

    public StringFilter getStringField() {
        return stringField;
    }

    public StringFilter stringField() {
        if (stringField == null) {
            stringField = new StringFilter();
        }
        return stringField;
    }

    public void setStringField(StringFilter stringField) {
        this.stringField = stringField;
    }

    public IntegerFilter getIntegerField() {
        return integerField;
    }

    public IntegerFilter integerField() {
        if (integerField == null) {
            integerField = new IntegerFilter();
        }
        return integerField;
    }

    public void setIntegerField(IntegerFilter integerField) {
        this.integerField = integerField;
    }

    public LongFilter getLongField() {
        return longField;
    }

    public LongFilter longField() {
        if (longField == null) {
            longField = new LongFilter();
        }
        return longField;
    }

    public void setLongField(LongFilter longField) {
        this.longField = longField;
    }

    public BigDecimalFilter getBigDecimalField() {
        return bigDecimalField;
    }

    public BigDecimalFilter bigDecimalField() {
        if (bigDecimalField == null) {
            bigDecimalField = new BigDecimalFilter();
        }
        return bigDecimalField;
    }

    public void setBigDecimalField(BigDecimalFilter bigDecimalField) {
        this.bigDecimalField = bigDecimalField;
    }

    public FloatFilter getFloatField() {
        return floatField;
    }

    public FloatFilter floatField() {
        if (floatField == null) {
            floatField = new FloatFilter();
        }
        return floatField;
    }

    public void setFloatField(FloatFilter floatField) {
        this.floatField = floatField;
    }

    public DoubleFilter getDoubleField() {
        return doubleField;
    }

    public DoubleFilter doubleField() {
        if (doubleField == null) {
            doubleField = new DoubleFilter();
        }
        return doubleField;
    }

    public void setDoubleField(DoubleFilter doubleField) {
        this.doubleField = doubleField;
    }

    public ReplyTypeFilter getEnumField() {
        return enumField;
    }

    public ReplyTypeFilter enumField() {
        if (enumField == null) {
            enumField = new ReplyTypeFilter();
        }
        return enumField;
    }

    public void setEnumField(ReplyTypeFilter enumField) {
        this.enumField = enumField;
    }

    public BooleanFilter getBooleanField() {
        return booleanField;
    }

    public BooleanFilter booleanField() {
        if (booleanField == null) {
            booleanField = new BooleanFilter();
        }
        return booleanField;
    }

    public void setBooleanField(BooleanFilter booleanField) {
        this.booleanField = booleanField;
    }

    public LocalDateFilter getLocalDateField() {
        return localDateField;
    }

    public LocalDateFilter localDateField() {
        if (localDateField == null) {
            localDateField = new LocalDateFilter();
        }
        return localDateField;
    }

    public void setLocalDateField(LocalDateFilter localDateField) {
        this.localDateField = localDateField;
    }

    public ZonedDateTimeFilter getZonedDateField() {
        return zonedDateField;
    }

    public ZonedDateTimeFilter zonedDateField() {
        if (zonedDateField == null) {
            zonedDateField = new ZonedDateTimeFilter();
        }
        return zonedDateField;
    }

    public void setZonedDateField(ZonedDateTimeFilter zonedDateField) {
        this.zonedDateField = zonedDateField;
    }

    public InstantFilter getInstantField() {
        return instantField;
    }

    public InstantFilter instantField() {
        if (instantField == null) {
            instantField = new InstantFilter();
        }
        return instantField;
    }

    public void setInstantField(InstantFilter instantField) {
        this.instantField = instantField;
    }

    public DurationFilter getDurationField() {
        return durationField;
    }

    public DurationFilter durationField() {
        if (durationField == null) {
            durationField = new DurationFilter();
        }
        return durationField;
    }

    public void setDurationField(DurationFilter durationField) {
        this.durationField = durationField;
    }

    public UUIDFilter getUuidField() {
        return uuidField;
    }

    public UUIDFilter uuidField() {
        if (uuidField == null) {
            uuidField = new UUIDFilter();
        }
        return uuidField;
    }

    public void setUuidField(UUIDFilter uuidField) {
        this.uuidField = uuidField;
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
        final TestEntityCriteria that = (TestEntityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(stringField, that.stringField) &&
            Objects.equals(integerField, that.integerField) &&
            Objects.equals(longField, that.longField) &&
            Objects.equals(bigDecimalField, that.bigDecimalField) &&
            Objects.equals(floatField, that.floatField) &&
            Objects.equals(doubleField, that.doubleField) &&
            Objects.equals(enumField, that.enumField) &&
            Objects.equals(booleanField, that.booleanField) &&
            Objects.equals(localDateField, that.localDateField) &&
            Objects.equals(zonedDateField, that.zonedDateField) &&
            Objects.equals(instantField, that.instantField) &&
            Objects.equals(durationField, that.durationField) &&
            Objects.equals(uuidField, that.uuidField) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            stringField,
            integerField,
            longField,
            bigDecimalField,
            floatField,
            doubleField,
            enumField,
            booleanField,
            localDateField,
            zonedDateField,
            instantField,
            durationField,
            uuidField,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestEntityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (stringField != null ? "stringField=" + stringField + ", " : "") +
            (integerField != null ? "integerField=" + integerField + ", " : "") +
            (longField != null ? "longField=" + longField + ", " : "") +
            (bigDecimalField != null ? "bigDecimalField=" + bigDecimalField + ", " : "") +
            (floatField != null ? "floatField=" + floatField + ", " : "") +
            (doubleField != null ? "doubleField=" + doubleField + ", " : "") +
            (enumField != null ? "enumField=" + enumField + ", " : "") +
            (booleanField != null ? "booleanField=" + booleanField + ", " : "") +
            (localDateField != null ? "localDateField=" + localDateField + ", " : "") +
            (zonedDateField != null ? "zonedDateField=" + zonedDateField + ", " : "") +
            (instantField != null ? "instantField=" + instantField + ", " : "") +
            (durationField != null ? "durationField=" + durationField + ", " : "") +
            (uuidField != null ? "uuidField=" + uuidField + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
