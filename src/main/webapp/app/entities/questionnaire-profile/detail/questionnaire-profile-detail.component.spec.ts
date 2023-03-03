import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionnaireProfileDetailComponent } from './questionnaire-profile-detail.component';

describe('QuestionnaireProfile Management Detail Component', () => {
  let comp: QuestionnaireProfileDetailComponent;
  let fixture: ComponentFixture<QuestionnaireProfileDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuestionnaireProfileDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ questionnaireProfile: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QuestionnaireProfileDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuestionnaireProfileDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load questionnaireProfile on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.questionnaireProfile).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
