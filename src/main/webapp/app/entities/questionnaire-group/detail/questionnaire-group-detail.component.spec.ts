import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuestionnaireGroupDetailComponent } from './questionnaire-group-detail.component';

describe('QuestionnaireGroup Management Detail Component', () => {
  let comp: QuestionnaireGroupDetailComponent;
  let fixture: ComponentFixture<QuestionnaireGroupDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuestionnaireGroupDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ questionnaireGroup: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QuestionnaireGroupDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuestionnaireGroupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load questionnaireGroup on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.questionnaireGroup).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
