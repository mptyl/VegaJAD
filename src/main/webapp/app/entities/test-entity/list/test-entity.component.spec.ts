import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TestEntityService } from '../service/test-entity.service';

import { TestEntityComponent } from './test-entity.component';

describe('TestEntity Management Component', () => {
  let comp: TestEntityComponent;
  let fixture: ComponentFixture<TestEntityComponent>;
  let service: TestEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'test-entity', component: TestEntityComponent }]), HttpClientTestingModule],
      declarations: [TestEntityComponent],
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
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(TestEntityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestEntityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TestEntityService);

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
    expect(comp.testEntities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to testEntityService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTestEntityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTestEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
