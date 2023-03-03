export interface IQuestionnaireGroup {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewQuestionnaireGroup = Omit<IQuestionnaireGroup, 'id'> & { id: null };
