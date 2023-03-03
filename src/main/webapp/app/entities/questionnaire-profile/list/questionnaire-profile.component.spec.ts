import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuestionnaireProfileService } from '../service/questionnaire-profile.service';

import { QuestionnaireProfileComponent } from './questionnaire-profile.component';

describe('QuestionnaireProfile Management Component', () => {
  let comp: QuestionnaireProfileComponent;
  let fixture: ComponentFixture<QuestionnaireProfileComponent>;
  let service: QuestionnaireProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'questionnaire-profile', component: QuestionnaireProfileComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [QuestionnaireProfileComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(QuestionnaireProfileComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionnaireProfileComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuestionnaireProfileService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.questionnaireProfiles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to questionnaireProfileService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getQuestionnaireProfileIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getQuestionnaireProfileIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
