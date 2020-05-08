import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { WaiverUpdateComponent } from 'app/entities/waiver/waiver-update.component';
import { WaiverService } from 'app/entities/waiver/waiver.service';
import { Waiver } from 'app/shared/model/waiver.model';

describe('Component Tests', () => {
  describe('Waiver Management Update Component', () => {
    let comp: WaiverUpdateComponent;
    let fixture: ComponentFixture<WaiverUpdateComponent>;
    let service: WaiverService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [WaiverUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WaiverUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WaiverUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WaiverService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Waiver(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Waiver();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
