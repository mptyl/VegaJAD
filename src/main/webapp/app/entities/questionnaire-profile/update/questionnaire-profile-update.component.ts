import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { QuestionnaireProfileFormService, QuestionnaireProfileFormGroup } from './questionnaire-profile-form.service';
import { IQuestionnaireProfile } from '../questionnaire-profile.model';
import { QuestionnaireProfileService } from '../service/questionnaire-profile.service';

@Component({
  selector: 'jhi-questionnaire-profile-update',
  templateUrl: './questionnaire-profile-update.component.html',
})
export class QuestionnaireProfileUpdateComponent implements OnInit {
  isSaving = false;
  questionnaireProfile: IQuestionnaireProfile | null = null;

  editForm: QuestionnaireProfileFormGroup = this.questionnaireProfileFormService.createQuestionnaireProfileFormGroup();

  constructor(
    protected questionnaireProfileService: QuestionnaireProfileService,
    protected questionnaireProfileFormService: QuestionnaireProfileFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionnaireProfile }) => {
      this.questionnaireProfile = questionnaireProfile;
      if (questionnaireProfile) {
        this.updateForm(questionnaireProfile);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionnaireProfile = this.questionnaireProfileFormService.getQuestionnaireProfile(this.editForm);
    if (questionnaireProfile.id !== null) {
      this.subscribeToSaveResponse(this.questionnaireProfileService.update(questionnaireProfile));
    } else {
      this.subscribeToSaveResponse(this.questionnaireProfileService.create(questionnaireProfile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionnaireProfile>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(questionnaireProfile: IQuestionnaireProfile): void {
    this.questionnaireProfile = questionnaireProfile;
    this.questionnaireProfileFormService.resetForm(this.editForm, questionnaireProfile);
  }
}
