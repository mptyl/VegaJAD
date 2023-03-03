import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuestionnaireGroup, NewQuestionnaireGroup } from '../questionnaire-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuestionnaireGroup for edit and NewQuestionnaireGroupFormGroupInput for create.
 */
type QuestionnaireGroupFormGroupInput = IQuestionnaireGroup | PartialWithRequiredKeyOf<NewQuestionnaireGroup>;

type QuestionnaireGroupFormDefaults = Pick<NewQuestionnaireGroup, 'id'>;

type QuestionnaireGroupFormGroupContent = {
  id: FormControl<IQuestionnaireGroup['id'] | NewQuestionnaireGroup['id']>;
  name: FormControl<IQuestionnaireGroup['name']>;
  description: FormControl<IQuestionnaireGroup['description']>;
};

export type QuestionnaireGroupFormGroup = FormGroup<QuestionnaireGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireGroupFormService {
  createQuestionnaireGroupFormGroup(questionnaireGroup: QuestionnaireGroupFormGroupInput = { id: null }): QuestionnaireGroupFormGroup {
    const questionnaireGroupRawValue = {
      ...this.getFormDefaults(),
      ...questionnaireGroup,
    };
    return new FormGroup<QuestionnaireGroupFormGroupContent>({
      id: new FormControl(
        { value: questionnaireGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(questionnaireGroupRawValue.name),
      description: new FormControl(questionnaireGroupRawValue.description),
    });
  }

  getQuestionnaireGroup(form: QuestionnaireGroupFormGroup): IQuestionnaireGroup | NewQuestionnaireGroup {
    return form.getRawValue() as IQuestionnaireGroup | NewQuestionnaireGroup;
  }

  resetForm(form: QuestionnaireGroupFormGroup, questionnaireGroup: QuestionnaireGroupFormGroupInput): void {
    const questionnaireGroupRawValue = { ...this.getFormDefaults(), ...questionnaireGroup };
    form.reset(
      {
        ...questionnaireGroupRawValue,
        id: { value: questionnaireGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QuestionnaireGroupFormDefaults {
    return {
      id: null,
    };
  }
}
