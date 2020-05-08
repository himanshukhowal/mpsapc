import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { ManuscriptUpdateComponent } from 'app/entities/manuscript/manuscript-update.component';
import { ManuscriptService } from 'app/entities/manuscript/manuscript.service';
import { Manuscript } from 'app/shared/model/manuscript.model';

describe('Component Tests', () => {
  describe('Manuscript Management Update Component', () => {
    let comp: ManuscriptUpdateComponent;
    let fixture: ComponentFixture<ManuscriptUpdateComponent>;
    let service: ManuscriptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [ManuscriptUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ManuscriptUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ManuscriptUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ManuscriptService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Manuscript(123);
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
        const entity = new Manuscript();
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
