import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LivreDetailComponent } from './livre-detail.component';

describe('Component Tests', () => {
  describe('Livre Management Detail Component', () => {
    let comp: LivreDetailComponent;
    let fixture: ComponentFixture<LivreDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LivreDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ livre: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LivreDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LivreDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load livre on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.livre).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
