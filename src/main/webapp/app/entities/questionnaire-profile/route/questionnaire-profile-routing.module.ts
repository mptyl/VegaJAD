import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionnaireProfileComponent } from '../list/questionnaire-profile.component';
import { QuestionnaireProfileDetailComponent } from '../detail/questionnaire-profile-detail.component';
import { QuestionnaireProfileUpdateComponent } from '../update/questionnaire-profile-update.component';
import { QuestionnaireProfileRoutingResolveService } from './questionnaire-profile-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const questionnaireProfileRoute: Routes = [
  {
    path: '',
    component: QuestionnaireProfileComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionnaireProfileDetailComponent,
    resolve: {
      questionnaireProfile: QuestionnaireProfileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionnaireProfileUpdateComponent,
    resolve: {
      questionnaireProfile: QuestionnaireProfileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionnaireProfileUpdateComponent,
    resolve: {
      questionnaireProfile: QuestionnaireProfileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionnaireProfileRoute)],
  exports: [RouterModule],
})
export class QuestionnaireProfileRoutingModule {}
