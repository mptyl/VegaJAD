import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionnaireGroupComponent } from '../list/questionnaire-group.component';
import { QuestionnaireGroupDetailComponent } from '../detail/questionnaire-group-detail.component';
import { QuestionnaireGroupUpdateComponent } from '../update/questionnaire-group-update.component';
import { QuestionnaireGroupRoutingResolveService } from './questionnaire-group-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const questionnaireGroupRoute: Routes = [
  {
    path: '',
    component: QuestionnaireGroupComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionnaireGroupDetailComponent,
    resolve: {
      questionnaireGroup: QuestionnaireGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionnaireGroupUpdateComponent,
    resolve: {
      questionnaireGroup: QuestionnaireGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionnaireGroupUpdateComponent,
    resolve: {
      questionnaireGroup: QuestionnaireGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionnaireGroupRoute)],
  exports: [RouterModule],
})
export class QuestionnaireGroupRoutingModule {}
