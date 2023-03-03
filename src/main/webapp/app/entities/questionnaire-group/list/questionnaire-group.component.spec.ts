import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuestionnaireGroupService } from '../service/questionnaire-group.service';

import { QuestionnaireGroupComponent } from './questionnaire-group.component';

describe('QuestionnaireGroup Management Component', () => {
  let comp: QuestionnaireGroupComponent;
  let fixture: ComponentFixture<QuestionnaireGroupComponent>;
  let service: QuestionnaireGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'questionnaire-group', component: QuestionnaireGroupComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [QuestionnaireGroupComponent],
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
      .overrideTemplate(QuestionnaireGroupComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionnaireGroupComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuestionnaireGroupService);

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
    expect(comp.questionnaireGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to questionnaireGroupService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getQuestionnaireGroupIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getQuestionnaireGroupIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
