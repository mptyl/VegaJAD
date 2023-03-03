import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionnaireProfile } from '../questionnaire-profile.model';
import { QuestionnaireProfileService } from '../service/questionnaire-profile.service';

@Injectable({ providedIn: 'root' })
export class QuestionnaireProfileRoutingResolveService implements Resolve<IQuestionnaireProfile | null> {
  constructor(protected service: QuestionnaireProfileService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionnaireProfile | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionnaireProfile: HttpResponse<IQuestionnaireProfile>) => {
          if (questionnaireProfile.body) {
            return of(questionnaireProfile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
