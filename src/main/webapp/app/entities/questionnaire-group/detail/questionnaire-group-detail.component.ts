import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionnaireGroup } from '../questionnaire-group.model';

@Component({
  selector: 'jhi-questionnaire-group-detail',
  templateUrl: './questionnaire-group-detail.component.html',
})
export class QuestionnaireGroupDetailComponent implements OnInit {
  questionnaireGroup: IQuestionnaireGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionnaireGroup }) => {
      this.questionnaireGroup = questionnaireGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
