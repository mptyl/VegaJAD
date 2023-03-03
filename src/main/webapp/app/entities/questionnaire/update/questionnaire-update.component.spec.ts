import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionnaireFormService } from './questionnaire-form.service';
import { QuestionnaireService } from '../service/questionnaire.service';
import { IQuestionnaire } from '../questionnaire.model';
import { IQuestionnaireGroup } from 'app/entities/questionnaire-group/questionnaire-group.model';
import { QuestionnaireGroupService } from 'app/entities/questionnaire-group/service/questionnaire-group.service';
import { IQuestionnaireProfile } from 'app/entities/questionnaire-profile/questionnaire-profile.model';
import { QuestionnaireProfileService } from 'app/entities/questionnaire-profile/service/questionnaire-profile.service';

import { QuestionnaireUpdateComponent } from './questionnaire-update.component';

describe('Questionnaire Management Update Component', () => {
  let comp: QuestionnaireUpdateComponent;
  let fixture: ComponentFixture<QuestionnaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionnaireFormService: QuestionnaireFormService;
  let questionnaireService: QuestionnaireService;
  let questionnaireGroupService: QuestionnaireGroupService;
  let questionnaireProfileService: QuestionnaireProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionnaireUpdateComponent],
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
      .overrideTemplate(QuestionnaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionnaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionnaireFormService = TestBed.inject(QuestionnaireFormService);
    questionnaireService = TestBed.inject(QuestionnaireService);
    questionnaireGroupService = TestBed.inject(QuestionnaireGroupService);
    questionnaireProfileService = TestBed.inject(QuestionnaireProfileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call QuestionnaireGroup query and add missing value', () => {
      const questionnaire: IQuestionnaire = { id: 456 };
      const questionnaireGroup: IQuestionnaireGroup = { id: 56989 };
      questionnaire.questionnaireGroup = questionnaireGroup;

      const questionnaireGroupCollection: IQuestionnaireGroup[] = [{ id: 75361 }];
      jest.spyOn(questionnaireGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: questionnaireGroupCollection })));
      const additionalQuestionnaireGroups = [questionnaireGroup];
      const expectedCollection: IQuestionnaireGroup[] = [...additionalQuestionnaireGroups, ...questionnaireGroupCollection];
      jest.spyOn(questionnaireGroupService, 'addQuestionnaireGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      expect(questionnaireGroupService.query).toHaveBeenCalled();
      expect(questionnaireGroupService.addQuestionnaireGroupToCollectionIfMissing).toHaveBeenCalledWith(
        questionnaireGroupCollection,
        ...additionalQuestionnaireGroups.map(expect.objectContaining)
      );
      expect(comp.questionnaireGroupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call QuestionnaireProfile query and add missing value', () => {
      const questionnaire: IQuestionnaire = { id: 456 };
      const questionnaireProfile: IQuestionnaireProfile = { id: 20943 };
      questionnaire.questionnaireProfile = questionnaireProfile;

      const questionnaireProfileCollection: IQuestionnaireProfile[] = [{ id: 45132 }];
      jest.spyOn(questionnaireProfileService, 'query').mockReturnValue(of(new HttpResponse({ body: questionnaireProfileCollection })));
      const additionalQuestionnaireProfiles = [questionnaireProfile];
      const expectedCollection: IQuestionnaireProfile[] = [...additionalQuestionnaireProfiles, ...questionnaireProfileCollection];
      jest.spyOn(questionnaireProfileService, 'addQuestionnaireProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      expect(questionnaireProfileService.query).toHaveBeenCalled();
      expect(questionnaireProfileService.addQuestionnaireProfileToCollectionIfMissing).toHaveBeenCalledWith(
        questionnaireProfileCollection,
        ...additionalQuestionnaireProfiles.map(expect.objectContaining)
      );
      expect(comp.questionnaireProfilesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questionnaire: IQuestionnaire = { id: 456 };
      const questionnaireGroup: IQuestionnaireGroup = { id: 19791 };
      questionnaire.questionnaireGroup = questionnaireGroup;
      const questionnaireProfile: IQuestionnaireProfile = { id: 88985 };
      questionnaire.questionnaireProfile = questionnaireProfile;

      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      expect(comp.questionnaireGroupsSharedCollection).toContain(questionnaireGroup);
      expect(comp.questionnaireProfilesSharedCollection).toContain(questionnaireProfile);
      expect(comp.questionnaire).toEqual(questionnaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaire>>();
      const questionnaire = { id: 123 };
      jest.spyOn(questionnaireFormService, 'getQuestionnaire').mockReturnValue(questionnaire);
      jest.spyOn(questionnaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaire }));
      saveSubject.complete();

      // THEN
      expect(questionnaireFormService.getQuestionnaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionnaireService.update).toHaveBeenCalledWith(expect.objectContaining(questionnaire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaire>>();
      const questionnaire = { id: 123 };
      jest.spyOn(questionnaireFormService, 'getQuestionnaire').mockReturnValue({ id: null });
      jest.spyOn(questionnaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaire }));
      saveSubject.complete();

      // THEN
      expect(questionnaireFormService.getQuestionnaire).toHaveBeenCalled();
      expect(questionnaireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuestionnaire>>();
      const questionnaire = { id: 123 };
      jest.spyOn(questionnaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionnaireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQuestionnaireGroup', () => {
      it('Should forward to questionnaireGroupService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionnaireGroupService, 'compareQuestionnaireGroup');
        comp.compareQuestionnaireGroup(entity, entity2);
        expect(questionnaireGroupService.compareQuestionnaireGroup).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareQuestionnaireProfile', () => {
      it('Should forward to questionnaireProfileService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(questionnaireProfileService, 'compareQuestionnaireProfile');
        comp.compareQuestionnaireProfile(entity, entity2);
        expect(questionnaireProfileService.compareQuestionnaireProfile).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
