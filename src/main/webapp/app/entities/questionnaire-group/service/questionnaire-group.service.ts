import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionnaireGroup, NewQuestionnaireGroup } from '../questionnaire-group.model';

export type PartialUpdateQuestionnaireGroup = Partial<IQuestionnaireGroup> & Pick<IQuestionnaireGroup, 'id'>;

export type EntityResponseType = HttpResponse<IQuestionnaireGroup>;
export type EntityArrayResponseType = HttpResponse<IQuestionnaireGroup[]>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questionnaire-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionnaireGroup: NewQuestionnaireGroup): Observable<EntityResponseType> {
    return this.http.post<IQuestionnaireGroup>(this.resourceUrl, questionnaireGroup, { observe: 'response' });
  }

  update(questionnaireGroup: IQuestionnaireGroup): Observable<EntityResponseType> {
    return this.http.put<IQuestionnaireGroup>(
      `${this.resourceUrl}/${this.getQuestionnaireGroupIdentifier(questionnaireGroup)}`,
      questionnaireGroup,
      { observe: 'response' }
    );
  }

  partialUpdate(questionnaireGroup: PartialUpdateQuestionnaireGroup): Observable<EntityResponseType> {
    return this.http.patch<IQuestionnaireGroup>(
      `${this.resourceUrl}/${this.getQuestionnaireGroupIdentifier(questionnaireGroup)}`,
      questionnaireGroup,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionnaireGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionnaireGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuestionnaireGroupIdentifier(questionnaireGroup: Pick<IQuestionnaireGroup, 'id'>): number {
    return questionnaireGroup.id;
  }

  compareQuestionnaireGroup(o1: Pick<IQuestionnaireGroup, 'id'> | null, o2: Pick<IQuestionnaireGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuestionnaireGroupIdentifier(o1) === this.getQuestionnaireGroupIdentifier(o2) : o1 === o2;
  }

  addQuestionnaireGroupToCollectionIfMissing<Type extends Pick<IQuestionnaireGroup, 'id'>>(
    questionnaireGroupCollection: Type[],
    ...questionnaireGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const questionnaireGroups: Type[] = questionnaireGroupsToCheck.filter(isPresent);
    if (questionnaireGroups.length > 0) {
      const questionnaireGroupCollectionIdentifiers = questionnaireGroupCollection.map(
        questionnaireGroupItem => this.getQuestionnaireGroupIdentifier(questionnaireGroupItem)!
      );
      const questionnaireGroupsToAdd = questionnaireGroups.filter(questionnaireGroupItem => {
        const questionnaireGroupIdentifier = this.getQuestionnaireGroupIdentifier(questionnaireGroupItem);
        if (questionnaireGroupCollectionIdentifiers.includes(questionnaireGroupIdentifier)) {
          return false;
        }
        questionnaireGroupCollectionIdentifiers.push(questionnaireGroupIdentifier);
        return true;
      });
      return [...questionnaireGroupsToAdd, ...questionnaireGroupCollection];
    }
    return questionnaireGroupCollection;
  }
}
