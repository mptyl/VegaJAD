import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionnaireGroup } from '../questionnaire-group.model';
import { QuestionnaireGroupService } from '../service/questionnaire-group.service';

@Injectable({ providedIn: 'root' })
export class QuestionnaireGroupRoutingResolveService implements Resolve<IQuestionnaireGroup | null> {
  constructor(protected service: QuestionnaireGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionnaireGroup | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionnaireGroup: HttpResponse<IQuestionnaireGroup>) => {
          if (questionnaireGroup.body) {
            return of(questionnaireGroup.body);
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
