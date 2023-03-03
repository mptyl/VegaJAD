import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { QuestionnaireGroupFormService, QuestionnaireGroupFormGroup } from './questionnaire-group-form.service';
import { IQuestionnaireGroup } from '../questionnaire-group.model';
import { QuestionnaireGroupService } from '../service/questionnaire-group.service';

@Component({
  selector: 'jhi-questionnaire-group-update',
  templateUrl: './questionnaire-group-update.component.html',
})
export class QuestionnaireGroupUpdateComponent implements OnInit {
  isSaving = false;
  questionnaireGroup: IQuestionnaireGroup | null = null;

  editForm: QuestionnaireGroupFormGroup = this.questionnaireGroupFormService.createQuestionnaireGroupFormGroup();

  constructor(
    protected questionnaireGroupService: QuestionnaireGroupService,
    protected questionnaireGroupFormService: QuestionnaireGroupFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionnaireGroup }) => {
      this.questionnaireGroup = questionnaireGroup;
      if (questionnaireGroup) {
        this.updateForm(questionnaireGroup);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionnaireGroup = this.questionnaireGroupFormService.getQuestionnaireGroup(this.editForm);
    if (questionnaireGroup.id !== null) {
      this.subscribeToSaveResponse(this.questionnaireGroupService.update(questionnaireGroup));
    } else {
      this.subscribeToSaveResponse(this.questionnaireGroupService.create(questionnaireGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionnaireGroup>>): void {
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

  protected updateForm(questionnaireGroup: IQuestionnaireGroup): void {
    this.questionnaireGroup = questionnaireGroup;
    this.questionnaireGroupFormService.resetForm(this.editForm, questionnaireGroup);
  }
}
