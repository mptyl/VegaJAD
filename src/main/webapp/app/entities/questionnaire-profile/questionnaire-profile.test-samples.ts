import { IQuestionnaireProfile, NewQuestionnaireProfile } from './questionnaire-profile.model';

export const sampleWithRequiredData: IQuestionnaireProfile = {
  id: 76918,
};

export const sampleWithPartialData: IQuestionnaireProfile = {
  id: 54146,
  description: 'estensioni Contrada Sassari',
};

export const sampleWithFullData: IQuestionnaireProfile = {
  id: 46312,
  description: 'Yen withdrawal deposit',
};

export const sampleWithNewData: NewQuestionnaireProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
