import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionnaireGroup } from '../questionnaire-group.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../questionnaire-group.test-samples';

import { QuestionnaireGroupService } from './questionnaire-group.service';

const requireRestSample: IQuestionnaireGroup = {
  ...sampleWithRequiredData,
};

describe('QuestionnaireGroup Service', () => {
  let service: QuestionnaireGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IQuestionnaireGroup | IQuestionnaireGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionnaireGroupService);
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

    it('should create a QuestionnaireGroup', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const questionnaireGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(questionnaireGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuestionnaireGroup', () => {
      const questionnaireGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(questionnaireGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuestionnaireGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuestionnaireGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a QuestionnaireGroup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQuestionnaireGroupToCollectionIfMissing', () => {
      it('should add a QuestionnaireGroup to an empty array', () => {
        const questionnaireGroup: IQuestionnaireGroup = sampleWithRequiredData;
        expectedResult = service.addQuestionnaireGroupToCollectionIfMissing([], questionnaireGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionnaireGroup);
      });

      it('should not add a QuestionnaireGroup to an array that contains it', () => {
        const questionnaireGroup: IQuestionnaireGroup = sampleWithRequiredData;
        const questionnaireGroupCollection: IQuestionnaireGroup[] = [
          {
            ...questionnaireGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQuestionnaireGroupToCollectionIfMissing(questionnaireGroupCollection, questionnaireGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuestionnaireGroup to an array that doesn't contain it", () => {
        const questionnaireGroup: IQuestionnaireGroup = sampleWithRequiredData;
        const questionnaireGroupCollection: IQuestionnaireGroup[] = [sampleWithPartialData];
        expectedResult = service.addQuestionnaireGroupToCollectionIfMissing(questionnaireGroupCollection, questionnaireGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionnaireGroup);
      });

      it('should add only unique QuestionnaireGroup to an array', () => {
        const questionnaireGroupArray: IQuestionnaireGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const questionnaireGroupCollection: IQuestionnaireGroup[] = [sampleWithRequiredData];
        expectedResult = service.addQuestionnaireGroupToCollectionIfMissing(questionnaireGroupCollection, ...questionnaireGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionnaireGroup: IQuestionnaireGroup = sampleWithRequiredData;
        const questionnaireGroup2: IQuestionnaireGroup = sampleWithPartialData;
        expectedResult = service.addQuestionnaireGroupToCollectionIfMissing([], questionnaireGroup, questionnaireGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionnaireGroup);
        expect(expectedResult).toContain(questionnaireGroup2);
      });

      it('should accept null and undefined values', () => {
        const questionnaireGroup: IQuestionnaireGroup = sampleWithRequiredData;
        expectedResult = service.addQuestionnaireGroupToCollectionIfMissing([], null, questionnaireGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionnaireGroup);
      });

      it('should return initial array if no QuestionnaireGroup is added', () => {
        const questionnaireGroupCollection: IQuestionnaireGroup[] = [sampleWithRequiredData];
        expectedResult = service.addQuestionnaireGroupToCollectionIfMissing(questionnaireGroupCollection, undefined, null);
        expect(expectedResult).toEqual(questionnaireGroupCollection);
      });
    });

    describe('compareQuestionnaireGroup', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQuestionnaireGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQuestionnaireGroup(entity1, entity2);
        const compareResult2 = service.compareQuestionnaireGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQuestionnaireGroup(entity1, entity2);
        const compareResult2 = service.compareQuestionnaireGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQuestionnaireGroup(entity1, entity2);
        const compareResult2 = service.compareQuestionnaireGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
