import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MpsapcTestModule } from '../../../test.module';
import { MailTemplatesComponent } from 'app/entities/mail-templates/mail-templates.component';
import { MailTemplatesService } from 'app/entities/mail-templates/mail-templates.service';
import { MailTemplates } from 'app/shared/model/mail-templates.model';

describe('Component Tests', () => {
  describe('MailTemplates Management Component', () => {
    let comp: MailTemplatesComponent;
    let fixture: ComponentFixture<MailTemplatesComponent>;
    let service: MailTemplatesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [MailTemplatesComponent]
      })
        .overrideTemplate(MailTemplatesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MailTemplatesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MailTemplatesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MailTemplates(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.mailTemplates && comp.mailTemplates[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
