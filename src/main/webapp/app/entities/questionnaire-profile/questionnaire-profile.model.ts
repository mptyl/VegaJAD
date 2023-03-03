export interface IQuestionnaireProfile {
  id: number;
  description?: string | null;
}

export type NewQuestionnaireProfile = Omit<IQuestionnaireProfile, 'id'> & { id: null };
