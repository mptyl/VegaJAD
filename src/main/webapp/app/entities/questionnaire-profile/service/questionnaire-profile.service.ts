import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionnaireProfile, NewQuestionnaireProfile } from '../questionnaire-profile.model';

export type PartialUpdateQuestionnaireProfile = Partial<IQuestionnaireProfile> & Pick<IQuestionnaireProfile, 'id'>;

export type EntityResponseType = HttpResponse<IQuestionnaireProfile>;
export type EntityArrayResponseType = HttpResponse<IQuestionnaireProfile[]>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireProfileService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questionnaire-profiles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionnaireProfile: NewQuestionnaireProfile): Observable<EntityResponseType> {
    return this.http.post<IQuestionnaireProfile>(this.resourceUrl, questionnaireProfile, { observe: 'response' });
  }

  update(questionnaireProfile: IQuestionnaireProfile): Observable<EntityResponseType> {
    return this.http.put<IQuestionnaireProfile>(
      `${this.resourceUrl}/${this.getQuestionnaireProfileIdentifier(questionnaireProfile)}`,
      questionnaireProfile,
      { observe: 'response' }
    );
  }

  partialUpdate(questionnaireProfile: PartialUpdateQuestionnaireProfile): Observable<EntityResponseType> {
    return this.http.patch<IQuestionnaireProfile>(
      `${this.resourceUrl}/${this.getQuestionnaireProfileIdentifier(questionnaireProfile)}`,
      questionnaireProfile,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionnaireProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionnaireProfile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQuestionnaireProfileIdentifier(questionnaireProfile: Pick<IQuestionnaireProfile, 'id'>): number {
    return questionnaireProfile.id;
  }

  compareQuestionnaireProfile(o1: Pick<IQuestionnaireProfile, 'id'> | null, o2: Pick<IQuestionnaireProfile, 'id'> | null): boolean {
    return o1 && o2 ? this.getQuestionnaireProfileIdentifier(o1) === this.getQuestionnaireProfileIdentifier(o2) : o1 === o2;
  }

  addQuestionnaireProfileToCollectionIfMissing<Type extends Pick<IQuestionnaireProfile, 'id'>>(
    questionnaireProfileCollection: Type[],
    ...questionnaireProfilesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const questionnaireProfiles: Type[] = questionnaireProfilesToCheck.filter(isPresent);
    if (questionnaireProfiles.length > 0) {
      const questionnaireProfileCollectionIdentifiers = questionnaireProfileCollection.map(
        questionnaireProfileItem => this.getQuestionnaireProfileIdentifier(questionnaireProfileItem)!
      );
      const questionnaireProfilesToAdd = questionnaireProfiles.filter(questionnaireProfileItem => {
        const questionnaireProfileIdentifier = this.getQuestionnaireProfileIdentifier(questionnaireProfileItem);
        if (questionnaireProfileCollectionIdentifiers.includes(questionnaireProfileIdentifier)) {
          return false;
        }
        questionnaireProfileCollectionIdentifiers.push(questionnaireProfileIdentifier);
        return true;
      });
      return [...questionnaireProfilesToAdd, ...questionnaireProfileCollection];
    }
    return questionnaireProfileCollection;
  }
}
