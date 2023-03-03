import { IQuestionnaireGroup } from 'app/entities/questionnaire-group/questionnaire-group.model';
import { IQuestionnaireProfile } from 'app/entities/questionnaire-profile/questionnaire-profile.model';
import { QuestionnaireType } from 'app/entities/enumerations/questionnaire-type.model';

export interface IQuestionnaire {
  id: number;
  name?: string | null;
  version?: string | null;
  title?: string | null;
  subTitle?: string | null;
  notes?: string | null;
  image?: string | null;
  imageAlt?: string | null;
  instructions?: string | null;
  compilationTime?: number | null;
  forcedTerminationTime?: number | null;
  usedSeconds?: number | null;
  status?: number | null;
  xml?: string | null;
  json?: string | null;
  saveText?: string | null;
  searchText?: string | null;
  subjectToEvaluation?: string | null;
  questionnaireType?: QuestionnaireType | null;
  attachments?: number | null;
  questionnaireGroup?: Pick<IQuestionnaireGroup, 'id'> | null;
  questionnaireProfile?: Pick<IQuestionnaireProfile, 'id'> | null;
}

export type NewQuestionnaire = Omit<IQuestionnaire, 'id'> & { id: null };
