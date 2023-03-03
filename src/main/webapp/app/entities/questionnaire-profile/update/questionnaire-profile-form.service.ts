import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuestionnaireProfile, NewQuestionnaireProfile } from '../questionnaire-profile.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuestionnaireProfile for edit and NewQuestionnaireProfileFormGroupInput for create.
 */
type QuestionnaireProfileFormGroupInput = IQuestionnaireProfile | PartialWithRequiredKeyOf<NewQuestionnaireProfile>;

type QuestionnaireProfileFormDefaults = Pick<NewQuestionnaireProfile, 'id'>;

type QuestionnaireProfileFormGroupContent = {
  id: FormControl<IQuestionnaireProfile['id'] | NewQuestionnaireProfile['id']>;
  description: FormControl<IQuestionnaireProfile['description']>;
};

export type QuestionnaireProfileFormGroup = FormGroup<QuestionnaireProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireProfileFormService {
  createQuestionnaireProfileFormGroup(
    questionnaireProfile: QuestionnaireProfileFormGroupInput = { id: null }
  ): QuestionnaireProfileFormGroup {
    const questionnaireProfileRawValue = {
      ...this.getFormDefaults(),
      ...questionnaireProfile,
    };
    return new FormGroup<QuestionnaireProfileFormGroupContent>({
      id: new FormControl(
        { value: questionnaireProfileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(questionnaireProfileRawValue.description),
    });
  }

  getQuestionnaireProfile(form: QuestionnaireProfileFormGroup): IQuestionnaireProfile | NewQuestionnaireProfile {
    return form.getRawValue() as IQuestionnaireProfile | NewQuestionnaireProfile;
  }

  resetForm(form: QuestionnaireProfileFormGroup, questionnaireProfile: QuestionnaireProfileFormGroupInput): void {
    const questionnaireProfileRawValue = { ...this.getFormDefaults(), ...questionnaireProfile };
    form.reset(
      {
        ...questionnaireProfileRawValue,
        id: { value: questionnaireProfileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QuestionnaireProfileFormDefaults {
    return {
      id: null,
    };
  }
}
