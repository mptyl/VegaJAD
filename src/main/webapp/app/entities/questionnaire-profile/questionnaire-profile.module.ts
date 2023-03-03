import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionnaireProfileComponent } from './list/questionnaire-profile.component';
import { QuestionnaireProfileDetailComponent } from './detail/questionnaire-profile-detail.component';
import { QuestionnaireProfileUpdateComponent } from './update/questionnaire-profile-update.component';
import { QuestionnaireProfileDeleteDialogComponent } from './delete/questionnaire-profile-delete-dialog.component';
import { QuestionnaireProfileRoutingModule } from './route/questionnaire-profile-routing.module';

@NgModule({
  imports: [SharedModule, QuestionnaireProfileRoutingModule],
  declarations: [
    QuestionnaireProfileComponent,
    QuestionnaireProfileDetailComponent,
    QuestionnaireProfileUpdateComponent,
    QuestionnaireProfileDeleteDialogComponent,
  ],
})
export class QuestionnaireProfileModule {}
