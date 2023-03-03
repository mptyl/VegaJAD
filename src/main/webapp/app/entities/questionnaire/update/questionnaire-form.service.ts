import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuestionnaire, NewQuestionnaire } from '../questionnaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuestionnaire for edit and NewQuestionnaireFormGroupInput for create.
 */
type QuestionnaireFormGroupInput = IQuestionnaire | PartialWithRequiredKeyOf<NewQuestionnaire>;

type QuestionnaireFormDefaults = Pick<NewQuestionnaire, 'id'>;

type QuestionnaireFormGroupContent = {
  id: FormControl<IQuestionnaire['id'] | NewQuestionnaire['id']>;
  name: FormControl<IQuestionnaire['name']>;
  version: FormControl<IQuestionnaire['version']>;
  title: FormControl<IQuestionnaire['title']>;
  subTitle: FormControl<IQuestionnaire['subTitle']>;
  notes: FormControl<IQuestionnaire['notes']>;
  image: FormControl<IQuestionnaire['image']>;
  imageAlt: FormControl<IQuestionnaire['imageAlt']>;
  instructions: FormControl<IQuestionnaire['instructions']>;
  compilationTime: FormControl<IQuestionnaire['compilationTime']>;
  forcedTerminationTime: FormControl<IQuestionnaire['forcedTerminationTime']>;
  usedSeconds: FormControl<IQuestionnaire['usedSeconds']>;
  status: FormControl<IQuestionnaire['status']>;
  xml: FormControl<IQuestionnaire['xml']>;
  json: FormControl<IQuestionnaire['json']>;
  saveText: FormControl<IQuestionnaire['saveText']>;
  searchText: FormControl<IQuestionnaire['searchText']>;
  subjectToEvaluation: FormControl<IQuestionnaire['subjectToEvaluation']>;
  questionnaireType: FormControl<IQuestionnaire['questionnaireType']>;
  attachments: FormControl<IQuestionnaire['attachments']>;
  questionnaireGroup: FormControl<IQuestionnaire['questionnaireGroup']>;
  questionnaireProfile: FormControl<IQuestionnaire['questionnaireProfile']>;
};

export type QuestionnaireFormGroup = FormGroup<QuestionnaireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireFormService {
  createQuestionnaireFormGroup(questionnaire: QuestionnaireFormGroupInput = { id: null }): QuestionnaireFormGroup {
    const questionnaireRawValue = {
      ...this.getFormDefaults(),
      ...questionnaire,
    };
    return new FormGroup<QuestionnaireFormGroupContent>({
      id: new FormControl(
        { value: questionnaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(questionnaireRawValue.name),
      version: new FormControl(questionnaireRawValue.version),
      title: new FormControl(questionnaireRawValue.title),
      subTitle: new FormControl(questionnaireRawValue.subTitle),
      notes: new FormControl(questionnaireRawValue.notes),
      image: new FormControl(questionnaireRawValue.image),
      imageAlt: new FormControl(questionnaireRawValue.imageAlt),
      instructions: new FormControl(questionnaireRawValue.instructions),
      compilationTime: new FormControl(questionnaireRawValue.compilationTime),
      forcedTerminationTime: new FormControl(questionnaireRawValue.forcedTerminationTime),
      usedSeconds: new FormControl(questionnaireRawValue.usedSeconds),
      status: new FormControl(questionnaireRawValue.status),
      xml: new FormControl(questionnaireRawValue.xml),
      json: new FormControl(questionnaireRawValue.json),
      saveText: new FormControl(questionnaireRawValue.saveText),
      searchText: new FormControl(questionnaireRawValue.searchText),
      subjectToEvaluation: new FormControl(questionnaireRawValue.subjectToEvaluation),
      questionnaireType: new FormControl(questionnaireRawValue.questionnaireType),
      attachments: new FormControl(questionnaireRawValue.attachments),
      questionnaireGroup: new FormControl(questionnaireRawValue.questionnaireGroup),
      questionnaireProfile: new FormControl(questionnaireRawValue.questionnaireProfile),
    });
  }

  getQuestionnaire(form: QuestionnaireFormGroup): IQuestionnaire | NewQuestionnaire {
    return form.getRawValue() as IQuestionnaire | NewQuestionnaire;
  }

  resetForm(form: QuestionnaireFormGroup, questionnaire: QuestionnaireFormGroupInput): void {
    const questionnaireRawValue = { ...this.getFormDefaults(), ...questionnaire };
    form.reset(
      {
        ...questionnaireRawValue,
        id: { value: questionnaireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QuestionnaireFormDefaults {
    return {
      id: null,
    };
  }
}
