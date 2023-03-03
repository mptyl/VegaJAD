import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'questionnaire',
        data: { pageTitle: 'vegaJaApp.questionnaire.home.title' },
        loadChildren: () => import('./questionnaire/questionnaire.module').then(m => m.QuestionnaireModule),
      },
      {
        path: 'test-entity',
        data: { pageTitle: 'vegaJaApp.testEntity.home.title' },
        loadChildren: () => import('./test-entity/test-entity.module').then(m => m.TestEntityModule),
      },
      {
        path: 'questionnaire-profile',
        data: { pageTitle: 'vegaJaApp.questionnaireProfile.home.title' },
        loadChildren: () => import('./questionnaire-profile/questionnaire-profile.module').then(m => m.QuestionnaireProfileModule),
      },
      {
        path: 'questionnaire-group',
        data: { pageTitle: 'vegaJaApp.questionnaireGroup.home.title' },
        loadChildren: () => import('./questionnaire-group/questionnaire-group.module').then(m => m.QuestionnaireGroupModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
