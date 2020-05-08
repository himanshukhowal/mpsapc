import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { MailTemplatesUpdateComponent } from 'app/entities/mail-templates/mail-templates-update.component';
import { MailTemplatesService } from 'app/entities/mail-templates/mail-templates.service';
import { MailTemplates } from 'app/shared/model/mail-templates.model';

describe('Component Tests', () => {
  describe('MailTemplates Management Update Component', () => {
    let comp: MailTemplatesUpdateComponent;
    let fixture: ComponentFixture<MailTemplatesUpdateComponent>;
    let service: MailTemplatesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [MailTemplatesUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MailTemplatesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MailTemplatesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MailTemplatesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MailTemplates(123);
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
        const entity = new MailTemplates();
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
