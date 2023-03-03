import { IQuestionnaireGroup, NewQuestionnaireGroup } from './questionnaire-group.model';

export const sampleWithRequiredData: IQuestionnaireGroup = {
  id: 93921,
};

export const sampleWithPartialData: IQuestionnaireGroup = {
  id: 97499,
};

export const sampleWithFullData: IQuestionnaireGroup = {
  id: 78717,
  name: 'Uzbekistan Strada',
  description: 'target',
};

export const sampleWithNewData: NewQuestionnaireGroup = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
