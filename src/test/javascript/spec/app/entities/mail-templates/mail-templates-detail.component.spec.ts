import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { MailTemplatesDetailComponent } from 'app/entities/mail-templates/mail-templates-detail.component';
import { MailTemplates } from 'app/shared/model/mail-templates.model';

describe('Component Tests', () => {
  describe('MailTemplates Management Detail Component', () => {
    let comp: MailTemplatesDetailComponent;
    let fixture: ComponentFixture<MailTemplatesDetailComponent>;
    const route = ({ data: of({ mailTemplates: new MailTemplates(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [MailTemplatesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MailTemplatesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MailTemplatesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mailTemplates on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mailTemplates).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
