import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionnaireProfile } from '../questionnaire-profile.model';

@Component({
  selector: 'jhi-questionnaire-profile-detail',
  templateUrl: './questionnaire-profile-detail.component.html',
})
export class QuestionnaireProfileDetailComponent implements OnInit {
  questionnaireProfile: IQuestionnaireProfile | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionnaireProfile }) => {
      this.questionnaireProfile = questionnaireProfile;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
