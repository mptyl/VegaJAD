import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionnaireProfileFormService } from './questionnaire-profile-form.service';
import { QuestionnaireProfileService } from '../service/questionnaire-profile.service';
import { IQuestionnaireProfile } from '../questionnaire-profile.model';

import { QuestionnaireProfileUpdateComponent } from './questionnaire-profile-update.component';

describe('QuestionnaireProfile Management Update Component', () => {
  let comp: QuestionnaireProfileUpdateComponent;
  let fixture: ComponentFixture<QuestionnaireProfileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionnaireProfileFormService: QuestionnaireProfileFormService;
  let questionnaireProfileService: QuestionnaireProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionnaireProfileUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QuestionnaireProfileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionnaireProfileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionnaireProfileFormService = TestBed.inject(QuestionnaireProfileFormService);
    questionnaireProfileService = TestBed.inject(QuestionnaireProfileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const questionnaireProfile: IQuestionnaireProfile = { id: 456 };

      activatedRoute.data = of({ questionnaireProfile });
      comp.ngOnInit();

      expect(comp.questionnaireProfile).toEqual(questionnaireProfile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaireProfile>>();
      const questionnaireProfile = { id: 123 };
      jest.spyOn(questionnaireProfileFormService, 'getQuestionnaireProfile').mockReturnValue(questionnaireProfile);
      jest.spyOn(questionnaireProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaireProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaireProfile }));
      saveSubject.complete();

      // THEN
      expect(questionnaireProfileFormService.getQuestionnaireProfile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionnaireProfileService.update).toHaveBeenCalledWith(expect.objectContaining(questionnaireProfile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaireProfile>>();
      const questionnaireProfile = { id: 123 };
      jest.spyOn(questionnaireProfileFormService, 'getQuestionnaireProfile').mockReturnValue({ id: null });
      jest.spyOn(questionnaireProfileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaireProfile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaireProfile }));
      saveSubject.complete();

      // THEN
      expect(questionnaireProfileFormService.getQuestionnaireProfile).toHaveBeenCalled();
      expect(questionnaireProfileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaireProfile>>();
      const questionnaireProfile = { id: 123 };
      jest.spyOn(questionnaireProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaireProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionnaireProfileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
