import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { QuestionnaireFormService, QuestionnaireFormGroup } from './questionnaire-form.service';
import { IQuestionnaire } from '../questionnaire.model';
import { QuestionnaireService } from '../service/questionnaire.service';
import { IQuestionnaireGroup } from 'app/entities/questionnaire-group/questionnaire-group.model';
import { QuestionnaireGroupService } from 'app/entities/questionnaire-group/service/questionnaire-group.service';
import { IQuestionnaireProfile } from 'app/entities/questionnaire-profile/questionnaire-profile.model';
import { QuestionnaireProfileService } from 'app/entities/questionnaire-profile/service/questionnaire-profile.service';
import { QuestionnaireType } from 'app/entities/enumerations/questionnaire-type.model';

@Component({
  selector: 'jhi-questionnaire-update',
  templateUrl: './questionnaire-update.component.html',
})
export class QuestionnaireUpdateComponent implements OnInit {
  isSaving = false;
  questionnaire: IQuestionnaire | null = null;
  questionnaireTypeValues = Object.keys(QuestionnaireType);

  questionnaireGroupsSharedCollection: IQuestionnaireGroup[] = [];
  questionnaireProfilesSharedCollection: IQuestionnaireProfile[] = [];

  editForm: QuestionnaireFormGroup = this.questionnaireFormService.createQuestionnaireFormGroup();

  constructor(
    protected questionnaireService: QuestionnaireService,
    protected questionnaireFormService: QuestionnaireFormService,
    protected questionnaireGroupService: QuestionnaireGroupService,
    protected questionnaireProfileService: QuestionnaireProfileService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareQuestionnaireGroup = (o1: IQuestionnaireGroup | null, o2: IQuestionnaireGroup | null): boolean =>
    this.questionnaireGroupService.compareQuestionnaireGroup(o1, o2);

  compareQuestionnaireProfile = (o1: IQuestionnaireProfile | null, o2: IQuestionnaireProfile | null): boolean =>
    this.questionnaireProfileService.compareQuestionnaireProfile(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionnaire }) => {
      this.questionnaire = questionnaire;
      if (questionnaire) {
        this.updateForm(questionnaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionnaire = this.questionnaireFormService.getQuestionnaire(this.editForm);
    if (questionnaire.id !== null) {
      this.subscribeToSaveResponse(this.questionnaireService.update(questionnaire));
    } else {
      this.subscribeToSaveResponse(this.questionnaireService.create(questionnaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionnaire>>): void {
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

  protected updateForm(questionnaire: IQuestionnaire): void {
    this.questionnaire = questionnaire;
    this.questionnaireFormService.resetForm(this.editForm, questionnaire);

    this.questionnaireGroupsSharedCollection =
      this.questionnaireGroupService.addQuestionnaireGroupToCollectionIfMissing<IQuestionnaireGroup>(
        this.questionnaireGroupsSharedCollection,
        questionnaire.questionnaireGroup
      );
    this.questionnaireProfilesSharedCollection =
      this.questionnaireProfileService.addQuestionnaireProfileToCollectionIfMissing<IQuestionnaireProfile>(
        this.questionnaireProfilesSharedCollection,
        questionnaire.questionnaireProfile
      );
  }

  protected loadRelationshipsOptions(): void {
    this.questionnaireGroupService
      .query()
      .pipe(map((res: HttpResponse<IQuestionnaireGroup[]>) => res.body ?? []))
      .pipe(
        map((questionnaireGroups: IQuestionnaireGroup[]) =>
          this.questionnaireGroupService.addQuestionnaireGroupToCollectionIfMissing<IQuestionnaireGroup>(
            questionnaireGroups,
            this.questionnaire?.questionnaireGroup
          )
        )
      )
      .subscribe((questionnaireGroups: IQuestionnaireGroup[]) => (this.questionnaireGroupsSharedCollection = questionnaireGroups));

    this.questionnaireProfileService
      .query()
      .pipe(map((res: HttpResponse<IQuestionnaireProfile[]>) => res.body ?? []))
      .pipe(
        map((questionnaireProfiles: IQuestionnaireProfile[]) =>
          this.questionnaireProfileService.addQuestionnaireProfileToCollectionIfMissing<IQuestionnaireProfile>(
            questionnaireProfiles,
            this.questionnaire?.questionnaireProfile
          )
        )
      )
      .subscribe((questionnaireProfiles: IQuestionnaireProfile[]) => (this.questionnaireProfilesSharedCollection = questionnaireProfiles));
  }
}
