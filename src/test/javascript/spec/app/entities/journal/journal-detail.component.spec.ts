import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { JournalDetailComponent } from 'app/entities/journal/journal-detail.component';
import { Journal } from 'app/shared/model/journal.model';

describe('Component Tests', () => {
  describe('Journal Management Detail Component', () => {
    let comp: JournalDetailComponent;
    let fixture: ComponentFixture<JournalDetailComponent>;
    const route = ({ data: of({ journal: new Journal(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [JournalDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JournalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JournalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load journal on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.journal).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
