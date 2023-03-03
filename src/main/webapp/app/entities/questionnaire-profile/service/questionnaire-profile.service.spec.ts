import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionnaireProfile } from '../questionnaire-profile.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../questionnaire-profile.test-samples';

import { QuestionnaireProfileService } from './questionnaire-profile.service';

const requireRestSample: IQuestionnaireProfile = {
  ...sampleWithRequiredData,
};

describe('QuestionnaireProfile Service', () => {
  let service: QuestionnaireProfileService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuestionnaireProfile | IQuestionnaireProfile[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionnaireProfileService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a QuestionnaireProfile', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const questionnaireProfile = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(questionnaireProfile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuestionnaireProfile', () => {
      const questionnaireProfile = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(questionnaireProfile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuestionnaireProfile', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuestionnaireProfile', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuestionnaireProfile', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuestionnaireProfileToCollectionIfMissing', () => {
      it('should add a QuestionnaireProfile to an empty array', () => {
        const questionnaireProfile: IQuestionnaireProfile = sampleWithRequiredData;
        expectedResult = service.addQuestionnaireProfileToCollectionIfMissing([], questionnaireProfile);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionnaireProfile);
      });

      it('should not add a QuestionnaireProfile to an array that contains it', () => {
        const questionnaireProfile: IQuestionnaireProfile = sampleWithRequiredData;
        const questionnaireProfileCollection: IQuestionnaireProfile[] = [
          {
            ...questionnaireProfile,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuestionnaireProfileToCollectionIfMissing(questionnaireProfileCollection, questionnaireProfile);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuestionnaireProfile to an array that doesn't contain it", () => {
        const questionnaireProfile: IQuestionnaireProfile = sampleWithRequiredData;
        const questionnaireProfileCollection: IQuestionnaireProfile[] = [sampleWithPartialData];
        expectedResult = service.addQuestionnaireProfileToCollectionIfMissing(questionnaireProfileCollection, questionnaireProfile);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionnaireProfile);
      });

      it('should add only unique QuestionnaireProfile to an array', () => {
        const questionnaireProfileArray: IQuestionnaireProfile[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const questionnaireProfileCollection: IQuestionnaireProfile[] = [sampleWithRequiredData];
        expectedResult = service.addQuestionnaireProfileToCollectionIfMissing(questionnaireProfileCollection, ...questionnaireProfileArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionnaireProfile: IQuestionnaireProfile = sampleWithRequiredData;
        const questionnaireProfile2: IQuestionnaireProfile = sampleWithPartialData;
        expectedResult = service.addQuestionnaireProfileToCollectionIfMissing([], questionnaireProfile, questionnaireProfile2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionnaireProfile);
        expect(expectedResult).toContain(questionnaireProfile2);
      });

      it('should accept null and undefined values', () => {
        const questionnaireProfile: IQuestionnaireProfile = sampleWithRequiredData;
        expectedResult = service.addQuestionnaireProfileToCollectionIfMissing([], null, questionnaireProfile, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionnaireProfile);
      });

      it('should return initial array if no QuestionnaireProfile is added', () => {
        const questionnaireProfileCollection: IQuestionnaireProfile[] = [sampleWithRequiredData];
        expectedResult = service.addQuestionnaireProfileToCollectionIfMissing(questionnaireProfileCollection, undefined, null);
        expect(expectedResult).toEqual(questionnaireProfileCollection);
      });
    });

    describe('compareQuestionnaireProfile', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuestionnaireProfile(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuestionnaireProfile(entity1, entity2);
        const compareResult2 = service.compareQuestionnaireProfile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuestionnaireProfile(entity1, entity2);
        const compareResult2 = service.compareQuestionnaireProfile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuestionnaireProfile(entity1, entity2);
        const compareResult2 = service.compareQuestionnaireProfile(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
