import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../questionnaire-profile.test-samples';

import { QuestionnaireProfileFormService } from './questionnaire-profile-form.service';

describe('QuestionnaireProfile Form Service', () => {
  let service: QuestionnaireProfileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionnaireProfileFormService);
  });

  describe('Service methods', () => {
    describe('createQuestionnaireProfileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQuestionnaireProfileFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing IQuestionnaireProfile should create a new form with FormGroup', () => {
        const formGroup = service.createQuestionnaireProfileFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getQuestionnaireProfile', () => {
      it('should return NewQuestionnaireProfile for default QuestionnaireProfile initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createQuestionnaireProfileFormGroup(sampleWithNewData);

        const questionnaireProfile = service.getQuestionnaireProfile(formGroup) as any;

        expect(questionnaireProfile).toMatchObject(sampleWithNewData);
      });

      it('should return NewQuestionnaireProfile for empty QuestionnaireProfile initial value', () => {
        const formGroup = service.createQuestionnaireProfileFormGroup();

        const questionnaireProfile = service.getQuestionnaireProfile(formGroup) as any;

        expect(questionnaireProfile).toMatchObject({});
      });

      it('should return IQuestionnaireProfile', () => {
        const formGroup = service.createQuestionnaireProfileFormGroup(sampleWithRequiredData);

        const questionnaireProfile = service.getQuestionnaireProfile(formGroup) as any;

        expect(questionnaireProfile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQuestionnaireProfile should not enable id FormControl', () => {
        const formGroup = service.createQuestionnaireProfileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQuestionnaireProfile should disable id FormControl', () => {
        const formGroup = service.createQuestionnaireProfileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
