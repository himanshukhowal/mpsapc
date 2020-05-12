import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MpsapcTestModule } from '../../../test.module';
import { JournalComponent } from 'app/entities/journal/journal.component';
import { JournalService } from 'app/entities/journal/journal.service';
import { Journal } from 'app/shared/model/journal.model';

describe('Component Tests', () => {
  describe('Journal Management Component', () => {
    let comp: JournalComponent;
    let fixture: ComponentFixture<JournalComponent>;
    let service: JournalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [JournalComponent]
      })
        .overrideTemplate(JournalComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JournalComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JournalService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Journal(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.journals && comp.journals[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
