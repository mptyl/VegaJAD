import dayjs from 'dayjs/esm';

import { ReplyType } from 'app/entities/enumerations/reply-type.model';

import { ITestEntity, NewTestEntity } from './test-entity.model';

export const sampleWithRequiredData: ITestEntity = {
  id: 60484,
};

export const sampleWithPartialData: ITestEntity = {
  id: 35068,
  stringField: 'Dinar Agent di',
  integerField: 697,
  longField: 79208,
  floatField: 55463,
  doubleField: 87186,
  enumField: ReplyType['SELECT'],
  booleanField: true,
  zonedDateField: dayjs('2023-03-03T10:48'),
  durationField: '56863',
  blobField: '../fake-data/blob/hipster.png',
  blobFieldContentType: 'unknown',
  textBlobField: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ITestEntity = {
  id: 73613,
  stringField: 'Avon Korean',
  integerField: 50314,
  longField: 23745,
  bigDecimalField: 11694,
  floatField: 15971,
  doubleField: 60810,
  enumField: ReplyType['RANGE'],
  booleanField: true,
  localDateField: dayjs('2023-03-03'),
  zonedDateField: dayjs('2023-03-03T07:50'),
  instantField: dayjs('2023-03-03T01:28'),
  durationField: '28529',
  uuidField: '23c35b42-898d-48ea-90ae-cdbdd9294353',
  blobField: '../fake-data/blob/hipster.png',
  blobFieldContentType: 'unknown',
  anyBlobField: '../fake-data/blob/hipster.png',
  anyBlobFieldContentType: 'unknown',
  imageBlobField: '../fake-data/blob/hipster.png',
  imageBlobFieldContentType: 'unknown',
  textBlobField: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTestEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
