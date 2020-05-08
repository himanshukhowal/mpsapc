import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { MailDetailComponent } from 'app/entities/mail/mail-detail.component';
import { Mail } from 'app/shared/model/mail.model';

describe('Component Tests', () => {
  describe('Mail Management Detail Component', () => {
    let comp: MailDetailComponent;
    let fixture: ComponentFixture<MailDetailComponent>;
    const route = ({ data: of({ mail: new Mail(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [MailDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MailDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MailDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mail on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mail).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
