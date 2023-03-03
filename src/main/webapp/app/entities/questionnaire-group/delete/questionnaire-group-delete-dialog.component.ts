import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionnaireGroup } from '../questionnaire-group.model';
import { QuestionnaireGroupService } from '../service/questionnaire-group.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './questionnaire-group-delete-dialog.component.html',
})
export class QuestionnaireGroupDeleteDialogComponent {
  questionnaireGroup?: IQuestionnaireGroup;

  constructor(protected questionnaireGroupService: QuestionnaireGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionnaireGroupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
