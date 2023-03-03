import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionnaireProfile } from '../questionnaire-profile.model';
import { QuestionnaireProfileService } from '../service/questionnaire-profile.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './questionnaire-profile-delete-dialog.component.html',
})
export class QuestionnaireProfileDeleteDialogComponent {
  questionnaireProfile?: IQuestionnaireProfile;

  constructor(protected questionnaireProfileService: QuestionnaireProfileService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionnaireProfileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
