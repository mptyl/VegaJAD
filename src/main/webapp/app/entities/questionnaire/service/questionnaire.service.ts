import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionnaire, NewQuestionnaire } from '../questionnaire.model';

export type PartialUpdateQuestionnaire = Partial<IQuestionnaire> & Pick<IQuestionnaire, 'id'>;

export type EntityResponseType = HttpResponse<IQuestionnaire>;
export type EntityArrayResponseType = HttpResponse<IQuestionnaire[]>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questionnaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionnaire: NewQuestionnaire): Observable<EntityResponseType> {
    return this.http.post<IQuestionnaire>(this.resourceUrl, questionnaire, { observe: 'response' });
  }

  update(questionnaire: IQuestionnaire): Observable<EntityResponseType> {
    return this.http.put<IQuestionnaire>(`${this.resourceUrl}/${this.getQuestionnaireIdentifier(questionnaire)}`, questionnaire, {
      observe: 'response',
    });
  }

  partialUpdate(questionnaire: PartialUpdateQuestionnaire): Observable<EntityResponseType> {
    return this.http.patch<IQuestionnaire>(`${this.resourceUrl}/${this.getQuestionnaireIdentifier(questionnaire)}`, questionnaire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionnaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionnaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuestionnaireIdentifier(questionnaire: Pick<IQuestionnaire, 'id'>): number {
    return questionnaire.id;
  }

  compareQuestionnaire(o1: Pick<IQuestionnaire, 'id'> | null, o2: Pick<IQuestionnaire, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuestionnaireIdentifier(o1) === this.getQuestionnaireIdentifier(o2) : o1 === o2;
  }

  addQuestionnaireToCollectionIfMissing<Type extends Pick<IQuestionnaire, 'id'>>(
    questionnaireCollection: Type[],
    ...questionnairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const questionnaires: Type[] = questionnairesToCheck.filter(isPresent);
    if (questionnaires.length > 0) {
      const questionnaireCollectionIdentifiers = questionnaireCollection.map(
        questionnaireItem => this.getQuestionnaireIdentifier(questionnaireItem)!
      );
      const questionnairesToAdd = questionnaires.filter(questionnaireItem => {
        const questionnaireIdentifier = this.getQuestionnaireIdentifier(questionnaireItem);
        if (questionnaireCollectionIdentifiers.includes(questionnaireIdentifier)) {
          return false;
        }
        questionnaireCollectionIdentifiers.push(questionnaireIdentifier);
        return true;
      });
      return [...questionnairesToAdd, ...questionnaireCollection];
    }
    return questionnaireCollection;
  }
}
