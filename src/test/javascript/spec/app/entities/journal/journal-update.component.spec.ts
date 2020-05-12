import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { JournalUpdateComponent } from 'app/entities/journal/journal-update.component';
import { JournalService } from 'app/entities/journal/journal.service';
import { Journal } from 'app/shared/model/journal.model';

describe('Component Tests', () => {
  describe('Journal Management Update Component', () => {
    let comp: JournalUpdateComponent;
    let fixture: ComponentFixture<JournalUpdateComponent>;
    let service: JournalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [JournalUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(JournalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JournalUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JournalService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Journal(123);
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
        const entity = new Journal();
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
