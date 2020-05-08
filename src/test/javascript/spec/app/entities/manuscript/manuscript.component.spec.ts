import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MpsapcTestModule } from '../../../test.module';
import { ManuscriptComponent } from 'app/entities/manuscript/manuscript.component';
import { ManuscriptService } from 'app/entities/manuscript/manuscript.service';
import { Manuscript } from 'app/shared/model/manuscript.model';

describe('Component Tests', () => {
  describe('Manuscript Management Component', () => {
    let comp: ManuscriptComponent;
    let fixture: ComponentFixture<ManuscriptComponent>;
    let service: ManuscriptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [ManuscriptComponent]
      })
        .overrideTemplate(ManuscriptComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ManuscriptComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ManuscriptService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Manuscript(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.manuscripts && comp.manuscripts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
