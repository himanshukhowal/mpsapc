import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MpsapcTestModule } from '../../../test.module';
import { ContactUsComponent } from 'app/entities/contact-us/contact-us.component';
import { ContactUsService } from 'app/entities/contact-us/contact-us.service';
import { ContactUs } from 'app/shared/model/contact-us.model';

describe('Component Tests', () => {
  describe('ContactUs Management Component', () => {
    let comp: ContactUsComponent;
    let fixture: ComponentFixture<ContactUsComponent>;
    let service: ContactUsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [ContactUsComponent]
      })
        .overrideTemplate(ContactUsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactUsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContactUsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ContactUs(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.contactuses && comp.contactuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
