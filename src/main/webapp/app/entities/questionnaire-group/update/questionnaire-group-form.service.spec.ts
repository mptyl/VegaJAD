import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../questionnaire-group.test-samples';

import { QuestionnaireGroupFormService } from './questionnaire-group-form.service';

describe('QuestionnaireGroup Form Service', () => {
  let service: QuestionnaireGroupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionnaireGroupFormService);
  });

  describe('Service methods', () => {
    describe('createQuestionnaireGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuestionnaireGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing IQuestionnaireGroup should create a new form with FormGroup', () => {
        const formGroup = service.createQuestionnaireGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getQuestionnaireGroup', () => {
      it('should return NewQuestionnaireGroup for default QuestionnaireGroup initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createQuestionnaireGroupFormGroup(sampleWithNewData);

        const questionnaireGroup = service.getQuestionnaireGroup(formGroup) as any;

        expect(questionnaireGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuestionnaireGroup for empty QuestionnaireGroup initial value', () => {
        const formGroup = service.createQuestionnaireGroupFormGroup();

        const questionnaireGroup = service.getQuestionnaireGroup(formGroup) as any;

        expect(questionnaireGroup).toMatchObject({});
      });

      it('should return IQuestionnaireGroup', () => {
        const formGroup = service.createQuestionnaireGroupFormGroup(sampleWithRequiredData);

        const questionnaireGroup = service.getQuestionnaireGroup(formGroup) as any;

        expect(questionnaireGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuestionnaireGroup should not enable id FormControl', () => {
        const formGroup = service.createQuestionnaireGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuestionnaireGroup should disable id FormControl', () => {
        const formGroup = service.createQuestionnaireGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
