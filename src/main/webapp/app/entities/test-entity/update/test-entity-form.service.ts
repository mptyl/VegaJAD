import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITestEntity, NewTestEntity } from '../test-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestEntity for edit and NewTestEntityFormGroupInput for create.
 */
type TestEntityFormGroupInput = ITestEntity | PartialWithRequiredKeyOf<NewTestEntity>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITestEntity | NewTestEntity> = Omit<T, 'zonedDateField' | 'instantField'> & {
  zonedDateField?: string | null;
  instantField?: string | null;
};

type TestEntityFormRawValue = FormValueOf<ITestEntity>;

type NewTestEntityFormRawValue = FormValueOf<NewTestEntity>;

type TestEntityFormDefaults = Pick<NewTestEntity, 'id' | 'booleanField' | 'zonedDateField' | 'instantField'>;

type TestEntityFormGroupContent = {
  id: FormControl<TestEntityFormRawValue['id'] | NewTestEntity['id']>;
  stringField: FormControl<TestEntityFormRawValue['stringField']>;
  integerField: FormControl<TestEntityFormRawValue['integerField']>;
  longField: FormControl<TestEntityFormRawValue['longField']>;
  bigDecimalField: FormControl<TestEntityFormRawValue['bigDecimalField']>;
  floatField: FormControl<TestEntityFormRawValue['floatField']>;
  doubleField: FormControl<TestEntityFormRawValue['doubleField']>;
  enumField: FormControl<TestEntityFormRawValue['enumField']>;
  booleanField: FormControl<TestEntityFormRawValue['booleanField']>;
  localDateField: FormControl<TestEntityFormRawValue['localDateField']>;
  zonedDateField: FormControl<TestEntityFormRawValue['zonedDateField']>;
  instantField: FormControl<TestEntityFormRawValue['instantField']>;
  durationField: FormControl<TestEntityFormRawValue['durationField']>;
  uuidField: FormControl<TestEntityFormRawValue['uuidField']>;
  blobField: FormControl<TestEntityFormRawValue['blobField']>;
  blobFieldContentType: FormControl<TestEntityFormRawValue['blobFieldContentType']>;
  anyBlobField: FormControl<TestEntityFormRawValue['anyBlobField']>;
  anyBlobFieldContentType: FormControl<TestEntityFormRawValue['anyBlobFieldContentType']>;
  imageBlobField: FormControl<TestEntityFormRawValue['imageBlobField']>;
  imageBlobFieldContentType: FormControl<TestEntityFormRawValue['imageBlobFieldContentType']>;
  textBlobField: FormControl<TestEntityFormRawValue['textBlobField']>;
};

export type TestEntityFormGroup = FormGroup<TestEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestEntityFormService {
  createTestEntityFormGroup(testEntity: TestEntityFormGroupInput = { id: null }): TestEntityFormGroup {
    const testEntityRawValue = this.convertTestEntityToTestEntityRawValue({
      ...this.getFormDefaults(),
      ...testEntity,
    });
    return new FormGroup<TestEntityFormGroupContent>({
      id: new FormControl(
        { value: testEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      stringField: new FormControl(testEntityRawValue.stringField),
      integerField: new FormControl(testEntityRawValue.integerField),
      longField: new FormControl(testEntityRawValue.longField),
      bigDecimalField: new FormControl(testEntityRawValue.bigDecimalField),
      floatField: new FormControl(testEntityRawValue.floatField),
      doubleField: new FormControl(testEntityRawValue.doubleField),
      enumField: new FormControl(testEntityRawValue.enumField),
      booleanField: new FormControl(testEntityRawValue.booleanField),
      localDateField: new FormControl(testEntityRawValue.localDateField),
      zonedDateField: new FormControl(testEntityRawValue.zonedDateField),
      instantField: new FormControl(testEntityRawValue.instantField),
      durationField: new FormControl(testEntityRawValue.durationField),
      uuidField: new FormControl(testEntityRawValue.uuidField),
      blobField: new FormControl(testEntityRawValue.blobField),
      blobFieldContentType: new FormControl(testEntityRawValue.blobFieldContentType),
      anyBlobField: new FormControl(testEntityRawValue.anyBlobField),
      anyBlobFieldContentType: new FormControl(testEntityRawValue.anyBlobFieldContentType),
      imageBlobField: new FormControl(testEntityRawValue.imageBlobField),
      imageBlobFieldContentType: new FormControl(testEntityRawValue.imageBlobFieldContentType),
      textBlobField: new FormControl(testEntityRawValue.textBlobField),
    });
  }

  getTestEntity(form: TestEntityFormGroup): ITestEntity | NewTestEntity {
    return this.convertTestEntityRawValueToTestEntity(form.getRawValue() as TestEntityFormRawValue | NewTestEntityFormRawValue);
  }

  resetForm(form: TestEntityFormGroup, testEntity: TestEntityFormGroupInput): void {
    const testEntityRawValue = this.convertTestEntityToTestEntityRawValue({ ...this.getFormDefaults(), ...testEntity });
    form.reset(
      {
        ...testEntityRawValue,
        id: { value: testEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TestEntityFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      booleanField: false,
      zonedDateField: currentTime,
      instantField: currentTime,
    };
  }

  private convertTestEntityRawValueToTestEntity(
    rawTestEntity: TestEntityFormRawValue | NewTestEntityFormRawValue
  ): ITestEntity | NewTestEntity {
    return {
      ...rawTestEntity,
      zonedDateField: dayjs(rawTestEntity.zonedDateField, DATE_TIME_FORMAT),
      instantField: dayjs(rawTestEntity.instantField, DATE_TIME_FORMAT),
    };
  }

  private convertTestEntityToTestEntityRawValue(
    testEntity: ITestEntity | (Partial<NewTestEntity> & TestEntityFormDefaults)
  ): TestEntityFormRawValue | PartialWithRequiredKeyOf<NewTestEntityFormRawValue> {
    return {
      ...testEntity,
      zonedDateField: testEntity.zonedDateField ? testEntity.zonedDateField.format(DATE_TIME_FORMAT) : undefined,
      instantField: testEntity.instantField ? testEntity.instantField.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
