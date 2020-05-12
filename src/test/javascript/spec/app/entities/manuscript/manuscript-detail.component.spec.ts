import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MpsapcTestModule } from '../../../test.module';
import { ManuscriptDetailComponent } from 'app/entities/manuscript/manuscript-detail.component';
import { Manuscript } from 'app/shared/model/manuscript.model';

describe('Component Tests', () => {
  describe('Manuscript Management Detail Component', () => {
    let comp: ManuscriptDetailComponent;
    let fixture: ComponentFixture<ManuscriptDetailComponent>;
    const route = ({ data: of({ manuscript: new Manuscript(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MpsapcTestModule],
        declarations: [ManuscriptDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ManuscriptDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ManuscriptDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load manuscript on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.manuscript).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
