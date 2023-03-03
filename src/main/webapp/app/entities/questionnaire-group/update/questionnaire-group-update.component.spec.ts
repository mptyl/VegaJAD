import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionnaireGroupFormService } from './questionnaire-group-form.service';
import { QuestionnaireGroupService } from '../service/questionnaire-group.service';
import { IQuestionnaireGroup } from '../questionnaire-group.model';

import { QuestionnaireGroupUpdateComponent } from './questionnaire-group-update.component';

describe('QuestionnaireGroup Management Update Component', () => {
  let comp: QuestionnaireGroupUpdateComponent;
  let fixture: ComponentFixture<QuestionnaireGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionnaireGroupFormService: QuestionnaireGroupFormService;
  let questionnaireGroupService: QuestionnaireGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionnaireGroupUpdateComponent],
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
      .overrideTemplate(QuestionnaireGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionnaireGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionnaireGroupFormService = TestBed.inject(QuestionnaireGroupFormService);
    questionnaireGroupService = TestBed.inject(QuestionnaireGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const questionnaireGroup: IQuestionnaireGroup = { id: 456 };

      activatedRoute.data = of({ questionnaireGroup });
      comp.ngOnInit();

      expect(comp.questionnaireGroup).toEqual(questionnaireGroup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaireGroup>>();
      const questionnaireGroup = { id: 123 };
      jest.spyOn(questionnaireGroupFormService, 'getQuestionnaireGroup').mockReturnValue(questionnaireGroup);
      jest.spyOn(questionnaireGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaireGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaireGroup }));
      saveSubject.complete();

      // THEN
      expect(questionnaireGroupFormService.getQuestionnaireGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionnaireGroupService.update).toHaveBeenCalledWith(expect.objectContaining(questionnaireGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaireGroup>>();
      const questionnaireGroup = { id: 123 };
      jest.spyOn(questionnaireGroupFormService, 'getQuestionnaireGroup').mockReturnValue({ id: null });
      jest.spyOn(questionnaireGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaireGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaireGroup }));
      saveSubject.complete();

      // THEN
      expect(questionnaireGroupFormService.getQuestionnaireGroup).toHaveBeenCalled();
      expect(questionnaireGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaireGroup>>();
      const questionnaireGroup = { id: 123 };
      jest.spyOn(questionnaireGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaireGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionnaireGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
