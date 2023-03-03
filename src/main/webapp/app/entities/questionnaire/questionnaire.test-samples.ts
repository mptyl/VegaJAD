import { QuestionnaireType } from 'app/entities/enumerations/questionnaire-type.model';

import { IQuestionnaire, NewQuestionnaire } from './questionnaire.model';

export const sampleWithRequiredData: IQuestionnaire = {
  id: 6169,
};

export const sampleWithPartialData: IQuestionnaire = {
  id: 80470,
  name: 'Cambridgeshire Garden Movies',
  version: 'Struttura Bike sky',
  subTitle: 'Rubber Plastic Account',
  image: 'Shoes Unbranded',
  subjectToEvaluation: 'Granite application',
  attachments: 40317,
};

export const sampleWithFullData: IQuestionnaire = {
  id: 2459,
  name: 'Cotton Frozen Azerbaijan',
  version: 'Siena',
  title: 'proattive',
  subTitle: 'Checking Steel',
  notes: 'Loan Moldovan',
  image: 'Como',
  imageAlt: 'interattiva',
  instructions: 'online Strategist soluzioni',
  compilationTime: 98285,
  forcedTerminationTime: 58527,
  usedSeconds: 86722,
  status: 65856,
  xml: 'Account',
  json: 'input',
  saveText: 'Solomon Dynamic',
  searchText: '24/7 Contingenza state',
  subjectToEvaluation: 'sinergiche',
  questionnaireType: QuestionnaireType['ANCORE'],
  attachments: 20728,
};

export const sampleWithNewData: NewQuestionnaire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
