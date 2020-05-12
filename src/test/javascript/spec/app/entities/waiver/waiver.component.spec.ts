import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MpsapcTestModule } from '../../../test.module';
import { WaiverComponent } from 'app/entities/waiver/waiver.component';
import { WaiverService } from 'app/entities/waiver/waiver.service';
import { Waiver } from 'app/shared/model/waiver.model';

describe('Component Tests', () => {
  describe('Waiver Management Component', () => {
    let comp: WaiverComponent;
    let fixture: ComponentFixture<WaiverComponent>;
    let service: WaiverService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [WaiverComponent]
      })
        .overrideTemplate(WaiverComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WaiverComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WaiverService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Waiver(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.waivers && comp.waivers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
