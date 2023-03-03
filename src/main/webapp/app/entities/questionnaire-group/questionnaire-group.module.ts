import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionnaireGroupComponent } from './list/questionnaire-group.component';
import { QuestionnaireGroupDetailComponent } from './detail/questionnaire-group-detail.component';
import { QuestionnaireGroupUpdateComponent } from './update/questionnaire-group-update.component';
import { QuestionnaireGroupDeleteDialogComponent } from './delete/questionnaire-group-delete-dialog.component';
import { QuestionnaireGroupRoutingModule } from './route/questionnaire-group-routing.module';

@NgModule({
  imports: [SharedModule, QuestionnaireGroupRoutingModule],
  declarations: [
    QuestionnaireGroupComponent,
    QuestionnaireGroupDetailComponent,
    QuestionnaireGroupUpdateComponent,
    QuestionnaireGroupDeleteDialogComponent,
  ],
})
export class QuestionnaireGroupModule {}
